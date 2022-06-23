package com.framework.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.framework.utils.AlertUtils;
import com.framework.utils.FrameworkConstants;

@Controller
public class LOVController extends SuperController {

	@RequestMapping(value = "/GetLOV", method = RequestMethod.GET)
	public @ResponseBody String getLOV(HttpSession foHttpSession, HttpServletRequest foHttpRequest,
			HttpServletResponse foHttpResponse, @RequestParam String requestData) {
		AlertUtils.resetMessage(foHttpSession);
		String loReturn = FrameworkConstants.EMPTY;
		try {
			JSONParser parser = new JSONParser();
			JSONObject requestJson = (JSONObject) parser.parse(requestData);
			String lsLOVType = ((String) requestJson.get("lov_type")).toUpperCase();
			loReturn = (String) foHttpSession.getAttribute("LOV_" + lsLOVType);
			return loReturn;
		} catch (Exception ex) {

			return loReturn;
		}
	}

}
