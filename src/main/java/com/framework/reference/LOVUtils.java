package com.framework.reference;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.framework.utils.AppUtils;
import com.framework.utils.FrameworkConstants;


public class LOVUtils {

	public static void buildLOV(HttpSession session, String lsKey) {
	
		// Match In doc numbers
		String lsLOV = FrameworkConstants.EMPTY;
		String lsCurrentrow = FrameworkConstants.EMPTY;
		List<Map<String, Object>> loLovData = (List<Map<String, Object>>) session.getAttribute(lsKey);
		if (loLovData != null) {
			for (Map<String, Object> loRow : loLovData) {
	
				lsCurrentrow = buildLOVRow((String) loRow.get("LOV_CODE"),
						(String) loRow.get("LOV_VALUE"), (String) loRow.get("LOV_DATAKEY"));
				lsLOV = lsLOV + lsCurrentrow;
			}
			session.setAttribute("LOV_" +lsKey , lsLOV);
		}
	
	}

	public static String buildDynamicLOV(HttpSession session, String lsKey,
			List<Map<String, Object>> foLovData) {
		
		// Match In doc numbers
		String lsLOV = FrameworkConstants.EMPTY;
		String lsCurrentrow = FrameworkConstants.EMPTY;
		//List<Map<String, Object>> loLovData = (List<Map<String, Object>>) session.getAttribute(lsKey);
		if (foLovData != null) {
			for (Map<String, Object> loRow : foLovData) {
	
				lsCurrentrow = buildLOVRow((String) loRow.get("LOV_CODE"),
						(String) loRow.get("LOV_VALUE"), (String) loRow.get("LOV_DATAKEY"));
				lsLOV = lsLOV + lsCurrentrow;
			}
		}
		if (!AppUtils.isValueEmpty(lsKey))
		{
			session.setAttribute("LOV_" +lsKey , lsLOV);
		}
		return  lsLOV;
	}
	
	public static String buildLOVRow(String fsCode, String fsValue, String fsDataKey ) {
		return "<tr role='row' class='odd'>"
				+ "<td class='sorting_1'><button class='btn btn-link selectvalue p-0' type='submit' value='" + fsCode
				+ "' value_name='" + fsValue + "'>" + fsCode + "</button></td>" 
				+ "<td style='white-space: pre-wrap;'>"
				+ fsValue + "</td>" 
				+ "<td class='d-none' id='lvl_datakey'>"
				+ fsDataKey + "</td>" 
				+ "</tr>";
	}
	
}
