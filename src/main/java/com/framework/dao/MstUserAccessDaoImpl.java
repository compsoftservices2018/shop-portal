package com.framework.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.framework.model.MstUserAccess;
import com.framework.model.MstUserAccessKey;
import com.framework.utils.AppUtils;
import com.framework.utils.FrameworkConstants;

@Repository("MstUserAccessDao")
public class MstUserAccessDaoImpl implements MstUserAccessDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private void obectMapper(MstUserAccess loMstUserAccess, Map<String, Object> foResult, HttpSession foHttpSession) {
		loMstUserAccess.getEmbAudit().setObject_mode(FrameworkConstants.OBJECT_MODE_UPDATE);
		loMstUserAccess.getEmbAudit().setCreated_by((String) foResult.get("created_by"));
		loMstUserAccess.getEmbAudit().setCreated_date((Timestamp) foResult.get("created_date"));
		loMstUserAccess.getMstUserAccessKey().setCompany_code((String) foResult.get("company_code"));
		loMstUserAccess.getMstUserAccessKey().setUser_code((String) foResult.get("user_code"));
		loMstUserAccess.getMstUserAccessKey().setModule_code((String) foResult.get("module_code"));
		loMstUserAccess.setModule_name((String) foResult.get("module_name"));
		loMstUserAccess.setShow((String) foResult.get("show"));
		loMstUserAccess.setAddition((String) foResult.get("addition"));
		loMstUserAccess.setUpdation((String) foResult.get("updatation"));
		loMstUserAccess.setDeletion((String) foResult.get("deletion"));
		loMstUserAccess.setApproval((String) foResult.get("approval"));
		setCommonAttributes(foHttpSession, loMstUserAccess);
	}

	public void addUserAccess(HttpSession foHttpSession, Session foSession, MstUserAccess foUserAccess) {
		//Session loSession = sessionFactory.getCurrentSession();
		setCommonAttributes(foHttpSession, foUserAccess);
		foSession.saveOrUpdate(foUserAccess);
	}

	public MstUserAccess getUserAccess(HttpSession foHttpSession, MstUserAccessKey foUserAccessKey) {
		MstUserAccess loMstUserAccess = new MstUserAccess();
		List<Map<String, Object>> lsReturn;
		String lsSelect = "SELECT * FROM MST_USER_ACCESS WHERE COMPANY_CODE = ? AND USER_CODE = ? AND MODULE_CODE = ?";

		try {
			lsReturn = jdbcTemplate.queryForList(lsSelect, new Object[] { foUserAccessKey.getCompany_code(),
					foUserAccessKey.getUser_code(), foUserAccessKey.getModule_code() });

			if (lsReturn.size() <= 0) {
				return null;
			}
			obectMapper(loMstUserAccess, lsReturn.get(0), foHttpSession);
			return loMstUserAccess;
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public List<Map<String, Object>> getUserAccesssForUser(String fsCompanyCode, String fsUserCode, String fsUserType) {
		List<Map<String, Object>> lsReturn;
		String lsSelect = "SELECT * FROM MST_User WHERE User_CODE = ? ";
		lsReturn = jdbcTemplate.queryForList(lsSelect, new Object[] { fsUserCode });
		lsSelect = "SELECT  MST_USER_ACCESS.COMPANY_CODE, MST_USER_ACCESS.USER_CODE, MST_MODULES.MODULE_CODE MODULE_CODE , MST_USER_ACCESS.MODULE_CODE ACCESS_MODULE_CODE , "
				+ " MST_MODULES.MODULE_NAME, NVL(MST_USER_ACCESS.SHOW,'N') SHOW, NVL(MST_USER_ACCESS.ADDITION,'N') ADDITION, NVL(MST_USER_ACCESS.UPDATION,'N') UPDATION,  "
				+ " NVL(MST_USER_ACCESS.DELETION,'N') DELETION, NVL(MST_USER_ACCESS.APPROVAL,'N') APPROVAL  "
				+ " FROM MST_MODULES LEFT OUTER JOIN MST_USER_ACCESS  ON MST_USER_ACCESS.MODULE_CODE = MST_MODULES.MODULE_CODE  "
				+ " AND (MST_USER_ACCESS.COMPANY_CODE = ? OR MST_USER_ACCESS.COMPANY_CODE IS NULL)  "
				+ " AND (MST_USER_ACCESS.USER_CODE = ? OR MST_USER_ACCESS.USER_CODE IS NULL) "
				+ "  WHERE MODULE_TYPE = 'SMENU' AND MST_MODULES.COMPANY_CODE = ? ORDER BY MST_MODULES.MODULE_CODE";
		try {
			lsReturn = jdbcTemplate.queryForList(lsSelect, new Object[] { fsCompanyCode, fsUserCode, fsCompanyCode });
			if (lsReturn.size() <= 0) {
				return null;
			}
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
		return lsReturn;
	}

	private void setCommonAttributes(HttpSession foHttpSession, MstUserAccess foUserAccess) {
		if (foUserAccess.getEmbAudit().getObject_mode().equals(FrameworkConstants.OBJECT_MODE_NEW)) {
			foUserAccess.getEmbAudit().setCreated_by(AppUtils.getLoggedInUser(foHttpSession));
			foUserAccess.getEmbAudit().setCreated_date(AppUtils.getCurrentTimestamp());
		}
		foUserAccess.getEmbAudit().setUpdated_by(AppUtils.getLoggedInUser(foHttpSession));
		foUserAccess.getEmbAudit().setUpdated_date(AppUtils.getCurrentTimestamp());

	}

}
