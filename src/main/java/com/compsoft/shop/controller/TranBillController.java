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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.compsoft.shop.model.TranBill;
import com.compsoft.shop.model.TranBillDetail;
import com.compsoft.shop.model.TranBillKey;
import com.compsoft.shop.model.TranBillPayment;
import com.compsoft.shop.model.TranOrder;
import com.compsoft.shop.model.TranOrderDetail;
import com.compsoft.shop.model.TranOrderPayment;
import com.compsoft.shop.reference.AppReferenceUtils;
import com.compsoft.shop.reference.ShopSequenceGenerator;
import com.compsoft.shop.service.TranBillService;
import com.compsoft.shop.service.TranOrderService;
import com.framework.controller.SuperController;
import com.framework.reference.LOVUtils;
import com.framework.reference.ReferenceUtils;
import com.framework.security.Security;
import com.framework.utils.AlertUtils;
import com.framework.utils.AppConstants;
import com.framework.utils.AppUtils;
import com.framework.utils.FrameworkConstants;
import com.framework.utils.GlobalValues;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Vaishali Chawadkar
 *
 */

@Controller
public class TranBillController extends SuperController {

	@Autowired
	private TranOrderService TranOrderService;
	@Autowired
	private TranBillService TranBillService;

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@RequestMapping(value = "/GenerateBill", method = RequestMethod.POST)
	public @ResponseBody JSONObject generateBill(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			@RequestParam String requestData) {
		AlertUtils.resetMessage(foHttpSession);
		JSONObject loReturn = new JSONObject();
		try {

			Map<String, Object> model = new HashMap<String, Object>();

			JSONParser parser = new JSONParser();
			JSONObject requestJson = (JSONObject) parser.parse(requestData);
			String lsorderNo = (String) requestJson.get("order_no");

			TranOrder loTranOrderParam = new TranOrder();
			loTranOrderParam.getTranOrderKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			loTranOrderParam.getTranOrderKey().setOrder_no(lsorderNo);
			TranOrder loOrder = TranOrderService.getOrder(loTranOrderParam);
			// JSONObject loOrder = (JSONObject) parser.parse(moGson.toJson());

			TranBill loBill = new TranBill();
			orderToBill(loOrder, loBill);

			Session loSession = startTransation(sessionFactory);
			AlertUtils.validateModel(loBill, foHttpSession);
			TranBillService.addBill(loSession, loBill);
			TranOrderService.addOrder(loSession, loOrder);

			String lsError = AlertUtils.getErrors(foHttpSession);
			if (AppUtils.isValueEmpty(lsError)) {
				if (commitNoMsg(loSession.getTransaction(), loSession, foHttpSession)) {
					loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
					loReturn.put("bill_no", loOrder.getBill_no());
					loReturn.put("order_status", ReferenceUtils.getOptionValue(foHttpSession,
							AppConstants.GROUP_ORDER_STATUS, loOrder.getStatus()));
				} else {
					loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
					loReturn.put("response_error", "Error in generating bill. Error-Save failed.");
				}
			} else {
				loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
				loReturn.put("response_error", lsError);
			}
			return loReturn;
		} catch (Exception ex) {
			ex.printStackTrace();
			loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
			loReturn.put("response_error", "Error in generating bill" + ex.getMessage());
			return loReturn;
		}

	}

	private void orderToBill(TranOrder foOrder, TranBill foBill) {
		foBill.getTranBillKey().setCompany_code(foOrder.getTranOrderKey().getCompany_code());
		foBill.getTranBillKey().setBill_no(
				ShopSequenceGenerator.getNextBillId(foOrder.getTranOrderKey().getCompany_code(), jdbcTemplate));

		foOrder.setBill_no(foBill.getTranBillKey().getBill_no());
		foOrder.setStatus(AppConstants.STATUS_ORDER_PROCESSING);

		foBill.setOrder_no(foOrder.getTranOrderKey().getOrder_no());
		foBill.setCustomer_code(foOrder.getCustomer_code());
		foBill.setCustomer_name(foOrder.getCustomer_name());
		foBill.setDelivery_address(foOrder.getDelivery_address());
		foBill.setBill_date(foOrder.getOrder_date());
		foBill.setPin(foOrder.getPin());
		foBill.setMobile(foOrder.getMobile());
		foBill.setEmail(foOrder.getEmail());
		foBill.setStatus(AppConstants.STATUS_BILL_NOT_SUBMITTED);
		foBill.setScheduled_delivery_date(foOrder.getScheduled_delivery_date());
		foBill.setScheduled_delivery_time(foOrder.getScheduled_delivery_time());

		List<TranOrderDetail> loOrderList = foOrder.getTranOrderDetail();
		List<TranBillDetail> loBillList = foBill.getTranBillDetailList();

		for (TranOrderDetail loOrderRow : loOrderList) {
			TranBillDetail loTranBillDetail = new TranBillDetail();

			loTranBillDetail.getTranBillDetailKey()
					.setCompany_code(loOrderRow.getTranOrderDetailKey().getCompany_code());
			loTranBillDetail.getTranBillDetailKey().setBill_no(foBill.getTranBillKey().getBill_no());
			loTranBillDetail.getTranBillDetailKey()
					.setProduct_code(loOrderRow.getTranOrderDetailKey().getProduct_code());
			loTranBillDetail.getTranBillDetailKey().setVendor_code(loOrderRow.getTranOrderDetailKey().getVendor_code());
			loTranBillDetail.getTranBillDetailKey()
					.setSelling_price(loOrderRow.getTranOrderDetailKey().getSelling_price());

			loTranBillDetail.setAlt_product_code(loOrderRow.getAlt_product_code());
			loTranBillDetail.setCess_percentage(loOrderRow.getCess_percentage());
			loTranBillDetail.setGst_percentage(loOrderRow.getGst_percentage());
			loTranBillDetail.setHsn_code(loOrderRow.getHsn_code());
			loTranBillDetail.setMrp(loOrderRow.getMrp());
			loTranBillDetail.setOrder_qty(loOrderRow.getOrder_qty());
			loTranBillDetail.setBill_qty(loOrderRow.getOrder_qty());
			loTranBillDetail.setReturned_qty(BigDecimal.ZERO);
			loTranBillDetail.setProduct_name(loOrderRow.getProduct_name());
			loTranBillDetail.setLanding_cost(loOrderRow.getLanding_cost());
			loTranBillDetail.setDisc_per(loOrderRow.getDisc_per());
			loTranBillDetail.setBv_per(loOrderRow.getBv_per());
			loTranBillDetail.setDiscount(loOrderRow.getDiscount());
			loTranBillDetail.setBv(loOrderRow.getBv());
			loBillList.add(loTranBillDetail);
		}

		List<TranOrderPayment> loOrderPayment = foOrder.getTranOrderPayment();
		List<TranBillPayment> loBillPayment = foBill.getTranBillPaymentList();
		int liPaymentId = 1;
		for (TranOrderPayment loPaymentRow : loOrderPayment) {

			TranBillPayment loTranBillPayment = new TranBillPayment();
			loTranBillPayment.getTranBillPaymentKey()
					.setCompany_code(loPaymentRow.getTranOrderPaymentKey().getCompany_code());
			loTranBillPayment.getTranBillPaymentKey().setBill_no(foBill.getTranBillKey().getBill_no());
			loTranBillPayment.getTranBillPaymentKey().setPayment_id(String.valueOf(liPaymentId++));
			loTranBillPayment.setPayment_amt(loPaymentRow.getPayment_amt());
			loTranBillPayment.setPayment_date(loPaymentRow.getPayment_date());
			loTranBillPayment.setPayment_mode(loPaymentRow.getPayment_mode());
			loTranBillPayment.setRemark(loPaymentRow.getRemark());
			loTranBillPayment.setRzr_order_id(loPaymentRow.getRzr_order_id());
			loTranBillPayment.setRzr_order_status(loPaymentRow.getRzr_order_status());
			loTranBillPayment.setRzr_payment_id(loPaymentRow.getRzr_payment_id());
			loTranBillPayment.setRzr_sign(loPaymentRow.getRzr_sign());
			loTranBillPayment.setStatus(loPaymentRow.getStatus());
			loTranBillPayment.setSub_status(AppConstants.STATUS_BILL_SUBMITTED);
			loBillPayment.add(loTranBillPayment);
		}
	}

	@RequestMapping(value = "/GetBillProductInfo", method = RequestMethod.GET)
	public @ResponseBody JSONObject getBillProductInfo(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse, @RequestParam String requestData) {

		JSONObject loObject = new JSONObject();
		try {
			JSONParser parser = new JSONParser();
			JSONObject requestJson = (JSONObject) parser.parse(requestData);
			String product_code = (String) requestJson.get("product_code");
			String vendor_code = (String) requestJson.get("vendor_code");
			String selling_price = (String) requestJson.get("selling_price");

			List<Map<String, Object>> loMapProductList = (List<Map<String, Object>>) foHttpSession
					.getAttribute("PRODUCTS");
			List<Map<String, Object>> loMapProductFiltered = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> loProductRow : loMapProductList) {
				boolean bool_selling_price = false;
				if (AppUtils.isValueEmpty(selling_price) || ((BigDecimal) loProductRow.get("selling_price"))
						.compareTo(new BigDecimal(selling_price)) == 0) {
					bool_selling_price = true;
				}

				// (vendor_code != null && vendor_code.equals((String)
				// loProductRow.get("vendor_code")))

				if ((product_code != null 
						&& (product_code.equals((String) loProductRow.get("product_code"))
								|| product_code.equals((String) loProductRow.get("alt_product_code"))))
						&& bool_selling_price) {
					loMapProductFiltered.add(loProductRow);
				}
			}
			if (loMapProductFiltered.size() > 1) {
				Gson gsonObject = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
				loObject.put("response_status", FrameworkConstants.RESPONSE_STATUS_UNEXPECTED);
				loObject.put("product_list", LOVUtils.buildDynamicLOV(foHttpSession, null, loMapProductFiltered));
			} else if (loMapProductFiltered.size() == 1) {
				Gson gsonObject = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
				loObject = (JSONObject) parser.parse(gsonObject.toJson(loMapProductFiltered.get(0)));
				loObject.put("response_status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
			} else {
				loObject.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
				loObject.put("response_error", "No product found.");

			}
			return loObject;

		} catch (Exception ex) {
			return loObject;
		}
	}

	@RequestMapping(value = "/GetCustomerInfo", method = RequestMethod.GET)
	public @ResponseBody JSONObject getCustomerInfo(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse, @RequestParam String requestData) {
		JSONObject loObject = new JSONObject();
		try {
			JSONParser parser = new JSONParser();
			JSONObject requestJson = (JSONObject) parser.parse(requestData);
			String member_code = (String) requestJson.get("customer_code");

			if (FrameworkConstants.STRING_ZERO.equals(member_code)) {
				loObject.put("MEMBER_NAME", "All Members");
			}

			List<Map<String, Object>> loMapMemberList = (List<Map<String, Object>>) foHttpSession
					.getAttribute("CUSTOMERS");
			for (Map<String, Object> loMemberRow : loMapMemberList) {
				if (member_code != null && member_code.equals((String) loMemberRow.get("customer_code"))) {
					Gson gsonObject = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
					loObject = (JSONObject) parser.parse(gsonObject.toJson(loMemberRow));
					return loObject;
				}
			}
			return loObject;
		} catch (Exception ex) {
			return loObject;
		}
	}

	@RequestMapping(value = "/NewBill", method = RequestMethod.GET)
	public ModelAndView newBill(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			TranBill loBill = new TranBill(true);

			if (foHttpSession.getAttribute("CUSTOMERS") == null) {
				AppReferenceUtils.setReferenceDataCustomers(foHttpSession, jdbcTemplate);
				LOVUtils.buildLOV(foHttpSession, "CUSTOMERS");
			}
			if (foHttpSession.getAttribute("PRODUCTS") == null) {
				AppReferenceUtils.setReferenceDataProducts(foHttpSession, jdbcTemplate);
				LOVUtils.buildLOV(foHttpSession, "PRODUCTS");
			}

			model.put("TranBill", loBill);
			model.put("TranBillDetailRows", loBill.getTranBillDetailList());
			model.put("TranBillPaymentRows", loBill.getTranBillPaymentList());
			return new ModelAndView("/Shop/Main_Bill", model);

		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("/Shop/Main_Bill", model);
		}
	}

	@RequestMapping(value = "/ReturnBill", method = RequestMethod.POST)
	public ModelAndView returnBill(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse, @RequestParam String lookup_bill_no_return) {
		JSONObject loObject = new JSONObject();
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			TranBillKey loBillKeyParam = new TranBillKey();
			loBillKeyParam.setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			loBillKeyParam.setBill_no(lookup_bill_no_return);
			TranBill loBill = TranBillService.getBill(loBillKeyParam);

			if (loBill == null) {
				AlertUtils.addError("Bill not found.", foHttpSession);
				loBill = new TranBill(true);
			}

			if (!loBill.getStatus().equals(AppConstants.STATUS_BILL_SUBMITTED)) {
				AlertUtils.addError("Returned can not be process on this bill as bill is not in final status.",
						foHttpSession);
				loBill = new TranBill(true);
			}

			if (loBill.getStatus().equals(AppConstants.STATUS_BILL_NOT_SUBMITTED)) {
				if (foHttpSession.getAttribute("CUSTOMERS") == null) {
					AppReferenceUtils.setReferenceDataCustomers(foHttpSession, jdbcTemplate);
					LOVUtils.buildLOV(foHttpSession, "CUSTOMERS");
				}
				if (foHttpSession.getAttribute("PRODUCTS") == null) {
					AppReferenceUtils.setReferenceDataProducts(foHttpSession, jdbcTemplate);
					LOVUtils.buildLOV(foHttpSession, "PRODUCTS");
				}
			}
			model.put("tran_return", "Y");
			model.put("TranBill", loBill);
			model.put("TranBillDetailRows", loBill.getTranBillDetailList());
			model.put("TranBillPaymentRows", loBill.getTranBillPaymentList());
			return new ModelAndView("/Shop/Main_Bill", model);

		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("/Shop/Main_Bill", model);
		}
	}

	@RequestMapping(value = "/ManageReturns", method = RequestMethod.GET)
	public ModelAndView manageReturns(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse) {
		JSONObject loObject = new JSONObject();
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			TranBill loBill = new TranBill(true);

			model.put("tran_return", "Y");
			model.put("TranBill", loBill);
			model.put("TranBillDetailRows", loBill.getTranBillDetailList());
			model.put("TranBillPaymentRows", loBill.getTranBillPaymentList());
			return new ModelAndView("/Shop/Main_Bill", model);

		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("/Shop/Main_Bill", model);
		}
	}

	@RequestMapping(value = "/GetBill", method = RequestMethod.POST)
	public ModelAndView getBill(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse, @RequestParam String lookup_bill_no) {
		JSONObject loObject = new JSONObject();
		Map<String, Object> model = new HashMap<String, Object>();
		try {

			TranBillKey loBillKeyParam = new TranBillKey();
			loBillKeyParam.setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			loBillKeyParam.setBill_no(lookup_bill_no);
			TranBill loBill = TranBillService.getBill(loBillKeyParam);

			if (loBill == null) {
				AlertUtils.addError("Bill not found.", foHttpSession);
				loBill = new TranBill(true);
			}

			if (loBill.getStatus().equals(AppConstants.STATUS_BILL_NOT_SUBMITTED)) {
				if (foHttpSession.getAttribute("CUSTOMERS") == null) {
					AppReferenceUtils.setReferenceDataCustomers(foHttpSession, jdbcTemplate);
					LOVUtils.buildLOV(foHttpSession, "CUSTOMERS");
				}
				if (foHttpSession.getAttribute("PRODUCTS") == null) {
					AppReferenceUtils.setReferenceDataProducts(foHttpSession, jdbcTemplate);
					LOVUtils.buildLOV(foHttpSession, "PRODUCTS");
				}
			}

			model.put("TranBill", loBill);
			model.put("TranBillDetailRows", loBill.getTranBillDetailList());
			model.put("TranBillPaymentRows", loBill.getTranBillPaymentList());
			return new ModelAndView("/Shop/Main_Bill", model);

		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView("/Shop/Main_Bill", model);
		}
	}

	@RequestMapping(value = "/SaveBill", method = RequestMethod.POST)
	//@Transactional(propagation = Propagation.REQUIRES_NEW)
	public @ResponseBody JSONObject saveBill(@ModelAttribute("TranBill") TranBill foTranBill, BindingResult foResult,
			HttpSession foHttpSession, HttpServletRequest foHttpRequest) {
		AlertUtils.resetMessage(foHttpSession);
		JSONObject loReturn = new JSONObject();
		try {
			AlertUtils.resetMessage(foHttpSession);
			Session loSession = startTransation(sessionFactory);

			Map<String, Object> model = new HashMap<String, Object>();

			prepareBill(foTranBill, foHttpSession);

			AlertUtils.validateModel(foTranBill, foHttpSession);

			String lsError = AlertUtils.getErrors(foHttpSession);
			if (AppUtils.isValueEmpty(lsError)) {
				TranBillService.addBill(loSession, foTranBill);
				if (commitNoMsg(loSession.getTransaction(), loSession, foHttpSession)) {
					loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
					loReturn.put("bill_no_enc", Security.encrypt(foTranBill.getTranBillKey().getBill_no()));
				} else {
					loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
					loReturn.put("response_error", "Error in generating bill. Error-Save failed.");

				}
			} else {
				loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
				loReturn.put("response_error", lsError);
			}
			loReturn.put("bill_no", foTranBill.getTranBillKey().getBill_no());
			return loReturn;
		} catch (Exception ex) {
			ex.printStackTrace();
			loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
			loReturn.put("response_error", "Error in generating bill" + ex.getMessage());
			return loReturn;
		}

	}

	private void prepareBill(TranBill foTranBill, HttpSession foHttpSession) {
		foTranBill.getTranBillKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
		if (AppUtils.isValueEmpty(foTranBill.getTranBillKey().getBill_no())) {
			foTranBill.getTranBillKey().setBill_no(
					ShopSequenceGenerator.getNextBillId(GlobalValues.getCompanyCode(foHttpSession), jdbcTemplate));
		}
		List<TranBillDetail> loBillProductList = foTranBill.getTranBillDetailList();
		loBillProductList.removeIf( loBillProduct -> (loBillProduct.TranBillDetailKey.product_code == null));
		for (TranBillDetail loBillProduct : loBillProductList) {
			loBillProduct.getTranBillDetailKey().setCompany_code(foTranBill.getTranBillKey().getCompany_code());
			loBillProduct.getTranBillDetailKey().setBill_no(foTranBill.getTranBillKey().getBill_no());
		}
		
		List<TranBillPayment> loBillPaymentList = foTranBill.getTranBillPaymentList();
		int i = 1;
		for (TranBillPayment loBillPayment : loBillPaymentList) {
			i++;
			loBillPayment.getTranBillPaymentKey().setCompany_code(foTranBill.getTranBillKey().getCompany_code());
			loBillPayment.getTranBillPaymentKey().setBill_no(foTranBill.getTranBillKey().getBill_no());
			if (AppUtils.isValueEmpty(loBillPayment.getTranBillPaymentKey().getPayment_id())) {
				loBillPayment.getTranBillPaymentKey().setPayment_id(String.valueOf(i));
			}
			if (foTranBill.getStatus().equals(AppConstants.STATUS_BILL_SUBMITTED)
					&& loBillPayment.getStatus().equals(AppConstants.STATUS_PAYMENT_PAID)) {
				loBillPayment.setSub_status(AppConstants.STATUS_BILL_SUBMITTED);
			}
			loBillPayment.setPayment_date(AppUtils.getCurrentDate());
		}
	}

	@RequestMapping(value = "/DeleteBillPayment", method = RequestMethod.GET)
	//@Transactional(propagation = Propagation.REQUIRES_NEW)
	public @ResponseBody JSONObject deleteBillPayment(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse, @RequestParam String requestData) {
		JSONObject loReturn = new JSONObject();
		try {
			if (!AppUtils.isValueEmpty(requestData)) {
				JSONParser parser = new JSONParser();
				JSONObject requestJson = (JSONObject) parser.parse(requestData);
				TranBillPayment loTranBillPayment = new TranBillPayment();
				loTranBillPayment.getTranBillPaymentKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
				loTranBillPayment.getTranBillPaymentKey().setBill_no((String) requestJson.get("bill_no"));
				loTranBillPayment.getTranBillPaymentKey().setPayment_id((String) requestJson.get("payment_id"));
				Session loSession = startTransation(sessionFactory);
				TranBillService.deleteBillPayment(loSession, loTranBillPayment);
				if (commitNoMsg(loSession.getTransaction(), loSession, foHttpSession)) {
					loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
				} else {
					loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
				}

			} else {
				loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
			}
			return loReturn;
		} catch (Exception ex) {
			ex.printStackTrace();
			loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
			return loReturn;
		}
	}

	@RequestMapping(value = "/DeleteBillDetail", method = RequestMethod.GET)
	//@Transactional(propagation = Propagation.REQUIRES_NEW)
	public @ResponseBody JSONObject deleteBillDetail(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse, @RequestParam String requestData) {
		JSONObject loReturn = new JSONObject();
		try {
			if (!AppUtils.isValueEmpty(requestData)) {
				JSONParser parser = new JSONParser();
				JSONObject requestJson = (JSONObject) parser.parse(requestData);
				TranBillDetail loTranBillDetail = new TranBillDetail();
				loTranBillDetail.getTranBillDetailKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
				loTranBillDetail.getTranBillDetailKey().setBill_no((String) requestJson.get("bill_no"));
				loTranBillDetail.getTranBillDetailKey().setProduct_code((String) requestJson.get("product_code"));
				loTranBillDetail.getTranBillDetailKey().setVendor_code((String) requestJson.get("vendor_code"));
				loTranBillDetail.getTranBillDetailKey()
						.setSelling_price(new BigDecimal((String) requestJson.get("selling_price")));
				Session loSession = startTransation(sessionFactory);
				TranBillService.deleteBillDetail(loSession, loTranBillDetail);
				if (commitNoMsg(loSession.getTransaction(), loSession, foHttpSession)) {
					loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
				} else {
					loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
				}

			} else {
				loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
			}
			return loReturn;
		} catch (Exception ex) {
			ex.printStackTrace();
			loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
			return loReturn;
		}
	}

	/*@RequestMapping(value = "/Bills", method = RequestMethod.GET)
	public ModelAndView bills(HttpSession foHttpSession) {
		AlertUtils.resetMessage(foHttpSession);
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			TranBill loBill = new TranBill();
			loBill.getTranBillKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			loBill.setStatus(AppConstants.STATUS_BILL_SUBMITTED);
			loBill.setBill_date(null);
			loBill.setBill_to_date(null);
			List<Map<String, Object>> loBills = TranBillService.getBills(loBill);

			model.put("BillList", loBills);
			model.put("BillSearch", loBill);
			return new ModelAndView("/Shop/Main_ManageBills", model);
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			ex.printStackTrace();
			return new ModelAndView("/app/jsp/Home", null);
		}
	}*/

	@RequestMapping(value = "/UpdateBillHeader", method = RequestMethod.POST)
	public @ResponseBody JSONObject updateBillStatus(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse, @RequestParam String requestData) {
		JSONObject loReturn = new JSONObject();
		try {
			if (!AppUtils.isValueEmpty(requestData)) {
				JSONParser parser = new JSONParser();
				JSONObject requestJson = (JSONObject) parser.parse(requestData);
				TranBillKey loTranBillKeyParam = new TranBillKey();
				loTranBillKeyParam.setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
				loTranBillKeyParam.setBill_no((String) requestJson.get("bill_no"));
				Session loSession = startTransation(sessionFactory);
				TranBill loBill = TranBillService.getBillHeader(loTranBillKeyParam);

				if (((String) requestJson.get("attr_name")).equals("scheduled_delivery_date")) {
					loBill.setScheduled_delivery_date(AppUtils.getFormattedTimestamp((String)requestJson.get("value")));
				} else if (((String) requestJson.get("attr_name")).equals("delivered_by")) {
					loBill.setDelivered_by((String) requestJson.get("value"));
				} else if (((String) requestJson.get("attr_name")).equals("status")) {
					loBill.setStatus((String) requestJson.get("value"));
				}

				AlertUtils.validateModel(loBill, foHttpSession);
				TranBillService.addBill(loSession, loBill);

				String lsError = AlertUtils.getErrors(foHttpSession);
				if (AppUtils.isValueEmpty(lsError)) {
					if (commitNoMsg(loSession.getTransaction(), loSession, foHttpSession)) {
						loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
						// .put("bill_no_enc",
						// Security.encrypt(loBill.getTranBillKey().getBill_no()));
					} else {
						loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
						loReturn.put("response_error", "Error on saving data");
						// loReturn.put("response_error", "Error in generating
						// bill. Error-Save failed.");

					}
				} else {
					loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
					loReturn.put("response_error", "Error on saving data");
							// loReturn.put("response_error", lsError);
				}
			}
			return loReturn;
		} catch (Exception ex) {
			ex.printStackTrace();
			loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
			loReturn.put("response_error", "Error on saving data " + ex.getMessage());
			return loReturn;
		}
	}
	
	@RequestMapping(value = "/ManageBills", method = RequestMethod.GET)
	public ModelAndView manageBills(HttpSession foHttpSession, HttpServletRequest foHttpRequest) {
		Map<String, Object> model = new HashMap<String, Object>();
		TranBill loTranBill = new TranBill();
		loTranBill.getTranBillKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
		loTranBill.setBill_date(AppUtils.getCurrentDate());
		loTranBill.setBill_to_date(AppUtils.getCurrentDate());
		loTranBill.setStatus(AppConstants.STATUS_BILL_SUBMITTED);
		List<Map<String, Object>> loBills = TranBillService.getBills(loTranBill);
		
		model.put("BillList", loBills);
		model.put("TranBill", loTranBill	);
		return new ModelAndView("/Shop/Main_ManageBills", model);
	}

	@RequestMapping(value = "/SearchBills", method = RequestMethod.POST)
	public ModelAndView searchBills(@ModelAttribute("TranBill") TranBill loTranBill, BindingResult foResult,
			HttpSession foHttpSession, HttpServletRequest foHttpRequest) {
		AlertUtils.resetMessage(foHttpSession);
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			loTranBill.getTranBillKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			loTranBill.setBill_date(AppUtils.getFormattedTimestamp(foHttpRequest.getParameter("bill_date")));
			loTranBill.setBill_to_date(AppUtils.getFormattedTimestamp(foHttpRequest.getParameter("bill_to_date")));
			List<Map<String, Object>> loBills = TranBillService.getBills(loTranBill);
			model.put("BillList", loBills);
			model.put("TranBill", loTranBill);
			return new ModelAndView("/Shop/Main_ManageBills", model);
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			ex.printStackTrace();
			return new ModelAndView("/app/jsp/Home");
		}
	}
}
