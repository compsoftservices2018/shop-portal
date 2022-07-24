package com.framework.controller;

import java.util.List;
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

import com.framework.model.MstCompany;
import com.framework.model.MstUser;
import com.framework.reference.AdminAppReferenceUtils;
import com.framework.reference.ReferenceUtils;
import com.framework.security.Security;
import com.framework.service.MstCompanyService;
import com.framework.service.MstUserService;
import com.framework.utils.AlertUtils;
import com.framework.utils.FrameworkConstants;
import com.framework.utils.GlobalValues;

@Controller
public class CommonController extends SuperController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MstUserService MstUserService;

	@Autowired
	private MstCompanyService MstCompanyService;

	@RequestMapping(value = "/Admin", method = RequestMethod.GET)
	public ModelAndView admin(HttpSession foHttpSession) {
		AdminAppReferenceUtils.setAppReferenceDataBeforeLogin(foHttpSession, jdbcTemplate);
		MstCompany loMstCompany = null;
		loMstCompany = MstCompanyService
				.getCompanyByKey(ReferenceUtils.getGlobalConfigParamValue(foHttpSession, "COMPANY_KEY"));
		loMstCompany.getMstCompanyKey().setCompany_code(null);
		foHttpSession.setAttribute("COMPANYOBJ", loMstCompany);
		return new ModelAndView("/app/jsp/Home");
	}

	@RequestMapping(value = "/AdminSignIn", method = RequestMethod.POST)
	public ModelAndView adminSignIn(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			@RequestParam String login_id, @RequestParam String password) {
		String lsUser = Security.encrypt(login_id);
		String lsPassword = Security.encrypt(password);
		AdminAppReferenceUtils.setAppReferenceDataBeforeLogin(foHttpSession, jdbcTemplate);
		MstUser loUser = null;
		MstCompany loMstCompany = null;
		String lsSelect = "SELECT * FROM MST_SYSADMUSER WHERE USER_ID = ? AND PASSWORD = ? AND STATUS = 'A'";
		List<Map<String, Object>> loUserMap = jdbcTemplate.queryForList(lsSelect,
				new Object[] { new String(lsUser), new String(lsPassword) });
		loUser = new MstUser();
		if (loUserMap.size() == 1) {
			loUser.getMstUserKey().setCompany_code("SUPERADMIN");
			loUser.getMstUserKey().setUser_code(login_id);
			loUser.setStatus("A");
			loUser.setUser_type(FrameworkConstants.USER_TYPE_SUPRADMIN);
			loUser.setUser_name("Super Admin");
			loUser.setMobile("");
			loUser.setEmail("");
			loMstCompany = MstCompanyService
					.getCompanyByKey(ReferenceUtils.getGlobalConfigParamValue(foHttpSession, "COMPANY_KEY"));
			loMstCompany.getMstCompanyKey().setCompany_code(null);
			foHttpSession.setAttribute("COMPANYOBJ", loMstCompany);
			foHttpSession.setAttribute("USEROBJ", loUser);
		} else {
			loUser = new MstUser();
			loUser.getMstUserKey().setUser_code(login_id);
			loUser.setUser_type(FrameworkConstants.USER_TYPE_ADMIN);
			loUser = MstUserService.getUser(loUser);
			if (loUser == null) {
				AlertUtils.addError("Authentication failed!!!! Invalid Login id.", foHttpSession);
				return new ModelAndView("/base/AdminLogin");
			} else {
				loMstCompany = MstCompanyService
						.getCompanyByKey(Security.encrypt(loUser.getMstUserKey().getCompany_code()));
				foHttpSession.setAttribute("COMPANYOBJ", loMstCompany);
				foHttpSession.setAttribute("USEROBJ", loUser);
			}
		}

		if (AlertUtils.hasAppErrors(foHttpSession)) {
			foHttpSession.setAttribute("USEROBJ", null);
			return new ModelAndView("/app/jsp/Home");
		}
		AdminAppReferenceUtils.setAppReferenceDataOnLogin(foHttpSession, jdbcTemplate);
		return new ModelAndView("/app/jsp/Home");
	}

	@RequestMapping(value = "/AppSignIn", method = RequestMethod.POST)
	public ModelAndView appSignIn(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			@RequestParam String login_id, @RequestParam String password) {
		AlertUtils.resetMessage(foHttpSession);
		MstUser loUser = null;
		try {
			loUser = new MstUser();
			loUser.getMstUserKey().setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			loUser.getMstUserKey().setUser_code(login_id);
			loUser.setUser_type(FrameworkConstants.USER_TYPE_USER);
			loUser = MstUserService.getUser(loUser);
			if (loUser == null) {
				AlertUtils.addError("Authentication failed!!!! Invalid Login id.", foHttpSession);
				return new ModelAndView("/base/jsp/AppLogin");
			} else {
				foHttpSession.setAttribute("USEROBJ", loUser);
			}

			if (AlertUtils.hasAppErrors(foHttpSession)) {
				foHttpSession.setAttribute("USEROBJ", null);
				return new ModelAndView("/app/jsp/Home");
			}
			AdminAppReferenceUtils.setAppReferenceDataOnLogin(foHttpSession, jdbcTemplate);
			return new ModelAndView("/app/jsp/Home");

		} catch (Exception ex) {
			logException(ex, foHttpSession);
			return new ModelAndView("/app/jsp/Home");
		}
	}

	@RequestMapping(value = "/AdminSignOut", method = RequestMethod.GET)
	public ModelAndView adminSignOut(HttpSession foHttpSession, HttpServletRequest foHttpRequest) {
		foHttpSession.setAttribute("USEROBJ", null);
		String lsRedirect = "redirect:/Admin";
		foHttpSession.invalidate();
		return new ModelAndView(lsRedirect);
	}

	@RequestMapping(value = "/PageAdminSignIn", method = RequestMethod.GET)
	public ModelAndView pageAdminSignIn(HttpSession foHttpSession, HttpServletRequest foHttpRequest) {
		return new ModelAndView("/base/jsp/AdminLogin");
	}

	@RequestMapping(value = "/PageAppSignIn", method = RequestMethod.GET)
	public ModelAndView pageAppSignIn(HttpSession foHttpSession, HttpServletRequest foHttpRequest) {
		return new ModelAndView("/base/jsp/AppLogin");
	}

	@RequestMapping(value = "/ClosePage", method = RequestMethod.GET)
	public ModelAndView closePage(HttpSession foHttpSession, HttpServletRequest foHttpRequest) {
		return new ModelAndView("/app/jsp/Home");
	}

	@RequestMapping(value = "/Home", method = RequestMethod.GET)
	public ModelAndView home(HttpSession foHttpSession, HttpServletRequest foHttpRequest) {
		return new ModelAndView("/app/jsp/Home");
	}

}
