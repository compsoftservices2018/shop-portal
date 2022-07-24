package com.framework.utils;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/*import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;*/
import org.springframework.jdbc.core.JdbcTemplate;

import com.framework.reference.ReferenceUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;

/**
 * @author Pradeep Chawadkar
 *
 */

public class AlertUtils {


	public static void addError(String fsError, HttpSession foHttpSession) {
		ArrayList<String> loErrorList = (ArrayList<String>) foHttpSession.getAttribute("ERRORS");
		if (loErrorList == null) {
			loErrorList = new ArrayList<String>();
		}
		loErrorList.add(fsError);
		foHttpSession.setAttribute("ERRORS", loErrorList);
	}

	public static void addInfo(String fsInfo, HttpSession foHttpSession) {
		ArrayList<String> loInfoList = (ArrayList<String>) foHttpSession.getAttribute("INFO");
		if (loInfoList == null) {
			loInfoList = new ArrayList<String>();
		}
		loInfoList.add(fsInfo);
		foHttpSession.setAttribute("INFO", loInfoList);
	}

	public static void addSuccess(String fsInfo, HttpSession foHttpSession) {
		ArrayList<String> loSuccessList = (ArrayList<String>) foHttpSession.getAttribute("SUCCESS");
		if (loSuccessList == null) {
			loSuccessList = new ArrayList<String>();
		}
		loSuccessList.add(fsInfo);
		foHttpSession.setAttribute("SUCCESS", loSuccessList);
	}

	public static boolean hasAppErrors(HttpSession foHttpSession) {
		if ((ArrayList<String>) foHttpSession.getAttribute("ERRORS") == null
				|| (((ArrayList<String>) foHttpSession.getAttribute("ERRORS")).size() == 0)) {
			return false;
		}
		return true;
	}

	public static boolean hasAppInfo(HttpSession foHttpSession) {
		if ((ArrayList<String>) foHttpSession.getAttribute("INFO") == null
				|| (((ArrayList<String>) foHttpSession.getAttribute("INFO")).size() == 0)) {
			return false;
		}
		return true;
	}

	public static boolean hasAppSuccess(HttpSession foHttpSession) {
		if ((ArrayList<String>) foHttpSession.getAttribute("SUCCESS") == null
				|| (((ArrayList<String>) foHttpSession.getAttribute("SUCCESS")).size() == 0)) {
			return false;
		}
		return true;
	}

	public static void validateModel(Object foObject, HttpSession foHttpSession) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set inputErrors = validator.validate(foObject);
		Iterator iterator = inputErrors.iterator();
		while (iterator.hasNext()) {
			ConstraintViolation setElement = (ConstraintViolation) iterator.next();
			AlertUtils.addError(setElement.getMessage(), foHttpSession);
		}
	}
	
	public static String validateModel(Object foObject) {
		String lsErrors = "";
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set inputErrors = validator.validate(foObject);
		Iterator iterator = inputErrors.iterator();
		while (iterator.hasNext()) {
			ConstraintViolation setElement = (ConstraintViolation) iterator.next();
			lsErrors = lsErrors + setElement.getMessage() + "<br>";
		}
		return lsErrors;
	}

	public static void resetMessage(HttpSession foHttpSession) {
		foHttpSession.setAttribute("ERRORS", null);
		foHttpSession.setAttribute("INFO", null);
		foHttpSession.setAttribute("SUCCESS", null);
	}

	public static String getErrors(HttpSession foHttpSession) {
		ArrayList<String> loErrors = (ArrayList<String>) foHttpSession.getAttribute("ERRORS");
		String lsErrors = "";
		if (loErrors != null && loErrors.size() != 0) {

			for (int i = 0; i < loErrors.size(); i++) {
				lsErrors = lsErrors + (String) loErrors.get(i) + "<br>";
			}
		}
		return lsErrors;
	}

}
