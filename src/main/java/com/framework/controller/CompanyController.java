package com.framework.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.framework.model.MstCompany;
import com.framework.model.MstCompanyKey;
import com.framework.reference.ReferenceUtils;
import com.framework.security.Security;
import com.framework.service.MstCompanyService;
import com.framework.utils.AlertUtils;
import com.framework.utils.FrameworkConstants;

@Controller
public class CompanyController extends SuperController {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MstCompanyService MstCompanyService;

	@RequestMapping(value = "/AddCompany", method = RequestMethod.GET)
	public ModelAndView addCompany(@ModelAttribute("MstCompany") MstCompany loCompany, HttpSession foHttpSession) {
		Map<String, Object> model = new HashMap<String, Object>();
		loCompany = new MstCompany();
		model.put("MstCompany", loCompany);
		model.put("MstCompanyList", MstCompanyService.getCompanies(foHttpSession));
		return new ModelAndView("/Admin/Main_Company", model);
	}

	@RequestMapping(value = "/SaveCompany", method = RequestMethod.POST)
	public ModelAndView saveCompany(@ModelAttribute("MstCompany") MstCompany foMstCompany, BindingResult foResult,
			HttpSession foHttpSession, HttpServletRequest foHttpRequest) {
		Map<String, Object> model = new HashMap<String, Object>();

		if (foMstCompany.getEmbAudit().getObject_mode().equals(FrameworkConstants.OBJECT_MODE_NEW) && (MstCompanyService
				.getCompany(new MstCompanyKey(foMstCompany.getMstCompanyKey().getCompany_code())) != null)) {
			AlertUtils.addError("Company record already exists with same company code.", foHttpSession);
			model.put("MstCompany", foMstCompany);
			model.put("MstCompanyList", MstCompanyService.getCompanies(foHttpSession));
			return new ModelAndView("/Admin/Main_Company", model);
		}
		Session loSession = startTransation(sessionFactory);

		foMstCompany.setComp_key(Security.encrypt(foMstCompany.getMstCompanyKey().getCompany_code()).replace('+', 'z'));
		AlertUtils.validateModel(foMstCompany, foHttpSession);
		if (!AlertUtils.hasAppErrors(foHttpSession)) {
			MstCompanyService.addCompany(foHttpSession, loSession, foMstCompany);
			if (commit(loSession.getTransaction(), loSession, foHttpSession)) {
				foMstCompany.getEmbAudit().setObject_mode(FrameworkConstants.OBJECT_MODE_UPDATE);
			}
		}
		ReferenceUtils.setReferenceDataCompany(foHttpSession, jdbcTemplate, null);
		model.put("MstCompany", foMstCompany);
		model.put("MstCompanyList", MstCompanyService.getCompanies(foHttpSession));
		return new ModelAndView("/Admin/Main_Company", model);
	}

	@RequestMapping(value = "/GetCompany", method = RequestMethod.POST)
	public ModelAndView getCompany(@ModelAttribute("MstCompany") MstCompany foMstCompany, BindingResult foResult,
			String company_code, HttpSession foHttpSession, HttpServletRequest foHttpRequest) {
		Map<String, Object> model = new HashMap<String, Object>();
		MstCompanyKey loCompanyKey = new MstCompanyKey(company_code);
		MstCompany loCompany = MstCompanyService.getCompany(loCompanyKey);
		model.put("MstCompany", loCompany);
		model.put("MstCompanyList", MstCompanyService.getCompanies(foHttpSession));
		return new ModelAndView("/Admin/Main_Company", model);
	}

}
