package com.compsoft.shop.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.compsoft.shop.model.MstCustomer;
import com.compsoft.shop.model.MstProduct;
import com.compsoft.shop.model.TranOrder;
import com.compsoft.shop.reference.AppReferenceUtils;
import com.compsoft.shop.service.MstCustomerService;
import com.compsoft.shop.service.TranCustCartService;
import com.compsoft.shop.service.TranOrderService;
import com.framework.controller.SuperController;
import com.framework.model.MstCompany;
import com.framework.model.MstUser;
import com.framework.reference.ReferenceUtils;
import com.framework.service.MstCompanyService;
import com.framework.utils.AlertUtils;
import com.framework.utils.AppConstants;
import com.framework.utils.AppUtils;
import com.framework.utils.GlobalValues;

/**
 * @author Pradeep Chawadkar
 *
 */
/**
 * @author Pradeep
 *
 */
@Controller
public class ShopOnlineController extends SuperController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MstCustomerService MstCustomerService;

	@Autowired
	private MstCompanyService MstCompanyService;

	@Autowired
	private TranCustCartService TranCustCartService;

	@Autowired
	private TranOrderService TranOrderService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView onlineShop(HttpSession foHttpSession, HttpServletRequest foHttpRequest, String circle) {

		try {
			AlertUtils.resetMessage(foHttpSession);
			Map<String, Object> model = new HashMap<String, Object>();
			// String lsCode = Security.decrypt(circle);

			MstCompany loMstCompany = null;

			loMstCompany = MstCompanyService.getCompanyByKey(circle);

			if (loMstCompany == null) {
				AlertUtils.addError("Company has not been registered.", foHttpSession);
				return new ModelAndView("/app/jsp/Home");
			}
			ReferenceUtils.setReferenceDataCompany(foHttpSession, jdbcTemplate,
					loMstCompany.getMstCompanyKey().getCompany_code());
			foHttpSession.setAttribute("COMPANYOBJ", loMstCompany);
			foHttpSession.setAttribute("TITLE", loMstCompany.getCompany_name());
			AppReferenceUtils.setAppReferenceDataBeforeLogin(foHttpSession, jdbcTemplate);
			/*if (AppUtils.isValueEmpty((String) foHttpSession.getAttribute("CUSTOMER_LOCATION"))) {
				model.put("location", " ");
			}*/

			return new ModelAndView("/app/jsp/Home", model);
		} catch (Exception ex) {
			
			logException(ex, foHttpSession);
			return new ModelAndView("/app/jsp/Home");
		}
	}

	@RequestMapping(value = "/Contact", method = RequestMethod.GET)
	public ModelAndView contact(HttpSession foHttpSession) {
		AlertUtils.resetMessage(foHttpSession);
		return new ModelAndView("/Shop/Main_Contact");
	}

	@RequestMapping(value = "/ContinueShopping", method = RequestMethod.GET)
	public ModelAndView ContinueShopping(HttpSession foHttpSession, HttpServletRequest foHttpRequest) {
		AlertUtils.resetMessage(foHttpSession);
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			MstProduct loProduct = new MstProduct();
			loProduct.getMstProductKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			MstCustomer loCustomer = (MstCustomer) foHttpSession.getAttribute("LOGGEDINCUSTOMER");
			TranOrder loTranOrder = (TranOrder) foHttpSession.getAttribute("CURRENT_ORDER");
			String lsOrderNo = null;
			if (loTranOrder!= null)
			{
				lsOrderNo = loTranOrder.getTranOrderKey().getOrder_no();
			}
			List<Map<String, Object>> loProducts = TranCustCartService.getProductForLocation(loProduct,
					(String) foHttpSession.getAttribute("CUSTOMER_LOCATION"), lsOrderNo);
			model.put("ProductList", loProducts);
			return new ModelAndView("/app/jsp/Home", model);
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			return new ModelAndView("/app/jsp/Home");
		}

	}

	@RequestMapping(value = "/ShopOnlineSignin", method = RequestMethod.POST)
	public ModelAndView signIn(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			@RequestParam String login_id, @RequestParam String password) {
		AlertUtils.resetMessage(foHttpSession);
		try {
			AlertUtils.resetMessage(foHttpSession);
			Map<String, Object> model = new HashMap<String, Object>();

			MstCustomer loMstCustomer = new MstCustomer();
			loMstCustomer.getMstCustomerKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			loMstCustomer.getMstCustomerKey().setCustomer_code(login_id);
			loMstCustomer.setMobile(login_id);
			loMstCustomer.setEmail(login_id);
			MstCustomer loCustomer = MstCustomerService.getCustomer(loMstCustomer);

			if (loCustomer == null) {
				model.put("user_error", "Authentication failed!!!! Invalid Login id.");
				return new ModelAndView("/Shop/Main_SignIn", model);
			}

			if (password.equals(loCustomer.getPassword())) {
				foHttpSession.setAttribute("LOGGEDINCUSTOMER", loCustomer);
				/*if (loCustomer.getCustomer_type().equals("A")) {
					foHttpSession.setAttribute("CUSTOMER_LOCATION", loCustomer.getDelivery_pins());
				} else {
					foHttpSession.setAttribute("CUSTOMER_LOCATION", loCustomer.getPin());
				}*/
				foHttpSession.setAttribute("CUSTOMER_LOCATION", loCustomer.getPin());
				MstUser loUser = new MstUser();
				loUser.getMstUserKey().setCompany_code(loCustomer.getMstCustomerKey().getCompany_code());
				loUser.getMstUserKey().setUser_code(loCustomer.getMstCustomerKey().getCustomer_code());
				loUser.setUser_type(loCustomer.getCustomer_type());
				loUser.setUser_name(loCustomer.getCustomer_name());
				loUser.setMobile(loCustomer.getMobile());
				loUser.setEmail(loCustomer.getEmail());
				foHttpSession.setAttribute("USEROBJ", loUser);

			} else {
				model.put("user_error", "Authentication failed!!!! Invalid Password.");
				return new ModelAndView("/Shop/Main_SignIn", model);
			}
			AppReferenceUtils.setAppReferenceDataOnLogin(foHttpSession, jdbcTemplate);

			MstProduct loProduct = new MstProduct();
			loProduct.getMstProductKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			
			TranOrder loTranOrderParam = new TranOrder();
			loTranOrderParam.getTranOrderKey().setCompany_code(loCustomer.getMstCustomerKey().getCompany_code());
			loTranOrderParam.setCustomer_code(loCustomer.getMstCustomerKey().getCustomer_code());
			loTranOrderParam.setStatus(AppConstants.STATUS_ORDER_NOT_SUBMITTED);
			TranOrder loCurrentOrder = TranOrderService.getOrder(loTranOrderParam);
			foHttpSession.setAttribute("CURRENT_ORDER",	loCurrentOrder);
			
			foHttpSession.setAttribute("CART_SUMMARY",
					TranCustCartService.getCartSummary(loCustomer.getMstCustomerKey()));
			return new ModelAndView("/app/jsp/Home", model);
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			return new ModelAndView("/app/jsp/Home");
		}

	}

	
	@RequestMapping(value = "/ShopOnlineSignout", method = RequestMethod.GET)
	public ModelAndView signOut(HttpSession foHttpSession) {
		AlertUtils.resetMessage(foHttpSession);

		String lsRedirect = "redirect:/?circle=" + GlobalValues.getCompanyCodeEnc(foHttpSession);
		foHttpSession.invalidate();
		return new ModelAndView(lsRedirect);
	}

	/*@RequestMapping(value = "/Home", method = RequestMethod.GET)
	public ModelAndView home(HttpSession foHttpSession) {

		AlertUtils.resetMessage(foHttpSession);
		String lsRedirect = "redirect:/?circle=" + GlobalValues.getCompanyCodeEnc(foHttpSession);

		return new ModelAndView(lsRedirect);
	}*/

	@RequestMapping(value = "/SessionHome", method = RequestMethod.GET)
	public ModelAndView sessionhome(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse) {
		AlertUtils.resetMessage(foHttpSession);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("root_site", "http://compsoft.in");
		return new ModelAndView("/Shop/Main_SessionTimeout", model);
	}

	/*@RequestMapping(value = "/ManageOrders", method = RequestMethod.GET)
	public ModelAndView manageOrder(@ModelAttribute("TranOrder") TranOrder loTranOrder, HttpSession foHttpSession,
			BindingResult foResult) {
		AlertUtils.resetMessage(foHttpSession);
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			MstCustomer loCustomer = (MstCustomer) foHttpSession.getAttribute("LOGGEDINCUSTOMER");
			loTranOrder.getTranOrderKey().setCompany_code(loCustomer.getMstCustomerKey().getCompany_code());
			loTranOrder.setStatus(AppConstants.STATUS_ORDER_SUBMITTED);
			// List<Map<String, Object>> loOrders =
			// TranOrderService.getOrders(loTranOrder);
			model.put("OrderList", TranOrderService.getOrders(loTranOrder));
			model.put("TranOrder", loTranOrder);
			return new ModelAndView("/Shop/Main_ManageOrder", model);
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			ex.printStackTrace();
			return new ModelAndView("/app/jsp/Home", null);
		}
	}*/

	public JSONObject ConvertSheetToJson(XSSFWorkbook foSheet, List<Map<String, Object>> foParams, String fsKey) {
		JSONObject loJObj = new JSONObject();
		int r = 1;
		int c = 0;

		try {

			if (foParams == null) {

				loJObj.put("status", "failed");
				loJObj.put("error", "Parameter not setup.");
				loJObj.put("data", null);
				return loJObj;

			}

			XSSFSheet sheet = foSheet.getSheetAt(0); // creating a Sheet object
														// to
			int liNumberofRows = sheet.getPhysicalNumberOfRows();

			if (liNumberofRows == 0) {

				loJObj.put("status", "failed");
				loJObj.put("error", "Sheet is empty");
				loJObj.put("data", null);
				return loJObj;
			}
			String lsAllErrors = "";
			String lsAllSuccess = "";
			JSONArray loJSONData = new JSONArray();
			for (r = 1; r < liNumberofRows; r++) {
				JSONObject loRow = new JSONObject();
				JSONObject loRowKey = new JSONObject();
				if (sheet.getRow(r).getCell(0) == null
						|| AppUtils.isValueEmpty(sheet.getRow(r).getCell(0).toString())) {
					break;
				}
				for (c = 0; c < foParams.size(); c++) {
					Cell loCell = sheet.getRow(r).getCell(c);
					if (loCell == null || AppUtils.isValueEmpty(loCell.toString())) {
						continue;
					}
					Map<String, Object> loParam = foParams.get(c);
					String lsValue = null;
					if (((String) loParam.get("DATA_TYPE")).equals("DECIMAL")) {
						lsValue = new BigDecimal(loCell.toString()).toString();
					} else if (((String) loParam.get("DATA_TYPE")).equals("NUMBER")) {
						lsValue = (new BigDecimal(loCell.toString())).longValue() + "";
					} else if (((String) loParam.get("DATA_TYPE")).equals("DATE")) {
						lsValue = AppUtils.getFormattedTimestamp(loCell.getDateCellValue());
					} else {
						lsValue = loCell.toString();
						if (loCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							lsValue = (new BigDecimal(loCell.toString())).longValue() + "";
						}
					}
					if (loParam.get("KEY") != null && ((String) loParam.get("KEY")).equals("Y")) {
						loRowKey.put((String) loParam.get("APP_ATTRIBUTE"), AppUtils.convertNullToEmpty(lsValue));
					} else {
						loRow.put((String) loParam.get("APP_ATTRIBUTE"), AppUtils.convertNullToEmpty(lsValue));
					}
				}
				loRow.put(fsKey, loRowKey);
				loJSONData.add(loRow);
			}
			loJObj.put("status", "success");
			loJObj.put("success", "Data successfully converted");
			loJObj.put("data", loJSONData);
			return loJObj;

		} catch (Exception ex) {
			loJObj.put("status", "failed");
			loJObj.put("error", "Error in converting data");
			loJObj.put("data", null);
			return loJObj;
		}

	}

	@RequestMapping(value = "/SubmitPin", method = RequestMethod.POST)
	public ModelAndView submitPin(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			@RequestParam String location) {
		AlertUtils.resetMessage(foHttpSession);
		Map<String, Object> model = new HashMap<String, Object>();
		
		if (AppUtils.isValueEmpty(location)
				|| location.length() != 6)
		{
			AlertUtils.addError("Invalid pin", foHttpSession);
			return new ModelAndView("/app/jsp/Home", model);
		}
		
		
		MstProduct loProduct = new MstProduct();
		loProduct.getMstProductKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
		
		foHttpSession.setAttribute("CUSTOMER_LOCATION", location);
		List<Map<String, Object>> loProducts = TranCustCartService.getProductForLocation(loProduct,
				(String)foHttpSession.getAttribute("CUSTOMER_LOCATION"), null);
		
		
		if (loProducts == null)
		{
			AlertUtils.addInfo("No products found.", foHttpSession);
			return new ModelAndView("/app/jsp/Home");
		}
		model.put("ProductList", loProducts);
		return new ModelAndView("/Shop/List_Product", model);
	}

	@RequestMapping(value = "/SignIn", method = RequestMethod.GET)
	public ModelAndView ShopSignInPage(HttpSession foHttpSession, HttpServletRequest foHttpRequest) {
		AlertUtils.resetMessage(foHttpSession);
		Map<String, Object> model = new HashMap<String, Object>();
		return new ModelAndView("/Shop/Main_SignIn", model);
	}
	
}
