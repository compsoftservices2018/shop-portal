package com.compsoft.shop.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
public class VidhataIntegrationController extends SuperController {

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

	@RequestMapping(value = "/VidhataSignIn", method = RequestMethod.GET)
	public ModelAndView vidhataSignIn(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			@RequestParam String id, @RequestParam String circle) {
		AlertUtils.resetMessage(foHttpSession);
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
			model.put("login_id", id);
			return new ModelAndView("/Shop/Main_VidhataSignIn", model);
		} catch (Exception ex) {
			
			logException(ex, foHttpSession);
			AlertUtils.addError("Permission restricted.", foHttpSession);
			return new ModelAndView("/Shop/Main_VidhataSignIn");
		}

	}
	
	@RequestMapping(value = "/ShopOnlineVidhataSignin", method = RequestMethod.POST)
	public ModelAndView shopOnlineVidhataSignin(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			@RequestParam String login_id, @RequestParam String password) {
		AlertUtils.resetMessage(foHttpSession);
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			MstCustomer loMstCustomer = new MstCustomer();
			loMstCustomer.getMstCustomerKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			loMstCustomer.getMstCustomerKey().setCustomer_code(login_id);
			MstCustomer loCustomer = MstCustomerService.getCustomer(loMstCustomer);

			if (loCustomer == null) {
				model.put("user_error", "Authentication failed!!!! Invalid Login id.");
				model.put("login_id", login_id);
				return new ModelAndView("/Shop/Main_VidhataSignIn", model);
			}

			if (password.equals(loCustomer.getTpt_password())) {
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
				model.put("login_id", login_id);
				return new ModelAndView("/Shop/Main_VidhataSignIn", model);
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
			model.put("login_id", login_id);
			return new ModelAndView("/Shop/Main_VidhataSignIn");
		}

	}
	
}
