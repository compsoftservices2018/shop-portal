package com.framework.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.framework.model.MstRefCode;
import com.framework.model.MstRefCodeKey;
import com.framework.service.MstRefCodeService;
import com.framework.utils.AlertUtils;
import com.framework.utils.FrameworkConstants;

@Controller
public class RefCodeController extends SuperController {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private MstRefCodeService MstRefCodeService;

	@RequestMapping(value = "/AddRefCode", method = RequestMethod.GET)
	public ModelAndView addRefCode(@ModelAttribute("MstRefCode") MstRefCode loRefCode, String ref_group,
			HttpSession foHttpSession) {
		Map<String, Object> model = new HashMap<String, Object>();
		loRefCode = new MstRefCode();
		loRefCode.getMstRefCodeKey().setRef_group(ref_group);
		model.put("MstRefCode", loRefCode);
		model.put("MstRefCodeList", MstRefCodeService.getAllRefCodes(foHttpSession, ref_group));
		return new ModelAndView("/Admin/Main_Reference", model);
	}

	@RequestMapping(value = "/SaveRefCode", method = RequestMethod.POST)
	public ModelAndView saveRefCode(@ModelAttribute("MstRefCode") MstRefCode foMstRefCode, BindingResult foResult,
			HttpSession foHttpSession, HttpServletRequest foHttpRequest) {
		Map<String, Object> model = new HashMap<String, Object>();

		if (foMstRefCode.getEmbAudit().getObject_mode().equals(FrameworkConstants.OBJECT_MODE_NEW) && (MstRefCodeService
				.getRefCode(foHttpSession, new MstRefCodeKey(foMstRefCode.getMstRefCodeKey().getRef_group(),
						foMstRefCode.getMstRefCodeKey().getRef_code())) != null)) {
			AlertUtils.addError("Record already exists with same code.", foHttpSession);
			model.put("MstRefCode", foMstRefCode);
			model.put("MstRefCodeList",
					MstRefCodeService.getAllRefCodes(foHttpSession, foMstRefCode.getMstRefCodeKey().getRef_group()));
			return new ModelAndView("/Admin/Main_Reference", model);
		}
		Session loSession = startTransation(sessionFactory);

		AlertUtils.validateModel(foMstRefCode, foHttpSession);
		if (!AlertUtils.hasAppErrors(foHttpSession)) {
			MstRefCodeService.addRefCode(foHttpSession, loSession, foMstRefCode);
			if (commit(loSession.getTransaction(), loSession, foHttpSession)) {
				foMstRefCode.getEmbAudit().setObject_mode(FrameworkConstants.OBJECT_MODE_UPDATE);
			}
		}
		model.put("MstRefCode", foMstRefCode);
		model.put("MstRefCodeList",
				MstRefCodeService.getAllRefCodes(foHttpSession, foMstRefCode.getMstRefCodeKey().getRef_group()));
		return new ModelAndView("/Admin/Main_Reference", model);
	}

	@RequestMapping(value = "/GetRefCode", method = RequestMethod.POST)
	public ModelAndView getRefCode(@ModelAttribute("MstRefCode") MstRefCode foMstRefCode, BindingResult foResult,
			String ref_group, String ref_code, HttpSession foHttpSession, HttpServletRequest foHttpRequest) {
		Map<String, Object> model = new HashMap<String, Object>();
		MstRefCodeKey loRefCodeKey = new MstRefCodeKey(ref_group, ref_code);
		MstRefCode loRefCode = MstRefCodeService.getRefCode(foHttpSession, loRefCodeKey);
		model.put("MstRefCode", loRefCode);
		model.put("MstRefCodeList", MstRefCodeService.getAllRefCodes(foHttpSession, ref_group));
		return new ModelAndView("/Admin/Main_Reference", model);
	}

}
