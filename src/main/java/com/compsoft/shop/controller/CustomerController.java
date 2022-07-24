package com.compsoft.shop.controller;

import java.util.HashMap;
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

import com.compsoft.shop.model.MstCustomer;
import com.compsoft.shop.model.MstCustomerKey;
import com.compsoft.shop.reference.ShopSequenceGenerator;
import com.compsoft.shop.service.MstCustomerService;
import com.framework.controller.SuperController;
import com.framework.reference.ReferenceUtils;
import com.framework.security.Security;
import com.framework.service.CommonService;
import com.framework.utils.AlertUtils;
import com.framework.utils.AppConstants;
import com.framework.utils.AppUtils;
import com.framework.utils.FrameworkConstants;
import com.framework.utils.GlobalValues;

/**
 * @author Vaishali Chawadkar
 *
 */

@Controller
public class CustomerController extends SuperController {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private MstCustomerService MstCustomerService;

	@Autowired
	private CommonService CommonService;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	@RequestMapping(value = "/AddCustomer", method = RequestMethod.GET)
	public ModelAndView addCustomer(@ModelAttribute("MstCustomer") MstCustomer loCustomer, HttpSession foHttpSession) {
		AlertUtils.resetMessage(foHttpSession);
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			loCustomer = new MstCustomer();
			model.put("MstCustomer", loCustomer);
			return new ModelAndView("/Shop/Main_Customer", model);
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			return new ModelAndView("/app/jsp/Home");
		}
	}

	@RequestMapping(value = "/GetCustomer", method = RequestMethod.GET)
	public ModelAndView getCustomer(HttpSession foHttpSession) {
		AlertUtils.resetMessage(foHttpSession);
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			MstCustomer loCustomer = (MstCustomer) foHttpSession.getAttribute("LOGGEDINCUSTOMER");
			model.put("MstCustomer", loCustomer);
			return new ModelAndView("/Shop/Main_Customer", model);
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			return new ModelAndView("/app/jsp/Home");
		}
	}

	@RequestMapping(value = "/SaveCustomer", method = RequestMethod.POST)
	public ModelAndView saveCustomer(@ModelAttribute("MstCustomer") MstCustomer foMstCustomer, BindingResult foResult,
			HttpSession foHttpSession, HttpServletRequest foHttpRequest) {
		AlertUtils.resetMessage(foHttpSession);
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			foMstCustomer.getMstCustomerKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));

			if (foHttpSession.getAttribute("LOGGEDINCUSTOMER") == null) {
				boolean isError = false;
				if (MstCustomerService.getCustomer(foMstCustomer) != null) {
					AlertUtils.addError(
							"Customer already registered with this phone number or email id. Please login.",
							foHttpSession);
					isError = true;
				} else if (foMstCustomer.getOtp() == null
						|| !foMstCustomer.getOtp().equals(foHttpSession.getAttribute("OTP"))) {
					AlertUtils.addError("Incorrect OTP. Please re-enter.", foHttpSession);
					isError = true;
				}

				if (isError) {
					model.put("MstCustomer", foMstCustomer);
					return new ModelAndView("/Shop/Main_Customer", model);
				}
								
				if (AppUtils.isValueEmpty(foMstCustomer.getMstCustomerKey().getCustomer_code())) {
					String lsNewCustomerCode = ShopSequenceGenerator.getNextCustomerId(GlobalValues.getCompanyCode(foHttpSession),
							jdbcTemplate);
					foMstCustomer.getMstCustomerKey().setCustomer_code(lsNewCustomerCode);
				}
			}
			
			foMstCustomer.setCustomer_type(AppConstants.CUST_TYPE_CUSTOMER); 
			foMstCustomer.getMstCustomerKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			foMstCustomer.setGstin_no(FrameworkConstants.EMPTY);
			foMstCustomer.setDelivery_pins(FrameworkConstants.EMPTY);
			foMstCustomer.setAlt_customer_code(FrameworkConstants.EMPTY);
			foMstCustomer.setParent_customer_code(FrameworkConstants.EMPTY);
			
			Session loSession = startTransation(sessionFactory);
			foMstCustomer.setStatus(FrameworkConstants.STATUS_ACTIVE);
			AlertUtils.validateModel(foMstCustomer, foHttpSession);
			String lsError = AlertUtils.getErrors(foHttpSession);

			if (AppUtils.isValueEmpty(lsError)) {
				MstCustomerService.addCustomer(loSession, foMstCustomer);
				if (commitNoMsg(loSession.getTransaction(), loSession, foHttpSession)) {
					if (foHttpSession.getAttribute("LOGGEDINCUSTOMER") == null) {
						AlertUtils.addInfo("Customer registration is successful. Please login..", foHttpSession);
						String lsMobile = String.valueOf(foMstCustomer.getMobile());
						String lsMsg = "Thank you for registering with " + GlobalValues.getCompanyName(foHttpSession)
								+ " The one stop shop for all your need at the best price. " + lsMobile
								+ " is your login id";
						CommonService.sendSMS(foHttpSession, foMstCustomer.getMobile(), lsMsg,
								ReferenceUtils.getCnfigParamValue(foHttpSession, "SMS_ON_REG"));
						foHttpSession.setAttribute("OTP", null);
						return new ModelAndView("/app/jsp/Home", model);
					} else {
						AlertUtils.addInfo("Customer information updated successfully.", foHttpSession);
						model.put("MstCustomer", foMstCustomer);
						return new ModelAndView("/Shop/Main_Customer", model);
					}
				}
			} else {
				AlertUtils.addInfo("Registration is not successful. Please contact system administrator.",
						foHttpSession);
				model.put("MstCustomer", foMstCustomer);
				return new ModelAndView("/app/jsp/Home", model);
			}
			return new ModelAndView("/app/jsp/Home", model);
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			return new ModelAndView("/app/jsp/Home");
		}
	}

	@RequestMapping(value = "/GetVendor", method = RequestMethod.POST)
	public ModelAndView getCustomer(@ModelAttribute("MstCustomer") MstCustomer foMstCustomer, BindingResult foResult,
			String company_code, String vendor_code, HttpSession foHttpSession, HttpServletRequest foHttpRequest) {
		AlertUtils.resetMessage(foHttpSession);
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			MstCustomerKey loVendorKey = new MstCustomerKey(company_code, vendor_code);
			MstCustomer loVendor = MstCustomerService.getCustomer(loVendorKey);
			model.put("MstCustomer", loVendor);
			return new ModelAndView("/Master/Main_Vendor", model);
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			return new ModelAndView("/app/jsp/Home");
		}
	}

	@RequestMapping(value = "/PageResetPassword", method = RequestMethod.GET)
	public ModelAndView pageResetPassword(HttpSession foHttpSession, HttpServletRequest foHttpRequest) {
		AlertUtils.resetMessage(foHttpSession);
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			return new ModelAndView("/Shop/Main_ResetPassword", model);
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			return new ModelAndView("/app/jsp/Home");
		}
	}

	@RequestMapping(value = "/ResetPassword", method = RequestMethod.POST)
	public ModelAndView resetPassword(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			@RequestParam String mobile, @RequestParam String otp) {
		AlertUtils.resetMessage(foHttpSession);
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			MstCustomer loMstCustomer = new MstCustomer();

			loMstCustomer.getMstCustomerKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			loMstCustomer.setMobile(mobile);
			loMstCustomer = MstCustomerService.getCustomer(loMstCustomer);

			if (loMstCustomer == null) {
				AlertUtils.addError("Customer not register with this id.", foHttpSession);
				model.put("mobile", mobile);
				return new ModelAndView("/Shop/Main_ResetPassword", model);
			}

			if (otp == null || !otp.equals(foHttpSession.getAttribute("OTP"))) {
				AlertUtils.addError("Incorrect OTP. Please re-enter.", foHttpSession);
				return new ModelAndView("/Shop/Main_ResetPassword", model);
			}

			String lsSMSNumber = loMstCustomer.getMobile();

			Session loSession = startTransation(sessionFactory);
			String lsTempPassword = Security.getTempPassword();
			loMstCustomer.setPassword(lsTempPassword);
			loMstCustomer.setTemp_password(lsTempPassword);
			AlertUtils.validateModel(loMstCustomer, foHttpSession);
			String lsError = AlertUtils.getErrors(foHttpSession);

			if (AppUtils.isValueEmpty(lsError)) {
				MstCustomerService.addCustomer(loSession, loMstCustomer);
				if (commitNoMsg(loSession.getTransaction(), loSession, foHttpSession)) {
					String lsMsg = "Your temporary pasword reset to " + lsTempPassword
							+ " Please change your password as soon as you login with temporary password.";
					if (CommonService.sendSMS(foHttpSession, lsSMSNumber, lsMsg, "Y")) {
						AlertUtils.addInfo("Temporary password has been sent to your registered phone number.",
								foHttpSession);
						return new ModelAndView("/app/jsp/Home", model);
					}
				}
			}
			model.put("mobile", mobile);
			return new ModelAndView("/Shop/Main_ResetPassword", model);
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			return new ModelAndView("/app/jsp/Home");
		}
	}

	@RequestMapping(value = "/SendOTP", method = RequestMethod.GET)
	public @ResponseBody JSONObject sendOTP(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse, @RequestParam String mobile) {
		AlertUtils.resetMessage(foHttpSession);
		JSONObject loReturn = new JSONObject();

		try {
			MstCustomer loMstCustomer = new MstCustomer();
			loMstCustomer.getMstCustomerKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			loMstCustomer.setMobile(mobile);

			if (MstCustomerService.getCustomer(loMstCustomer) != null) {
				loReturn.put("status", FrameworkConstants.RESPONSE_STATUS_FAILED);
				loReturn.put("error", "Customer already registered with this phone number or email id. Please login.");
				return loReturn;
			}

			String lsTempPassword = Security.getOTP();
			if (!AppUtils.isValueEmpty(lsTempPassword)) {
				String lsMsg = "Dear Customer " + lsTempPassword
						+ " is your registration OTP. Do not share it with anyone.";
				if (CommonService.sendSMS(foHttpSession, mobile, lsMsg,
						ReferenceUtils.getCnfigParamValue(foHttpSession, "OTP_ON_REG"))) {
					foHttpSession.setAttribute("OTP", lsTempPassword);
					loReturn.put("status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
					loReturn.put("msg",
							"OTP has been sent to your mobile number. Please enter valid OTP for successful registration.");
					return loReturn;
				} else {
					loReturn.put("status", FrameworkConstants.RESPONSE_STATUS_FAILED);
					loReturn.put("error", "Unable to send OTP. Please try later.");
					return loReturn;
				}
			} else {
				loReturn.put("status", FrameworkConstants.RESPONSE_STATUS_FAILED);
				loReturn.put("error", "Unable to send OTP. Please try later.");
				return loReturn;
			}
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			loReturn.put("status", FrameworkConstants.RESPONSE_STATUS_FAILED);
			loReturn.put("error", "Unable to send OTP. Please try later.");
			return loReturn;
		}

	}

	@RequestMapping(value = "/SendOTPReset", method = RequestMethod.GET)
	public @ResponseBody JSONObject sendOTPReset(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse, @RequestParam String mobile) {
		AlertUtils.resetMessage(foHttpSession);
		JSONObject loReturn = new JSONObject();
		try {
			MstCustomer loMstCustomer = new MstCustomer();
			loMstCustomer.getMstCustomerKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			loMstCustomer.setMobile(mobile);

			if (MstCustomerService.getCustomer(loMstCustomer) == null) {
				loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
				loReturn.put("response_error", "Unable to send OTP to this number. Please contact system administrator.");
				return loReturn;
			}

			String lsTempPassword = Security.getOTP();
			if (!AppUtils.isValueEmpty(lsTempPassword)) {
				String lsMsg = "Dear Customer " + lsTempPassword
						+ " is your password reset OTP. Please enter this OTP for resetting your password successfully.";
				if (CommonService.sendSMS(foHttpSession, mobile, lsMsg,
						ReferenceUtils.getCnfigParamValue(foHttpSession, "OTP_ON_REG"))) {
					foHttpSession.setAttribute("OTP", lsTempPassword);
					loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
					loReturn.put("response_success",
							"OTP has been sent to your mobile number. Please enter valid OTP for resetting your password.");
					return loReturn;
				} else {
					loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
					loReturn.put("response_error", "Unable to send OTP. Please try later.");
					return loReturn;
				}
			} else {
				loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
				loReturn.put("response_error", "Unable to send OTP. Please try later.");
				return loReturn;
			}
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			loReturn.put("response_status", FrameworkConstants.RESPONSE_STATUS_FAILED);
			loReturn.put("response_error", "Unable to send OTP. Please try later.");
			return loReturn;
		}

	}

	@RequestMapping(value = "/ValidateOTP", method = RequestMethod.POST)
	public @ResponseBody JSONObject validateOTP(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse, @RequestParam String requestotp) {
		AlertUtils.resetMessage(foHttpSession);
		JSONObject loReturn = new JSONObject();
		try {
			if (requestotp != null && requestotp.equals(foHttpSession.getAttribute("OTP"))) {
				loReturn.put("status", FrameworkConstants.RESPONSE_STATUS_SUCCESS);
				loReturn.put("msg", "OTP validated succesfully. Please continue..");
			} else {
				loReturn.put("status", FrameworkConstants.RESPONSE_STATUS_FAILED);
				loReturn.put("error", "Incorrect OTP. Please re-enter.");
			}
			return loReturn;
		} catch (Exception ex) {
			logException(ex, foHttpSession);
			loReturn.put("status", FrameworkConstants.RESPONSE_STATUS_FAILED);
			loReturn.put("error", "Unable to validate OTP. Please try later.");
			return loReturn;
		}
	}

	@RequestMapping(value = "/SaveDeliveryInfo", method = RequestMethod.GET)
	public @ResponseBody JSONObject getProductInfoFresh(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse, @RequestParam String requestData) {
		AlertUtils.resetMessage(foHttpSession);
		try {
			Session loSession = startTransation(sessionFactory);
			JSONParser parser = new JSONParser();
			JSONObject requestJson = (JSONObject) parser.parse(requestData);
			String delivery_pins = (String) requestJson.get("delivery_pins");

			MstCustomer loCustomer = (MstCustomer) foHttpSession.getAttribute("LOGGEDINCUSTOMER");
			loCustomer.setDelivery_pins(delivery_pins);
			JSONObject responseJson = new JSONObject();
			String lsError = AlertUtils.getErrors(foHttpSession);

			if (AppUtils.isValueEmpty(lsError)) {
				MstCustomerService.addCustomer(loSession, loCustomer);
				if (commitNoMsg(loSession.getTransaction(), loSession, foHttpSession)) {
					foHttpSession.setAttribute("LOGGEDINCUSTOMER", loCustomer);
					responseJson.put("status", "success");
					responseJson.put("msg", "Delivery information saved successfully");
					return responseJson;
				} else {
					responseJson.put("status", "failed");
					responseJson.put("error", "Unable to save delivery information. Please try later.");
					return responseJson;
				}
			} else {
				responseJson.put("status", "failed");
				responseJson.put("error", lsError);
				return responseJson;
			}

		} catch (Exception ex) {
			return null;
		}

	}

}
