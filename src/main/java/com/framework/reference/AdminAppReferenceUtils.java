package com.framework.reference;


import com.framework.reference.ReferenceUtils;
import com.framework.utils.GlobalValues;

import javax.servlet.http.HttpSession;
import org.springframework.jdbc.core.JdbcTemplate;

public class AdminAppReferenceUtils {
	public static void setAppReferenceDataOnLogin(HttpSession fsSession, JdbcTemplate jdbcTemplate) {

		try {
			ReferenceUtils.setReferenceData(fsSession, jdbcTemplate);
			ReferenceUtils.setReferenceDataCompany(fsSession, jdbcTemplate, GlobalValues.getCompanyCode(fsSession));
			ReferenceUtils.setReferenceDataUsers(fsSession, jdbcTemplate);
			ReferenceUtils.setReferenceDataModule(fsSession, jdbcTemplate);

		} catch (Exception E) {
			E.printStackTrace();
		}
	}

	public static void setAppReferenceDataForAdmin(HttpSession fsSession, JdbcTemplate jdbcTemplate) {

		try {
			
		} catch (Exception E) {
			E.printStackTrace();
		}
	}

	public static void setAppReferenceDataBeforeLogin(HttpSession fsSession, JdbcTemplate jdbcTemplate) {

		try {
			ReferenceUtils.setReferenceDataAdminConfig(fsSession, jdbcTemplate); 
			ReferenceUtils.setReferenceData(fsSession, jdbcTemplate);

		} catch (Exception E) {
			E.printStackTrace();
		}
	}

}
