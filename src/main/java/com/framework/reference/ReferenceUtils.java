package com.framework.reference;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.framework.utils.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import com.framework.model.MstUser;

public class ReferenceUtils {
	public static String getCnfigParamValue(HttpSession foSession, String fsParamName) {

		Map<String, String> loConfigMap = (Map<String, String>) foSession.getAttribute("CONFIG");

		// response.sendRedirect(servletContext.getContextPath()+"/login");
		if (loConfigMap != null) {
			return loConfigMap.get(fsParamName);
		} else {
			return "";
		}
	}

	public static String getGlobalConfigParamValue(HttpSession foSession, String fsParamName) {

		Map<String, String> loConfigMap = (Map<String, String>) foSession.getAttribute("GLOBAL_CONFIG");

		// response.sendRedirect(servletContext.getContextPath()+"/login");
		if (loConfigMap != null) {
			return loConfigMap.get(fsParamName);
		} else {
			return "";
		}
	}

	public static void setReferenceData(HttpSession fsSession, JdbcTemplate jdbcTemplate) {

		try {
			// Load configuration
			List<Map<String, Object>> result = null;

			// Load Reference Data
			Map<String, String> loRefDataMap = new HashMap<String, String>();
			String lsSelect = "SELECT * FROM MST_LOV WHERE STATUS = 'A' ORDER BY REF_NAME, REF_GROUP ";
			result = jdbcTemplate.queryForList(lsSelect);
			for (int i = 0; i < result.size(); i++) {
				Map<String, Object> loRow = result.get(i);
				loRefDataMap.put((String) loRow.get("REF_GROUP") + (String) loRow.get("REF_CODE"),
						(String) loRow.get("REF_NAME"));
			}
			fsSession.setAttribute("REFERENCE", loRefDataMap);
		} catch (Exception E) {
			E.printStackTrace();
		}

	}

	public static String buildOptions(HttpSession session, String fsGroup, String fsValue, boolean fsEmptyValue) {
		Map<String, String> loMapAll = (Map<String, String>) session.getAttribute("REFERENCE");

		String lsReturnOptions = "";

		if (loMapAll == null) {
			return lsReturnOptions;
		}

		Iterator<Map.Entry<String, String>> itr = loMapAll.entrySet().iterator();
		if (fsEmptyValue) {
			lsReturnOptions = "<option value=''></option>";
		}

		while (itr.hasNext()) {
			Map.Entry<String, String> entry = itr.next();
			if (fsGroup.equals(entry.getKey().substring(0, 5))) {
				if (fsValue != null && fsValue.equals(entry.getKey().substring(5, entry.getKey().length()))) {
					lsReturnOptions = lsReturnOptions + "<option value='"
							+ entry.getKey().substring(5, entry.getKey().length()) + "' selected>" + entry.getValue()
							+ "</option>";
				} else {
					lsReturnOptions = lsReturnOptions + "<option value='"
							+ entry.getKey().substring(5, entry.getKey().length()) + "'>" + entry.getValue()
							+ "</option>";
				}

				// + " (" + entry.getKey().substring(5, entry.getKey().length())
				// + ")"
			}
		}
		return lsReturnOptions;
	}

	public static String buildOptionsAdmin(HttpSession session, String fsGroup, String fsValue, boolean fsEmptyValue) {
		Map<String, String> loMapAll = (Map<String, String>) session.getAttribute("REFERENCE_ADMIN");
		Iterator<Map.Entry<String, String>> itr = loMapAll.entrySet().iterator();
		String lsReturnOptions = "";

		if (fsEmptyValue) {
			lsReturnOptions = "<option value=''></option>";
		}

		while (itr.hasNext()) {
			Map.Entry<String, String> entry = itr.next();
			if (fsGroup.equals(entry.getKey().substring(0, 5))) {
				if (fsValue != null && fsValue.equals(entry.getKey().substring(5, entry.getKey().length()))) {
					lsReturnOptions = lsReturnOptions + "<option value='"
							+ entry.getKey().substring(5, entry.getKey().length()) + "' selected>" + entry.getValue()
							+ "</option>";
				} else {
					lsReturnOptions = lsReturnOptions + "<option value='"
							+ entry.getKey().substring(5, entry.getKey().length()) + "'>" + entry.getValue()
							+ "</option>";
				}

				// + " (" + entry.getKey().substring(5, entry.getKey().length())
				// + ")"
			}
		}
		return lsReturnOptions;
	}

	public static String getOptionValue(HttpSession session, String fsGroup, String fsValue) {
		Map<String, String> loMapAll = (Map<String, String>) session.getAttribute("REFERENCE");
		if (loMapAll == null) {
			return FrameworkConstants.EMPTY;
		}
		Iterator<Map.Entry<String, String>> itr = loMapAll.entrySet().iterator();
		String lsReturnOptionsValue = FrameworkConstants.EMPTY;

		if (AppUtils.isValueEmpty(fsValue)) {
			return lsReturnOptionsValue;
		}

		while (itr.hasNext()) {
			Map.Entry<String, String> entry = itr.next();
			if (fsGroup.equals(entry.getKey().substring(0, 5))) {
				if (fsValue.equals(entry.getKey().substring(5, entry.getKey().length()))) {
					return entry.getValue();
				}
			}
		}
		return lsReturnOptionsValue;
	}

	public static String getOptionValueAdmin(HttpSession session, String fsGroup, String fsValue) {
		Map<String, String> loMapAll = (Map<String, String>) session.getAttribute("REFERENCE_ADMIN");
		if (loMapAll == null) {
			return FrameworkConstants.EMPTY;
		}
		Iterator<Map.Entry<String, String>> itr = loMapAll.entrySet().iterator();
		String lsReturnOptionsValue = FrameworkConstants.EMPTY;

		if (AppUtils.isValueEmpty(fsValue)) {
			return lsReturnOptionsValue;
		}

		while (itr.hasNext()) {
			Map.Entry<String, String> entry = itr.next();
			if (fsGroup.equals(entry.getKey().substring(0, 5))) {
				if (fsValue.equals(entry.getKey().substring(5, entry.getKey().length()))) {
					return entry.getValue();
				}
			}
		}
		return lsReturnOptionsValue;
	}



	public static void setReferenceDataUsers(HttpSession fsSession, JdbcTemplate jdbcTemplate) {

		try {
			// Load configuration
			MstUser loMstUser = (MstUser) fsSession.getAttribute("USEROBJ");
			String lsUserType = loMstUser.getUser_type();
			String lsSelect = null;
			List<Map<String, Object>> loResult = null;

			if (lsUserType.equals(FrameworkConstants.USER_TYPE_SUPRADMIN)) {
				lsSelect = "SELECT * FROM MST_USER ORDER BY COMPANY_CODE, USER_CODE";
				loResult = jdbcTemplate.queryForList(lsSelect);
			} else {
				lsSelect = "SELECT * FROM MST_USER WHERE COMPANY_CODE IN (SELECT COMPANY_CODE FROM MST_USER WHERE USER_CODE = ? ) AND USER_CODE <> ? ORDER BY COMPANY_CODE, USER_CODE";
			}
			fsSession.setAttribute("USERS", loResult);

		} catch (Exception E) {
			E.printStackTrace();
		}
	}

	/*
	 * public static void setReferenceDataApplication(HttpSession fsSession,
	 * JdbcTemplate jdbcTemplate) {
	 * 
	 * try { // Load configuration MstUser loMstUser = (MstUser)
	 * fsSession.getAttribute("USEROBJ"); String lsUserType =
	 * loMstUser.getUser_type(); String lsSelect = null; List<Map<String,
	 * Object>> loResult = null; if
	 * (loMstUser.getUser_type().equals(FrameworkConstants.USER_TYPE_SUPRADMIN))
	 * { lsSelect = "SELECT * FROM MST_APPL WHERE APPL_CODE IN ('SADMN')";
	 * loResult = jdbcTemplate.queryForList(lsSelect); } else { lsSelect =
	 * "SELECT * FROM MST_APPL WHERE APPL_CODE IN (SELECT APPL_CODE FROM MST_USER WHERE USER_CODE = ? )"
	 * ; loResult = jdbcTemplate.queryForList(lsSelect, new Object[] {
	 * loMstUser.getMstUserKey().getUser_code() }); }
	 * 
	 * } catch (Exception E) { E.printStackTrace(); } }
	 */

	/*
	 * public static void setReferenceDataGSTProgress(HttpSession fsSession,
	 * JdbcTemplate jdbcTemplate) {
	 * 
	 * try { // Load configuration String lsSelect = null; List<Map<String,
	 * Object>> loResult = null;
	 * 
	 * MstUser loMstUser = (MstUser) fsSession.getAttribute("USEROBJ");
	 * 
	 * lsSelect =
	 * "SELECT * FROM MST_GSTRECON_PROGRESS WHERE COMPANY_CODE = ? AND USER_CODE = ?"
	 * ; loResult = jdbcTemplate.queryForList(lsSelect, new Object[] {
	 * loMstUser.getMstUserKey().getCompany_code(),
	 * loMstUser.getMstUserKey().getUser_code() });
	 * 
	 * if (loResult.size() == 0) {
	 * jdbcTemplate.update("INSERT INTO MST_GSTRECON_PROGRESS VALUES (?,?,?)",
	 * new Object[] { loMstUser.getMstUserKey().getCompany_code(),
	 * loMstUser.getMstUserKey().getUser_code(), new Integer(1) }); }
	 * 
	 * loResult = jdbcTemplate.queryForList(lsSelect, new Object[] {
	 * loMstUser.getMstUserKey().getCompany_code(),
	 * loMstUser.getMstUserKey().getUser_code() });
	 * 
	 * MstGSTReconProgress loGSTRecon = new MstGSTReconProgress();
	 * loGSTRecon.getMstGSTReconProgressKey().setCompany_code((String)
	 * loResult.get(0).get("company_code"));
	 * loGSTRecon.getMstGSTReconProgressKey().setCompany_code((String)
	 * loResult.get(0).get("user_code")); loGSTRecon.setStatus((String)
	 * loResult.get(0).get("status")); fsSession.setAttribute("GSTRECON",
	 * loGSTRecon);
	 * 
	 * } catch (Exception E) { E.printStackTrace(); } }
	 */

	public static void setReferenceCalendar(HttpSession fsSession, JdbcTemplate jdbcTemplate) {

		try {
			// Load configuration
			MstUser loMstUser = (MstUser) fsSession.getAttribute("USEROBJ");
			String lsSelect = null;
			List<Map<String, Object>> loResult = null;

			lsSelect = "SELECT * FROM MST_CALENDAR WHERE COMPANY_CODE = ?";
			loResult = jdbcTemplate.queryForList(lsSelect,
					new Object[] { loMstUser.getMstUserKey().getCompany_code() });
			fsSession.setAttribute("CALENDAR", loResult);

		} catch (Exception E) {
			E.printStackTrace();
		}
	}

	public static void setReferenceDataCompany(HttpSession fsSession, JdbcTemplate jdbcTemplate, String foCompanyCode) {

		try {
			// Load configuration
			String lsSelect = null;
			List<Map<String, Object>> loResult = null;

			if (!AppUtils.isValueEmpty(foCompanyCode)) {
				lsSelect = "SELECT * FROM MST_COMPANY WHERE COMPANY_CODE = ?";
				loResult = jdbcTemplate.queryForList(lsSelect, new Object[] { foCompanyCode });
				fsSession.setAttribute("COMPANY", loResult);
				fsSession.setAttribute("COMPANY_NAME", loResult.get(0).get("company_name"));
				fsSession.setAttribute("TITLE", loResult.get(0).get("company_name"));
				return;
			} else {
				lsSelect = "SELECT * FROM MST_COMPANY";
				loResult = jdbcTemplate.queryForList(lsSelect);
				fsSession.setAttribute("COMPANY", loResult);
				return;
			}

			/*
			 * MstUser loMstUser = (MstUser) fsSession.getAttribute("USEROBJ");
			 * String lsUserType = loMstUser.getUser_type();
			 * 
			 * if (lsUserType.equals(FrameworkConstants.USER_TYPE_SUPRADMIN)) {
			 * lsSelect = "SELECT * FROM MST_COMPANY"; loResult =
			 * jdbcTemplate.queryForList(lsSelect); } else { lsSelect =
			 * "SELECT * FROM MST_COMPANY WHERE COMPANY_CODE IN (SELECT COMPANY_CODE FROM MST_USER WHERE USER_CODE = ?)"
			 * ; loResult = jdbcTemplate.queryForList(lsSelect, new Object[] {
			 * loMstUser.getMstUserKey().getUser_code() }); }
			 */

			/*
			 * if (loMstUser.getUser_type().equals(FrameworkConstants.
			 * USER_TYPE_SUPRADMIN)) { fsSession.setAttribute("COMPANY_NAME",
			 * "Jobwork Administration"); fsSession.setAttribute("TITLE",
			 * "Jobwork Administration"); } else {
			 * fsSession.setAttribute("COMPANY_NAME",
			 * loResult.get(0).get("company_name"));
			 * fsSession.setAttribute("TITLE",
			 * loResult.get(0).get("company_name")); }
			 * fsSession.setAttribute("USERROLE",
			 * ReferenceUtils.getOptionValue(fsSession,
			 * FrameworkConstants.GROUP_USER_ROLE, loMstUser.getUser_type()));
			 */

		} catch (Exception E) {
			E.printStackTrace();
		}
	}

	public static String buildListCompany(HttpSession session, String fsValue, boolean fsEmptyValue) {
		List<Map<String, Object>> loMapCompany = (List<Map<String, Object>>) session.getAttribute("COMPANY");
		String lsReturnOptions = "";

		if (fsEmptyValue) {
			lsReturnOptions = "<option value=''>*All</option>";
		}

		for (Map<String, Object> loCompanyRow : loMapCompany) {
			if (fsValue != null && fsValue.equals((String) loCompanyRow.get("company_code"))) {
				lsReturnOptions = lsReturnOptions + "<option value='" + (String) loCompanyRow.get("company_code")
						+ "' selected>" + (String) loCompanyRow.get("company_name") + "</option>";
			} else {
				lsReturnOptions = lsReturnOptions + "<option value='" + (String) loCompanyRow.get("company_code")
						+ "' >" + (String) loCompanyRow.get("company_name") + "</option>";
			}
		}
		return lsReturnOptions;
	}

	public static String getProductValues(HttpSession session, String fsProductCode, String fsVendorProductCode) {
		List<Map<String, Object>> loMapProductList = null;

		loMapProductList = (List<Map<String, Object>>) session.getAttribute("PRODUCT_LIST_ALL");

		for (Map<String, Object> loProductRow : loMapProductList) {
			if (fsProductCode != null && fsProductCode.equals((String) loProductRow.get("product_code"))
					|| fsVendorProductCode != null
							&& fsVendorProductCode.equals((String) loProductRow.get("vendor_product_code"))) {

				GsonBuilder gsonMapBuilder = new GsonBuilder();

				Gson gsonObject = gsonMapBuilder.create();

				return gsonObject.toJson(loProductRow);

			}
		}
		return null;
	}

	public static void setReferenceDataModule(HttpSession fsSession, JdbcTemplate jdbcTemplate) {

		try {
			// Load configuration
			MstUser loMstUser = (MstUser) fsSession.getAttribute("USEROBJ");
			String lsUserType = loMstUser.getUser_type();
			String lsSql1 = FrameworkConstants.EMPTY;
			String lsSql2 = FrameworkConstants.EMPTY;
			String lsCompanyCode = FrameworkConstants.EMPTY;

			String lsSelect = null;
			List<Map<String, Object>> loResult = null;

			if (lsUserType.equals(FrameworkConstants.USER_TYPE_SUPRADMIN)) {
				lsSql1 = " COMPANY_CODE = '" + FrameworkConstants.ADMIN_COMPANY_CODE + "'" + " AND MODULE_CODE <> '"
						+ FrameworkConstants.ADMIN_COMPANY_CODE + "'";
				lsCompanyCode = FrameworkConstants.ADMIN_COMPANY_CODE;
			} else if (lsUserType.equals(FrameworkConstants.USER_TYPE_ADMIN)) {
				lsSql1 = " COMPANY_CODE = '" + GlobalValues.getCompanyCode(fsSession) + "'" + " AND MODULE_CODE <> '"
						+ GlobalValues.getCompanyCode(fsSession) + "'";
				lsCompanyCode = GlobalValues.getCompanyCode(fsSession);
			} else {
				lsSql1 = " ( MODULE_CODE IN (SELECT MODULE_CODE FROM MST_USER_ACCESS " + " WHERE USER_CODE = '"
						+ loMstUser.getMstUserKey().getUser_code() + "'" + " AND COMPANY_CODE = '"
						+ GlobalValues.getCompanyCode(fsSession) + "'" + " AND SHOW='Y' )) ";
				lsCompanyCode = GlobalValues.getCompanyCode(fsSession);
			}

			lsSelect = "SELECT MODULE_CODE, MODULE_NAME, URL, PARENT_MODULE_CODE , "
					+ " RPAD('.', (level-1)*2, '.') || MODULE_CODE AS tree, level ,  "
					+ " CONNECT_BY_ROOT MODULE_CODE AS root_id,  "
					+ " LTRIM(SYS_CONNECT_BY_PATH(COMPANY_CODE, '-'), '-') AS path,  " + " CONNECT_BY_ISLEAF AS leaf  "
					+ " FROM   MST_MODULES WHERE " + lsSql1 + " START WITH MODULE_CODE = '" + lsCompanyCode + "'"
					+ " CONNECT BY PARENT_MODULE_CODE = PRIOR MODULE_CODE "
					+ " ORDER  BY PATH, LEVEL desc, MODULE_CODE";

			loResult = jdbcTemplate.queryForList(lsSelect);

			fsSession.setAttribute("MODULES", loResult);
		} catch (Exception E) {
			E.printStackTrace();
		}
	}

	public static void setReferenceDataConfig(HttpSession fsSession, JdbcTemplate jdbcTemplate) {

		try {
			// Load configuration
			Map<String, String> loConfigMap = new HashMap<String, String>();
			String lsSelect = "SELECT * FROM MST_CONFIG WHERE COMPANY_CODE = ? ";
			List<Map<String, Object>> result = jdbcTemplate.queryForList(lsSelect,
					new Object[] { GlobalValues.getCompanyCode(fsSession) });
			for (int i = 0; i < result.size(); i++) {
				Map<String, Object> loRow = result.get(i);
				loConfigMap.put((String) loRow.get("PARAM_CD"), (String) loRow.get("PARAM_VALUE"));
			}

			fsSession.setAttribute("CONFIG", loConfigMap);
		} catch (Exception E) {
			E.printStackTrace();
		}
	}

	public static void setReferenceDataAdminConfig(HttpSession fsSession, JdbcTemplate jdbcTemplate) {

		try {
			// Load configuration
			Map<String, String> loConfigMap = new HashMap<String, String>();
			String lsSelect = "SELECT * FROM MST_CONFIG WHERE COMPANY_CODE = 'ALL' ";
			List<Map<String, Object>> result = jdbcTemplate.queryForList(lsSelect);
			for (int i = 0; i < result.size(); i++) {
				Map<String, Object> loRow = result.get(i);
				loConfigMap.put((String) loRow.get("PARAM_CD"), (String) loRow.get("PARAM_VALUE"));
			}

			fsSession.setAttribute("GLOBAL_CONFIG", loConfigMap);
		} catch (Exception E) {
			E.printStackTrace();
		}
	}

	public static List<Map<String, Object>> getImportExportParams(HttpSession fsSession, JdbcTemplate jdbcTemplate,
			String fsProcess) {

		try {

			String lsSelect = "SELECT * FROM MST_IMPORTEXPORT WHERE COMPANY_CODE = ?  AND PROCESS_NAME = ? ORDER BY CLIENT_ATTRIBUTE";
			List<Map<String, Object>> result = jdbcTemplate.queryForList(lsSelect,
					new Object[] { GlobalValues.getCompanyCode(fsSession), fsProcess });
			if (result.size() == 0) {
				return null;
			}
			return result;
		} catch (Exception E) {
			E.printStackTrace();
			return null;
		}
	}

}
