package com.compsoft.shop.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.compsoft.shop.model.MstCustomer;
import com.compsoft.shop.model.MstProductKey;
import com.compsoft.shop.model.TranOrder;
import com.compsoft.shop.model.TranOrderDetail;
import com.compsoft.shop.model.TranOrderPayment;
import com.compsoft.shop.reference.ShopSequenceGenerator;
import com.compsoft.shop.service.ExternalAPIService;
import com.compsoft.shop.service.MstProductService;
import com.compsoft.shop.service.TranCustCartService;
import com.compsoft.shop.service.TranOrderService;
import com.framework.controller.SuperController;
import com.framework.security.Security;
import com.framework.service.CommonService;
import com.framework.utils.AlertUtils;
import com.framework.utils.AppConstants;
import com.framework.utils.AppUtils;
import com.framework.utils.GlobalValues;

/**
 * @author Vaishali Chawadkar
 *
 */

@Controller
public class TranCustCartController extends SuperController {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private TranCustCartService TranCustCartService;

	@Autowired
	private TranOrderService TranOrderService;

	@Autowired
	private MstProductService MstProductService;

	@Autowired
	private ExternalAPIService TranPaymentService;
	
	
	@Autowired
	private CommonService CommonService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@RequestMapping(value = "/AddToCart", method = RequestMethod.GET)
	public @ResponseBody JSONObject addToCart(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse, @RequestParam String requestData) {
		AlertUtils.resetMessage(foHttpSession);
		JSONObject loReturn = new JSONObject();
		try {

			JSONParser parser = new JSONParser();
			JSONObject requestJson = (JSONObject) parser.parse(requestData);
			String product_code = (String) requestJson.get("product_code");
			String vendor_code = (String) requestJson.get("vendor_code");
			BigDecimal order_qty = new BigDecimal((String) requestJson.get("order_qty"));
			BigDecimal selling_price = new BigDecimal((String) requestJson.get("selling_price"));

			Session loSession = startTransation(sessionFactory);
			MstCustomer loCustomer = (MstCustomer) foHttpSession.getAttribute("LOGGEDINCUSTOMER");
			if (loCustomer == null) {
				loReturn.put("status", "falied");
				loReturn.put("error", "Please sign in...");
				return loReturn;
			}

			if (loCustomer.getCustomer_type().equals("A")) {
				loReturn.put("status", "falied");
				loReturn.put("error", "Purchase for admin user is restricted.");
				return loReturn;
			}

			if (order_qty == null || order_qty.compareTo(BigDecimal.ZERO) < 0) {
				loReturn.put("status", "falied");
				loReturn.put("error", "Incorrect quantity.");
				return loReturn;
			}

			TranOrder loTranOrder = (TranOrder) foHttpSession.getAttribute("CURRENT_ORDER");
			TranOrder loTranOrderSave = new TranOrder();
			if (loTranOrder == null) {
				String lsOrderNo = ShopSequenceGenerator.getNextOrderId(GlobalValues.getCompanyCode(foHttpSession),
						jdbcTemplate);
				loTranOrderSave.getTranOrderKey().setOrder_no(lsOrderNo);
			} else {
				loTranOrderSave.getTranOrderKey().setOrder_no(loTranOrder.getTranOrderKey().getOrder_no());
			}

			loTranOrderSave.getTranOrderKey().setCompany_code(loCustomer.getMstCustomerKey().getCompany_code());
			loTranOrderSave.setCustomer_code(loCustomer.getMstCustomerKey().getCustomer_code());
			loTranOrderSave.setStatus("DR");

			List<TranOrderDetail> loOrderDetail = loTranOrderSave.getTranOrderDetail();
			TranOrderDetail loTranOrderDetail = new TranOrderDetail();

			loTranOrderDetail.getTranOrderDetailKey()
					.setCompany_code(loTranOrderSave.getTranOrderKey().getCompany_code());
			loTranOrderDetail.getTranOrderDetailKey().setOrder_no(loTranOrderSave.getTranOrderKey().getOrder_no());
			loTranOrderDetail.getTranOrderDetailKey().setProduct_code(product_code);
			loTranOrderDetail.getTranOrderDetailKey().setVendor_code(vendor_code);
			loTranOrderDetail.setOrder_qty(order_qty);
			loTranOrderDetail.getTranOrderDetailKey().setSelling_price(selling_price);
			loOrderDetail.add(loTranOrderDetail);

			AlertUtils.validateModel(loTranOrderSave, foHttpSession);
			TranOrderService.addOrder(loSession, loTranOrderSave);

			String lsError = AlertUtils.getErrors(foHttpSession);
			if (AppUtils.isValueEmpty(lsError)) {
				if (commitNoMsg(loSession.getTransaction(), loSession, foHttpSession)) {

					Map<String, Object> loSummary = TranCustCartService.getCartSummary(loCustomer.getMstCustomerKey());

					if (loSummary != null) {
						loReturn.putAll(loSummary);
					}
					loReturn.put("status", "success");
					foHttpSession.setAttribute("CART_SUMMARY", loSummary);
					foHttpSession.setAttribute("CURRENT_ORDER", loTranOrderSave);
				} else {
					loReturn.put("status", "falied");
					loReturn.put("error", "Error in adding item in cart. Please contact system administrator.");
				}
			} else {
				loReturn.put("status", "falied");
				loReturn.put("error", lsError);
			}

			return loReturn;
		} catch (Exception ex) {
			ex.printStackTrace();
			loReturn.put("status", "falied");
			loReturn.put("error", "Error in adding item in cart. Please contact system administrator.");
			return loReturn;
		}
	}

	@RequestMapping(value = "/ViewCart", method = RequestMethod.GET)
	public ModelAndView viewCart(HttpSession foHttpSession, HttpServletRequest foHttpRequest) {
		// AlertUtils.resetMessage(foHttpSession);
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			MstCustomer loCustomer = (MstCustomer) foHttpSession.getAttribute("LOGGEDINCUSTOMER");

			TranOrder loDraftOrderParam = new TranOrder();
			loDraftOrderParam.getTranOrderKey().setCompany_code(loCustomer.getMstCustomerKey().getCompany_code());
			loDraftOrderParam.setCustomer_code(loCustomer.getMstCustomerKey().getCustomer_code());
			loDraftOrderParam.setStatus(AppConstants.STATUS_ORDER_NOT_SUBMITTED);
			TranOrder loDraftOrder = TranOrderService.getOrder(loDraftOrderParam);

			if (loDraftOrder == null) {
				AlertUtils.addInfo("Cart is Empty. Please continue shopping!!!", foHttpSession);
				return new ModelAndView("/app/jsp/Home", model);
			}

			populateOrderDetails(loCustomer, loDraftOrder);

			//loDraftOrder.setScheduled_delivery_date(AppUtils.getFormattedTimestamp(AppUtils.getCurrentDateTime()));
			//loDraftOrder.setScheduled_delivery_date(AppUtils.getFormattedTimestamp(AppUtils.getCurrentDateTime()));
			
			//loDraftOrder.setScheduled_delivery_time("7pm-10pm");

			model.put("TranOrder", loDraftOrder);
			return new ModelAndView("/Shop/Main_Cart", model);
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			return new ModelAndView("/app/jsp/Home");
		}

	}

	/*
	 * @RequestMapping(value = "/DeleteCart", method = RequestMethod.POST)
	 * public ModelAndView deleteCart(HttpSession foHttpSession) {
	 * AlertUtils.resetMessage(foHttpSession); try {
	 * 
	 * Session loSession = startTransation(sessionFactory);
	 * 
	 * MstCustomer loCustomer = (MstCustomer)
	 * foHttpSession.getAttribute("LOGGEDINCUSTOMER");
	 * 
	 * TranCustCartKey loTranCustCartKey = new TranCustCartKey();
	 * loTranCustCartKey.setCompany_code(loCustomer.getMstCustomerKey().
	 * getCompany_code());
	 * loTranCustCartKey.setCustomer_code(loCustomer.getMstCustomerKey().
	 * getCustomer_code()); TranCustCart loTranCustCart =
	 * TranCustCartService.getCart(loTranCustCartKey, null);
	 * 
	 * AlertUtils.validateModel(loTranCustCart, foHttpSession);
	 * TranCustCartService.deleteCart(loSession, loTranCustCart);
	 * 
	 * String lsError = AlertUtils.getErrors(foHttpSession); if
	 * (AppUtils.isValueEmpty(lsError)) { if
	 * (commitNoMsg(loSession.getTransaction(), loSession, foHttpSession)) {
	 * AlertUtils.addInfo("Cart is empty. Please continue shopping...",
	 * foHttpSession); foHttpSession.setAttribute("CART_SUMMARY", null); return
	 * new ModelAndView("redirect:/Home"); } } else { return new
	 * ModelAndView("redirect:/ViewCart"); } return new
	 * ModelAndView("redirect:/ViewCart"); } catch (Exception ex) {
	 * logException(ex, foHttpSession); return new ModelAndView("/app/jsp/Home",
	 * null);
	 * 
	 * } }
	 */

	@RequestMapping(value = "/SubmitCart", method = RequestMethod.POST)
	public @ResponseBody JSONObject submitCart(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse, @RequestParam String requestData) {
		AlertUtils.resetMessage(foHttpSession);
		JSONObject loReturn = new JSONObject();
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			MstCustomer loCustomer = (MstCustomer) foHttpSession.getAttribute("LOGGEDINCUSTOMER");

			TranOrder loDraftOrderParam = new TranOrder();
			loDraftOrderParam.getTranOrderKey().setCompany_code(loCustomer.getMstCustomerKey().getCompany_code());
			loDraftOrderParam.setCustomer_code(loCustomer.getMstCustomerKey().getCustomer_code());
			loDraftOrderParam.setStatus(AppConstants.STATUS_ORDER_NOT_SUBMITTED);
			TranOrder loDraftOrder = TranOrderService.getOrder(loDraftOrderParam);

			if (loDraftOrder == null) {
				loReturn.put("status", "failed");
				loReturn.put("errors", "No order in cart to submit.");
				return loReturn;
			}
			populateOrderDetails(loCustomer, loDraftOrder);

			JSONParser parser = new JSONParser();
			JSONObject requestJson = (JSONObject) parser.parse(requestData);
			String address = (String) requestJson.get("address");
			String payment_mode = (String) requestJson.get("payment_mode");
			String pin = (String) requestJson.get("pin");
			//String scheduled_delivery_time = (String) requestJson.get("scheduled_delivery_time");
			//String scheduled_delivery_date = (String) requestJson.get("scheduled_delivery_date");

			if (AppUtils.isValueEmpty(address)) {
				loReturn.put("status", "failed");
				loReturn.put("errors", "Address is required.");
				return loReturn;
			}
			if (AppUtils.isValueEmpty(payment_mode)) {
				loReturn.put("status", "failed");
				loReturn.put("errors", "Payment mode is required.");
				return loReturn;
			}
			if (AppUtils.isValueEmpty(pin)) {
				loReturn.put("status", "failed");
				loReturn.put("errors", "Pin is required.");
				return loReturn;
			}

			loDraftOrder.setDelivery_address(address);
			loDraftOrder.setPin(pin);
			//loDraftOrder.setScheduled_delivery_date(AppUtils.getFormattedTimestamp(scheduled_delivery_date));
			//loDraftOrder.setScheduled_delivery_time(scheduled_delivery_time);

			loDraftOrder.setStatus(AppConstants.STATUS_ORDER_SUBMITTED);
			loDraftOrder.setOrder_date(AppUtils.getCurrentDate());
			loDraftOrder.getTranOrderPayment().get(0).setPayment_mode(payment_mode);
			
			AppUtils.validateModel(loDraftOrder);
			String lsError = AlertUtils.getErrors(foHttpSession);
			Session loSession = startTransation(sessionFactory);
			if (AppUtils.isValueEmpty(lsError)) {
				
				// Creating Payments
				if (payment_mode.equals(AppConstants.PAYMENT_MODE_ONLINE)) {
					
					JSONObject loRZRPaymentOut = TranPaymentService.createPayment(loDraftOrder);
					if (loRZRPaymentOut == null) {
						loReturn.put("status", "failed");
						loReturn.put("errors",
								"Online payment service is down. Please try later.");
						return loReturn;
					} else {
						loDraftOrder.getTranOrderPayment().get(0).setRzr_order_id((String) loRZRPaymentOut.get("id"));
						loDraftOrder.getTranOrderPayment().get(0).setRzr_order_status((String) loRZRPaymentOut.get("status"));
					}
				}

				TranOrderService.addOrder(loSession, loDraftOrder);

				if (commitNoMsg(loSession.getTransaction(), loSession, foHttpSession)) {
					loReturn.put("status", "success");
					loReturn.put("order_no", loDraftOrder.getTranOrderKey().getOrder_no());
					loReturn.put("order_no_enc", Security.encrypt(loDraftOrder.getTranOrderKey().getOrder_no()));
					loReturn.put("rzr_order_id", loDraftOrder.getTranOrderPayment().get(0).getRzr_order_id());
					foHttpSession.setAttribute("CART_SUMMARY", null);
					foHttpSession.setAttribute("CURRENT_ORDER", null);

				} else {
					loReturn.put("status", "failed");
					loReturn.put("errors", "Unable to submit order. Please contact system administrator.");
				}
			} else {
				loReturn.put("status", "failed");
				loReturn.put("errors", (ArrayList<String>) foHttpSession.getAttribute("ERRORS"));
				loReturn.put("errors", lsError);
			}
			return loReturn;
		} catch (Exception ex) {
			ex.printStackTrace();
			loReturn.put("status", "failed");
			loReturn.put("errors", "Error in submitting cart. Please contact system administrator." + ex.getMessage());
			return loReturn;
		}

	}

	private void populateOrderDetails(MstCustomer foCustomer, TranOrder foTranOrder) {
		foTranOrder.setCustomer_name(foCustomer.getCustomer_name());
		foTranOrder.setDelivery_address(foCustomer.getAddress());
		foTranOrder.setPin(foCustomer.getPin());
		foTranOrder.setMobile(foCustomer.getMobile());
		foTranOrder.setEmail(foCustomer.getEmail());

		MstProductKey loProductKey = new MstProductKey();
		loProductKey.setCompany_code(foCustomer.getMstCustomerKey().getCompany_code());
		//loProductKey.setStart_date(AppUtils.getCurrentDate());
		JSONObject loProduct = null;

		List<TranOrderDetail> loOrderDetail = foTranOrder.getTranOrderDetail();
		if (loOrderDetail != null) {
			BigDecimal lbdTotSellingPrice = BigDecimal.ZERO;
			BigDecimal lbdTotProducts = BigDecimal.ZERO;
			BigDecimal lbdTotQty = BigDecimal.ZERO;
			BigDecimal lbdTotMrp = BigDecimal.ZERO;
			BigDecimal lbdTotLandingCost = BigDecimal.ZERO;
			BigDecimal lbdTotDiscount = BigDecimal.ZERO;
			BigDecimal lbdTotBv = BigDecimal.ZERO;
			for (TranOrderDetail loOrderDetailRow : loOrderDetail) {
				loProductKey.setProduct_code(loOrderDetailRow.getTranOrderDetailKey().getProduct_code());
				loProductKey.setVendor_code(loOrderDetailRow.getTranOrderDetailKey().getVendor_code());
				loProductKey.setSelling_price( loOrderDetailRow.getTranOrderDetailKey().getSelling_price());
				loProduct = MstProductService.getProduct(loProductKey, null);
				
				if (loProduct != null) {
					loOrderDetailRow.setAlt_product_code((String) loProduct.get("ALT_PRODUCT_CODE"));
					loOrderDetailRow.setAlt_product_name((String) loProduct.get("ALT_PRODUCT_NAME"));
					loOrderDetailRow.setCess_percentage((BigDecimal) loProduct.get("CESS_PERCENTAGE"));
					loOrderDetailRow.setGst_percentage((BigDecimal) loProduct.get("GST_PERCENTAGE"));
					loOrderDetailRow.setHsn_code((String) loProduct.get("HSN_CODE"));
					loOrderDetailRow.setMrp((BigDecimal) loProduct.get("MRP"));
					loOrderDetailRow.setProduct_name((String) loProduct.get("PRODUCT_NAME"));
					loOrderDetailRow.setAdd_information((String) loProduct.get("ADD_INFORMATION"));
					loOrderDetailRow.setLanding_cost((BigDecimal) loProduct.get("ADD_INFORMATION"));
					loOrderDetailRow.setDisc_per((BigDecimal) loProduct.get("DISC_PER"));
					loOrderDetailRow.setBv_per((BigDecimal) loProduct.get("BV_PER"));
					loOrderDetailRow.setDiscount((BigDecimal) loProduct.get("DISCOUNT"));
					loOrderDetailRow.setBv((BigDecimal) loProduct.get("BV"));
					loOrderDetailRow.setImage_name((String) loProduct.get("IMAGE_NAME"));
				} else {
					loOrderDetailRow.setProduct_name("Product Not Available.");
					loOrderDetailRow.setOrder_qty(BigDecimal.ZERO);
				}

				lbdTotSellingPrice = lbdTotSellingPrice.add(((BigDecimal) loProduct.get("SELLING_PRICE"))
						.multiply((BigDecimal) loOrderDetailRow.getOrder_qty()));
				lbdTotProducts = lbdTotProducts.add(new BigDecimal("1"));
				lbdTotQty = lbdTotQty.add((BigDecimal) loOrderDetailRow.getOrder_qty());
				lbdTotMrp = lbdTotMrp.add(
						((BigDecimal) loProduct.get("MRP")).multiply((BigDecimal) loOrderDetailRow.getOrder_qty()));
				lbdTotLandingCost = lbdTotLandingCost.add(((BigDecimal) loProduct.get("LANDING_COST"))
						.multiply((BigDecimal) loOrderDetailRow.getOrder_qty()));
				lbdTotDiscount = lbdTotDiscount.add(((BigDecimal) loProduct.get("DISCOUNT"))
						.multiply((BigDecimal) loOrderDetailRow.getOrder_qty()));
				lbdTotBv = lbdTotDiscount
						.add(((BigDecimal) loProduct.get("BV")).multiply((BigDecimal) loOrderDetailRow.getOrder_qty()));
			}
			foTranOrder.setTot_selling_price(lbdTotSellingPrice);
			foTranOrder.setTot_products(lbdTotProducts);
			foTranOrder.setTot_qty(lbdTotQty);
			foTranOrder.setTot_mrp(lbdTotMrp);
			foTranOrder.setTot_landing_cost(lbdTotLandingCost);
			foTranOrder.setTot_disc(lbdTotDiscount);
			foTranOrder.setTot_bv(lbdTotBv);
			foTranOrder.setTot_payment_amt(foTranOrder.getTot_selling_price());
		}

		List<TranOrderPayment> loTranOrderPaymentList = foTranOrder.getTranOrderPayment();
		if (loTranOrderPaymentList.size() == 0) {
			TranOrderPayment loTranOrderPaymentRow = new TranOrderPayment();
			loTranOrderPaymentList.add(loTranOrderPaymentRow);
		}
		loTranOrderPaymentList.get(0).getTranOrderPaymentKey()
				.setCompany_code(foTranOrder.getTranOrderKey().getCompany_code());
		loTranOrderPaymentList.get(0).getTranOrderPaymentKey().setOrder_no(foTranOrder.getTranOrderKey().getOrder_no());
		loTranOrderPaymentList.get(0).getTranOrderPaymentKey()
				.setPayment_id(foTranOrder.getTranOrderKey().getOrder_no());
		loTranOrderPaymentList.get(0).setCustomer_code(foCustomer.getMstCustomerKey().getCustomer_code());
		loTranOrderPaymentList.get(0).setPayment_amt(foTranOrder.getTot_payment_amt());
		loTranOrderPaymentList.get(0).setPayment_date(AppUtils.getCurrentDate());
		loTranOrderPaymentList.get(0).setRemark("Online Order - Cash on delivery");
		loTranOrderPaymentList.get(0).setStatus(AppConstants.STATUS_PAYMENT_PENDING);
		loTranOrderPaymentList.get(0).setPayment_mode(AppConstants.PAYMENT_MODE_CASH);

	}

}
