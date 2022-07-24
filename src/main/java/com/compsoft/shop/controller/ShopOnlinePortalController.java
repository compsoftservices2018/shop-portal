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
import org.springframework.web.servlet.ModelAndView;

import com.compsoft.shop.reference.AppReferenceUtils;
import com.framework.controller.SuperController;
import com.framework.model.MstCompany;
import com.framework.reference.ReferenceUtils;
import com.framework.service.MstCompanyService;
import com.framework.utils.AlertUtils;
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
public class ShopOnlinePortalController extends SuperController {

	@Autowired
	private JdbcTemplate jdbcTemplate;


	@Autowired
	private MstCompanyService MstCompanyService;

	
	@RequestMapping(value = "/Portal", method = RequestMethod.GET)
	public ModelAndView ShopOnlinePortalControllerPortal(HttpSession foHttpSession, HttpServletRequest foHttpRequest, String circle) {

		try {
			AlertUtils.resetMessage(foHttpSession);
			Map<String, Object> model = new HashMap<String, Object>();

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

			return new ModelAndView("/app/jsp/Home");
		} catch (Exception ex) {
			
			logException(ex, foHttpSession);
			return new ModelAndView("/app/jsp/Home");
		}
	}

	
	@RequestMapping(value = "/PortalHome", method = RequestMethod.GET)
	public ModelAndView portalHome(HttpSession foHttpSession) {

		AlertUtils.resetMessage(foHttpSession);
		String lsRedirect = "redirect:/Home?circle=" + GlobalValues.getCompanyCodeEnc(foHttpSession);

		return new ModelAndView(lsRedirect);
	}

	@RequestMapping(value = "/PortalSignOut", method = RequestMethod.GET)
	public ModelAndView portalSignOut(HttpSession foHttpSession) {
		AlertUtils.resetMessage(foHttpSession);

		String lsRedirect = "redirect:/?circle=" + GlobalValues.getCompanyCodeEnc(foHttpSession);
		foHttpSession.invalidate();
		return new ModelAndView(lsRedirect);
	}

	
	
}
