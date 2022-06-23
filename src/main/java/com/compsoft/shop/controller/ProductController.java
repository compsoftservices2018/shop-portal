package com.compsoft.shop.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.compsoft.shop.model.MstCustomer;
import com.compsoft.shop.model.MstProduct;
import com.compsoft.shop.model.MstProductKey;
import com.compsoft.shop.model.TranOrder;
import com.compsoft.shop.service.MstProductService;
import com.compsoft.shop.service.TranCustCartService;
import com.framework.controller.SuperController;
import com.framework.utils.AlertUtils;
import com.framework.utils.AppUtils;
import com.framework.utils.FrameworkConstants;
import com.framework.utils.GlobalValues;


/**
 * @author Pradeep Chawadkar
 *
 */
@Controller
public class ProductController extends SuperController {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private MstProductService MstProductService;

	@Autowired
	private TranCustCartService TranCustCartService;

	@RequestMapping(value = "/SaveProduct", method = RequestMethod.POST)
	public @ResponseBody JSONObject saveProduct(@ModelAttribute("MstProduct") MstProduct foMstProduct,
			BindingResult foResult,
			HttpSession foHttpSession, HttpServletRequest foHttpRequest) {
		AlertUtils.resetMessage(foHttpSession);
		JSONObject loReturn = new JSONObject();
		try {
			foMstProduct.getMstProductKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			foMstProduct.getMstProductKey().setVendor_code(GlobalValues.getVendorCode(foHttpSession));
			
			if (foMstProduct.getPage_mode() == null 
					|| foMstProduct.getPage_mode().equals("N")) {
				if (MstProductService.getProduct(foMstProduct.getMstProductKey()) != null) {
					loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
					loReturn.put("response_error", "Product already exists with this code");
					return loReturn;
				}
			}
			
			foMstProduct.setStart_date(AppUtils.getFormattedTimestamp((String)foHttpRequest.getParameter("start_date")));
			foMstProduct.setEnd_date(AppUtils.getFormattedTimestamp((String)foHttpRequest.getParameter("end_date")));

			Session loSession = startTransation(sessionFactory);
			String lsError = foMstProduct.validate();
			if (AppUtils.isValueEmpty(lsError)) {
				MstProductService.addProduct(loSession, foMstProduct);
				if (commitNoMsg(loSession.getTransaction(), loSession, foHttpSession)) {
					loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
					loReturn.put("response_success",
							"Product saved successfully. Please press Refresh/Search button to get updated list.");
				} else {
					loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
					loReturn.put("response_error", "Unable to save product. Please contact system administrator.");
				}
			} else {
				loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
				loReturn.put("response_error", lsError);
			}
			return loReturn;

		} catch (Exception ex) {
			logException(ex, foHttpSession);
			loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
			loReturn.put("response_error", "Unable to save product. " + ex.getMessage());
			return loReturn;
		}
	}

	@RequestMapping(value = "/GetProductInfo", method = RequestMethod.GET)
	public @ResponseBody JSONObject getProductInfo(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse, @RequestParam String requestData) {
		AlertUtils.resetMessage(foHttpSession);
		JSONObject loObject = new JSONObject();
		try {

			JSONParser parser = new JSONParser();
			JSONObject requestJson = (JSONObject) parser.parse(requestData);
			String product_code = (String) requestJson.get("product_code");
			String vendor_code = (String) requestJson.get("vendor_code");
			String selling_price = (String) requestJson.get("selling_price");
			MstProductKey loProductKey = new MstProductKey();
			loProductKey.setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			loProductKey.setProduct_code(product_code);
			loProductKey.setVendor_code(vendor_code);
			loProductKey.setSelling_price(new BigDecimal(selling_price));
			
			parser = new JSONParser();
			loObject = MstProductService.getProduct(loProductKey);
			
			String jsonInString = moGson.toJson(loObject);
			JSONObject responseJson = (JSONObject) parser.parse(jsonInString);
			
			if (responseJson != null) {
				responseJson.put("response_status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
			} else {
				responseJson.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
			}
			
			
			return responseJson;
		} catch (Exception ex) {
			loObject.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
			return loObject;
		}

	}

	@RequestMapping(value = "/SearchProducts", method = RequestMethod.POST)
	public ModelAndView searchProducts(HttpSession foHttpSession, String product_cat, String product_name) {
		AlertUtils.resetMessage(foHttpSession);
		try {
			Map<String, Object> model = new HashMap<String, Object>();

			if (AppUtils.isValueEmpty((String) foHttpSession.getAttribute("CUSTOMER_LOCATION"))) {
				model.put("location", " ");
				return new ModelAndView("/app/jsp/Home", model);
			}

			MstProduct loProduct = new MstProduct();
			loProduct.getMstProductKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			loProduct.setGroup_name(product_cat);
			loProduct.setProduct_name(product_name);

			TranOrder loTranOrder = (TranOrder) foHttpSession.getAttribute("CURRENT_ORDER");
			String lsOrderNo = null;
			if (loTranOrder != null) {
				lsOrderNo = loTranOrder.getTranOrderKey().getOrder_no();
			}

			List<Map<String, Object>> loProducts = TranCustCartService.getProductForLocation(loProduct,
					(String) foHttpSession.getAttribute("CUSTOMER_LOCATION"), lsOrderNo);

			if (loProducts == null) {
				AlertUtils.addInfo("No products found.", foHttpSession);
				return new ModelAndView("/app/jsp/Home");
			}
			model.put("ProductList", loProducts);
			return new ModelAndView("/Shop/List_Product", model);
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			return new ModelAndView("/app/jsp/Home");
		}
	}

	
	@RequestMapping(value = "/ManageProducts", method = RequestMethod.GET)
	public ModelAndView manageProducts(@ModelAttribute("MstProduct") MstProduct loProduct, HttpSession foHttpSession,
			BindingResult foResult, HttpServletRequest foHttpRequest) {
		AlertUtils.resetMessage(foHttpSession);
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			return new ModelAndView("/Shop/Main_ManageProducts");
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			ex.printStackTrace();
			return new ModelAndView("/app/jsp/Home");
		}
	}

	@RequestMapping(value = "/GetProducts", method = RequestMethod.GET)
	public ModelAndView getProducts(@ModelAttribute("MstProduct") MstProduct loProduct, HttpSession foHttpSession,
			BindingResult foResult) {
		AlertUtils.resetMessage(foHttpSession);
		try {
			MstCustomer loUser= (MstCustomer) foHttpSession.getAttribute("LOGGEDINCUSTOMER");
			Map<String, Object> model = new HashMap<String, Object>();
			loProduct.getMstProductKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			loProduct.getMstProductKey().setVendor_code(GlobalValues.getVendorCode(foHttpSession));
			List<Map<String, Object>> loAllProducts = MstProductService.getProductList(loProduct);
			model.put("AllProducts", loAllProducts);
			return new ModelAndView("/Shop/Main_ManageProducts", model);
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			ex.printStackTrace();
			return new ModelAndView("/app/jsp/Home");
		}
	}

	

}
