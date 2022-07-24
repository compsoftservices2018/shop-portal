package com.framework.utils;

import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
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

import com.framework.model.MstCompany;
import com.framework.model.MstUser;
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

public class GlobalValues {

	public static String getCompanyName(HttpSession fsSession) {
		MstCompany loCompanyObject = (MstCompany) fsSession.getAttribute("COMPANYOBJ");
		if (loCompanyObject != null) {
			return loCompanyObject.getCompany_name();
		}
		return FrameworkConstants.EMPTY;
	}
	public static String getCompanyShortName(HttpSession fsSession) {
		MstCompany loCompanyObject = (MstCompany) fsSession.getAttribute("COMPANYOBJ");
		if (loCompanyObject != null) {
			return loCompanyObject.getShort_name();
		}
		return FrameworkConstants.EMPTY;
	}
	
	
	public static String getCompanyCode(HttpSession fsSession) {
		MstCompany loCompanyObject = (MstCompany) fsSession.getAttribute("COMPANYOBJ");
		if (loCompanyObject != null) {
			return loCompanyObject.getMstCompanyKey().getCompany_code();
		}
		return FrameworkConstants.EMPTY;
	}

	public static String getUserName(HttpSession fsSession) {
		MstUser loMstUser = (MstUser) fsSession.getAttribute("USEROBJ");
		if (loMstUser != null) {
			return loMstUser.getUser_name();
		}
		return FrameworkConstants.EMPTY;
	}
	
	public static String getFiscalYear(HttpSession fsSession) {
		return (String) fsSession.getAttribute("FISCAL_YEAR");
	}
	
	public static String getUserCode(HttpSession fsSession) {
		MstUser loMstUser = (MstUser) fsSession.getAttribute("USEROBJ");
		if (loMstUser != null) {
			return loMstUser.getMstUserKey().getUser_code();
		}
		return FrameworkConstants.EMPTY;
	}
	
	public static String getVendorCode(HttpSession fsSession) {
		MstUser loMstUser = (MstUser) fsSession.getAttribute("USEROBJ");
		if (loMstUser != null) {
			return loMstUser.getVendor_code();
		}
		return FrameworkConstants.EMPTY;
	}

	public static String getUserRole(HttpSession fsSession) {
		MstUser loMstUser = (MstUser) fsSession.getAttribute("USEROBJ");
		if (loMstUser != null) {
			return ReferenceUtils.getOptionValue(fsSession, FrameworkConstants.GROUP_USER_ROLE,
					loMstUser.getUser_type());
		}
		return FrameworkConstants.EMPTY;
	}

	public static String getCompanyGst(HttpSession fsSession) {
		MstCompany loCompanyObject = (MstCompany) fsSession.getAttribute("COMPANYOBJ");
		if (loCompanyObject != null) {
			return loCompanyObject.getGstin_no();
		}
		return FrameworkConstants.EMPTY;
	}
	
	public static String getCompanyCodeEnc(HttpSession fsSession) {
		MstCompany loCompanyObject = (MstCompany) fsSession.getAttribute("COMPANYOBJ");
		if (loCompanyObject != null) {
			return loCompanyObject.getComp_key();
		}
		return FrameworkConstants.EMPTY;
	}
	
	public static Timestamp getCurrentDateTime() {
		return AppUtils.getCurrentTimestamp();
	}
	
}
