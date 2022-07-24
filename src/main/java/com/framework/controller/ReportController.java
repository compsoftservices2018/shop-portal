package com.framework.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import com.framework.reference.ReferenceUtils;

@Controller
public class ReportController extends SuperController {

	@RequestMapping(value = "/Report", method = RequestMethod.GET)
	public RedirectView report(HttpSession foHttpSession, HttpServletRequest foHttpRequest, String id) {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl(ReferenceUtils.getGlobalConfigParamValue(foHttpSession, "COMPSOFT_REPORT_API") + id);
		return redirectView;
	}

}
