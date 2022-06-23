package com.compsoft.shop.controller;

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
import com.compsoft.shop.model.TranOrder;
import com.compsoft.shop.service.TranCustCartService;
import com.compsoft.shop.service.TranOrderService;
import com.framework.controller.SuperController;
import com.framework.reference.ReferenceUtils;
import com.framework.security.Security;
import com.framework.utils.AlertUtils;
import com.framework.utils.AppConstants;
import com.framework.utils.AppUtils;
import com.framework.utils.FrameworkConstants;
import com.framework.utils.GlobalValues;
import com.framework.utils.Signature;

/**
 * @author Vaishali Chawadkar
 *
 */

@Controller
public class TranOrderController extends SuperController {

	@Autowired
	private TranOrderService TranOrderService;
	@Autowired
	private TranCustCartService TranCustCartService;

	@Autowired
	private SessionFactory sessionFactory;

	@RequestMapping(value = "/SearchOrder", method = RequestMethod.POST)
	public ModelAndView orders(@ModelAttribute("TranOrder") TranOrder loTranOrder, BindingResult foResult,
			HttpSession foHttpSession, HttpServletRequest foHttpRequest) {
		AlertUtils.resetMessage(foHttpSession);
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			MstCustomer loCustomer = (MstCustomer) foHttpSession.getAttribute("LOGGEDINCUSTOMER");

			loTranOrder.getTranOrderKey().setCompany_code(loCustomer.getMstCustomerKey().getCompany_code());
			loTranOrder.setCustomer_code(loCustomer.getMstCustomerKey().getCustomer_code());

			loTranOrder.setOrder_date(null);
			loTranOrder.setOrder_to_date(null);

			// loTranOrder.setStatus(AppConstants.GROUP_ORDER_STATUS);

			List<Map<String, Object>> loOrders = TranOrderService.getOrders(loTranOrder);
			TranOrder loTranOrderNew = TranOrderService.getOrder(loTranOrder);
			model.put("OrderList", loOrders);
			model.put("Order", loTranOrderNew);
			return new ModelAndView("/Shop/Main_Orders", model);
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			return new ModelAndView("/app/jsp/Home");
		}
	}

	@RequestMapping(value = "/OrderList", method = RequestMethod.GET)
	public ModelAndView OrderList(@ModelAttribute("TranOrder") TranOrder loTranOrder, HttpSession foHttpSession,
			BindingResult foResult) {
		AlertUtils.resetMessage(foHttpSession);
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			MstCustomer loCustomer = (MstCustomer) foHttpSession.getAttribute("LOGGEDINCUSTOMER");
			loTranOrder.getTranOrderKey().setCompany_code(loCustomer.getMstCustomerKey().getCompany_code());
			loTranOrder.setCustomer_code(loCustomer.getMstCustomerKey().getCustomer_code());
			loTranOrder.setStatus(AppConstants.STATUS_ORDER_SUBMITTED);
			loTranOrder.setOrder_date(null);
			loTranOrder.setOrder_to_date(null);
			List<Map<String, Object>> loOrders = TranOrderService.getOrders(loTranOrder);
			model.put("OrderList", loOrders);
			model.put("TranOrder", loTranOrder);
			return new ModelAndView("/Shop/Main_Orders", model);
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			ex.printStackTrace();
			return new ModelAndView("/app/jsp/Home");
		}
	}

	/*
	 * @RequestMapping(value = "/ModifyOrder", method = RequestMethod.GET)
	 * public ModelAndView modifyOrder(HttpSession foHttpSession,
	 * HttpServletRequest foHttpRequest,
	 * 
	 * @RequestParam String order_no) { AlertUtils.resetMessage(foHttpSession);
	 * try {
	 * 
	 * MstCustomer loCustomer = (MstCustomer)
	 * foHttpSession.getAttribute("LOGGEDINCUSTOMER"); TranCustCartKey
	 * loTranCustCartKey = new TranCustCartKey();
	 * loTranCustCartKey.setCompany_code(loCustomer.getMstCustomerKey().
	 * getCompany_code());
	 * loTranCustCartKey.setCustomer_code(loCustomer.getMstCustomerKey().
	 * getCustomer_code());
	 * 
	 * TranCustCart loTranCustCart =
	 * TranCustCartService.getCart(loTranCustCartKey, null); if (loTranCustCart
	 * != null) { AlertUtils.addError(
	 * "You already have items added in cart. Please empty your cart before modifying this order."
	 * , foHttpSession); String lsRedirect = "redirect:/ViewCart"; return new
	 * ModelAndView(lsRedirect, null); }
	 * 
	 * Session loSession = startTransation(sessionFactory);
	 * 
	 * loTranCustCart = new TranCustCart();
	 * loTranCustCart.getTranCustCartKey().setCompany_code(loCustomer.
	 * getMstCustomerKey().getCompany_code());
	 * loTranCustCart.getTranCustCartKey().setCustomer_code(loCustomer.
	 * getMstCustomerKey().getCustomer_code());
	 * 
	 * TranOrder loTranOrder = new TranOrder();
	 * loTranOrder.getTranOrderKey().setCompany_code(loCustomer.
	 * getMstCustomerKey().getCompany_code());
	 * loTranOrder.getTranOrderKey().setOrder_no(order_no); TranOrder
	 * loTranOrderData = TranOrderService.getOrder(loTranOrder);
	 * 
	 * List<TranOrderDetail> loOrderDetails =
	 * loTranOrderData.getTranOrderDetail();
	 * 
	 * List<TranCustCartDetail> loCustCartDetails =
	 * loTranCustCart.getTranCustCartDetail();
	 * 
	 * if (loOrderDetails != null) { int liRow = 0; for (TranOrderDetail
	 * loOrderDetail : loOrderDetails) {
	 * 
	 * TranCustCartDetail loCustCartDetail = new TranCustCartDetail();
	 * loCustCartDetail.getTranCustCartDetailKey()
	 * .setCompany_code(loCustomer.getMstCustomerKey().getCompany_code());
	 * loCustCartDetail.getTranCustCartDetailKey()
	 * .setCustomer_code(loCustomer.getMstCustomerKey().getCustomer_code());
	 * loCustCartDetail.getTranCustCartDetailKey().setProduct_code(loOrderDetail
	 * .getProduct_code());
	 * loCustCartDetail.getTranCustCartDetailKey().setVendor_code(loOrderDetail.
	 * getVendor_code());
	 * loCustCartDetail.setOrder_qty(loOrderDetail.getOrder_qty());
	 * loCustCartDetails.add(loCustCartDetail);
	 * 
	 * } }
	 * 
	 * AlertUtils.validateModel(loTranCustCart, foHttpSession);
	 * TranCustCartService.addCart(loSession, loTranCustCart);
	 * TranOrderService.deleteOrder(loSession, loTranOrderData);
	 * 
	 * String lsError = AlertUtils.getErrors(foHttpSession);
	 * foHttpSession.setAttribute("MODIFIED_ORDER", loTranOrderData);
	 * 
	 * if (AppUtils.isValueEmpty(lsError)) { if
	 * (commitNoMsg(loSession.getTransaction(), loSession, foHttpSession)) {
	 * Map<String, Object> loSummary =
	 * TranCustCartService.getCartSummary(loCustomer.getMstCustomerKey()); if
	 * (loSummary != null) { foHttpSession.setAttribute("CART_SUMMARY",
	 * loSummary); }
	 * 
	 * return new ModelAndView("redirect:/ViewCart"); } else { return new
	 * ModelAndView("redirect:/OrderPage"); } } return new
	 * ModelAndView("redirect:/OrderPage"); } catch (Exception ex) {
	 * logException(ex, foHttpSession); ex.printStackTrace(); return new
	 * ModelAndView("/app/jsp/Home", null); } }
	 */

	@RequestMapping(value = "/UpdateOrderStatus", method = RequestMethod.GET)
	public @ResponseBody JSONObject updateOrderStatus(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse, @RequestParam String requestData) {
		AlertUtils.resetMessage(foHttpSession);
		JSONObject loReturn = new JSONObject();
		try {
			MstCustomer loCustomer = (MstCustomer) foHttpSession.getAttribute("LOGGEDINCUSTOMER");

			JSONParser parser = new JSONParser();
			JSONObject requestJson = (JSONObject) parser.parse(requestData);
			String order_no = (String) requestJson.get("order_no");
			String status = (String) requestJson.get("status");
			String lsCustomerCode = (String) requestJson.get("customer_code");
			if (AppUtils.isValueEmpty(lsCustomerCode))
			{
				lsCustomerCode = loCustomer.getMstCustomerKey().getCustomer_code();
			}

			Session loSession = startTransation(sessionFactory);
			
			TranOrder loTranOrderParam = new TranOrder();
			loTranOrderParam.getTranOrderKey().setCompany_code(loCustomer.getMstCustomerKey().getCompany_code());
			loTranOrderParam.setCustomer_code(lsCustomerCode);
			loTranOrderParam.getTranOrderKey().setOrder_no(order_no);

			TranOrder loTranOrder = TranOrderService.getOrder(loTranOrderParam);
			loTranOrder.setStatus(status);

			AppUtils.validateModel(loTranOrder);
			String lsError = AlertUtils.getErrors(foHttpSession);

			if (AppUtils.isValueEmpty(lsError)) {
				TranOrderService.addOrder(loSession, loTranOrder);
				if (commitNoMsg(loSession.getTransaction(), loSession, foHttpSession)) {
					loReturn.put("status", "success");
					loReturn.put("message", "Order status update successfully.");
					loReturn.put("order_status", ReferenceUtils.getOptionValue(foHttpSession,
							AppConstants.GROUP_ORDER_STATUS, loTranOrder.getStatus()));
				} else {
					loReturn.put("status", "falied");
					loReturn.put("error", "Unable to update status of an order. Please contact system administrator.");
				}
			} else {
				loReturn.put("status", "falied");
				loReturn.put("error", lsError);
			}
			return loReturn;
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			loReturn.put("status", "falied");
			loReturn.put("error", "Error in adding item in cart. Please contact system administrator.");
			return loReturn;
		}
	}

	@RequestMapping(value = "/ManageOrders", method = RequestMethod.GET)
	public ModelAndView manageOrders(HttpSession foHttpSession, HttpServletRequest foHttpRequest) {
		AlertUtils.resetMessage(foHttpSession);
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			TranOrder loTranOrder = new TranOrder();
			loTranOrder.getTranOrderKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			loTranOrder.setOrder_date(AppUtils.getCurrentDate());
			loTranOrder.setOrder_to_date(AppUtils.getCurrentDate());
			loTranOrder.setStatus(AppConstants.STATUS_ORDER_SUBMITTED);
			List<Map<String, Object>> loOrders = TranOrderService.getOrders(loTranOrder);
			model.put("OrderList", loOrders);
			model.put("TranOrder", loTranOrder);
			return new ModelAndView("/Shop/Main_ManageOrder", model);
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			ex.printStackTrace();
			return new ModelAndView("/app/jsp/Home");
		}
	}
	
	@RequestMapping(value = "/SearchOrders", method = RequestMethod.POST)
	public ModelAndView searchOrders(@ModelAttribute("TranOrder") TranOrder loTranOrder, BindingResult foResult,
			HttpSession foHttpSession, HttpServletRequest foHttpRequest) {
		AlertUtils.resetMessage(foHttpSession);
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			loTranOrder.getTranOrderKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			loTranOrder.setOrder_date(AppUtils.getFormattedTimestamp(foHttpRequest.getParameter("order_date")));
			loTranOrder.setOrder_to_date(AppUtils.getFormattedTimestamp(foHttpRequest.getParameter("order_to_date")));
			List<Map<String, Object>> loOrders = TranOrderService.getOrders(loTranOrder);
			model.put("OrderList", loOrders);
			model.put("TranOrder", loTranOrder);
			return new ModelAndView("/Shop/Main_ManageOrder", model);
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			ex.printStackTrace();
			return new ModelAndView("/app/jsp/Home");
		}
	}

	@RequestMapping(value = "/Checkout", method = RequestMethod.GET)
	public ModelAndView checkout(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			@RequestParam String order) {
		AlertUtils.resetMessage(foHttpSession);
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			MstCustomer loCustomer = (MstCustomer) foHttpSession.getAttribute("LOGGEDINCUSTOMER");
			TranOrder loTranOrder = new TranOrder();
			loTranOrder.getTranOrderKey().setCompany_code(loCustomer.getMstCustomerKey().getCompany_code());
			loTranOrder.setCustomer_code(loCustomer.getMstCustomerKey().getCustomer_code());
			loTranOrder.getTranOrderKey().setOrder_no(order);
			TranOrder loTranOrderData = TranOrderService.getOrder(loTranOrder);
			model.put("order", loTranOrderData);
			return new ModelAndView("/Shop/Main_OrderDetails", model);
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			ex.printStackTrace();
			return new ModelAndView("/app/jsp/Home");
		}
	}

	@RequestMapping(value = "/OrderDetail", method = RequestMethod.GET)
	public ModelAndView orderDetail(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			@RequestParam String were23wer) {
		AlertUtils.resetMessage(foHttpSession);
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			MstCustomer loCustomer = (MstCustomer) foHttpSession.getAttribute("LOGGEDINCUSTOMER");
			TranOrder loTranOrder = new TranOrder();
			loTranOrder.getTranOrderKey().setCompany_code(loCustomer.getMstCustomerKey().getCompany_code());
			//loTranOrder.setCustomer_code(loCustomer.getMstCustomerKey().getCustomer_code());
			loTranOrder.getTranOrderKey().setOrder_no(Security.decrypt(were23wer));
			TranOrder loTranOrderData = TranOrderService.getOrder(loTranOrder);
			model.put("order", loTranOrderData);
			return new ModelAndView("/Shop/Main_OrderDetails", model);
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			ex.printStackTrace();
			return new ModelAndView("/app/jsp/Home");
		}
	}

	@RequestMapping(value = "/CheckoutCallBack", method = RequestMethod.POST)
	public ModelAndView checkoutCallback(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			@RequestParam String razorpay_payment_id, @RequestParam String razorpay_order_id,
			@RequestParam String razorpay_signature, @RequestParam String order_id) {
		AlertUtils.resetMessage(foHttpSession);
		try {
			String lsPaymentInfo = FrameworkConstants.EMPTY;
			Session loSession = startTransation(sessionFactory);
			Map<String, Object> model = new HashMap<String, Object>();
			MstCustomer loCustomer = (MstCustomer) foHttpSession.getAttribute("LOGGEDINCUSTOMER");

			TranOrder loTranOrderParam = new TranOrder();
			loTranOrderParam.getTranOrderKey().setCompany_code(loCustomer.getMstCustomerKey().getCompany_code());
			loTranOrderParam.setCustomer_code(loCustomer.getMstCustomerKey().getCustomer_code());
			loTranOrderParam.getTranOrderKey().setOrder_no(order_id);
			TranOrder loPaymentOrder = TranOrderService.getOrder(loTranOrderParam);

			loPaymentOrder.getTranOrderPayment().get(0).setStatus(AppConstants.STATUS_PAYMENT_PAID);
			loPaymentOrder.getTranOrderPayment().get(0).setPayment_date(AppUtils.getCurrentDate());
			loPaymentOrder.getTranOrderPayment().get(0).setRzr_payment_id(razorpay_payment_id);
			loPaymentOrder.getTranOrderPayment().get(0).setRzr_sign(razorpay_signature);

			AppUtils.validateModel(loPaymentOrder);
			String lsError = AlertUtils.getErrors(foHttpSession);
			if (AppUtils.isValueEmpty(lsError)) {
				TranOrderService.addOrder(loSession, loPaymentOrder);
				if (!commitNoMsg(loSession.getTransaction(), loSession, foHttpSession)) {
					lsPaymentInfo = lsPaymentInfo + "Error in processing payments. Please call customer service. ";
					model.put("payment_info", lsPaymentInfo);
					model.put("order", loPaymentOrder);
					return new ModelAndView("/Shop/Main_OrderDetails", model);
				}
			}
			lsPaymentInfo = lsPaymentInfo + "Order processed successfully. Your order reference number is " + order_id
					+ ". ";
			String lsFormedSign = Signature.calculateRFC2104HMAC(razorpay_order_id + "|" + razorpay_payment_id,
					ReferenceUtils.getCnfigParamValue(foHttpSession, "PAYMENT_RZP_KEY"));
			if (razorpay_signature != null && razorpay_signature.equals(lsFormedSign)) {
				lsPaymentInfo = lsPaymentInfo + "Payment verified securely successfully. ";
			} else {
				lsPaymentInfo = lsPaymentInfo + "Payment processed and verification is in process. ";
			}

			model.put("payment_info", lsPaymentInfo);
			model.put("order", loPaymentOrder);
			return new ModelAndView("/Shop/Main_OrderDetails", model);
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			ex.printStackTrace();
			return new ModelAndView("/app/jsp/Home");
		}
	}
	
	@RequestMapping(value = "/OrderDetailBill", method = RequestMethod.POST)
	public ModelAndView orderDetailBill(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			@RequestParam String order_no) {
		AlertUtils.resetMessage(foHttpSession);
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			TranOrder loTranOrder = new TranOrder();
			loTranOrder.getTranOrderKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			loTranOrder.getTranOrderKey().setOrder_no(order_no);
			TranOrder loTranOrderData = TranOrderService.getOrder(loTranOrder);
			model.put("order", loTranOrderData);
			return new ModelAndView("/Shop/Main_OrderDetails", model);
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			ex.printStackTrace();
			return new ModelAndView("/app/jsp/Home");
		}
	}

}
