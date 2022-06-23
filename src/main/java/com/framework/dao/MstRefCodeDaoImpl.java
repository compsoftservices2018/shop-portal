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

import com.framework.model.MstRefCode;
import com.framework.model.MstRefCodeKey;
import com.framework.utils.AppUtils;
import com.framework.utils.FrameworkConstants;

@Repository("MstRefCodeDao")
public class MstRefCodeDaoImpl implements MstRefCodeDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void addRefCode(HttpSession foHttpSession, Session foSession, MstRefCode foRefCode) {
		//Session loSession = sessionFactory.getCurrentSession();
		setCommonAttributes(foHttpSession, foRefCode);
		foSession.saveOrUpdate(foRefCode);
	}

	private void obectMapper(MstRefCode foMstRefCode, Map<String, Object> foResult, HttpSession foHttpSession) {
		foMstRefCode.getEmbAudit().setObject_mode(FrameworkConstants.OBJECT_MODE_UPDATE);
		setCommonAttributes(foHttpSession, foMstRefCode);
		foMstRefCode.getEmbAudit().setCreated_by((String) foResult.get("created_by"));
		foMstRefCode.getEmbAudit().setCreated_date((Timestamp) foResult.get("created_date"));
		foMstRefCode.getMstRefCodeKey().setRef_group((String) foResult.get("ref_group"));
		foMstRefCode.getMstRefCodeKey().setRef_code((String) foResult.get("ref_code"));
		foMstRefCode.setRef_name((String) foResult.get("ref_name"));
		foMstRefCode.setStatus((String) foResult.get("status"));
	}

	public MstRefCode getRefCode(HttpSession foHttpSession, MstRefCodeKey foRefCodeKey) {
		MstRefCode loRefCode = new MstRefCode();
		List<Map<String, Object>> lsReturn;
		String lsSelect = "SELECT *  FROM REF_CODES WHERE REF_GROUP = ? AND REF_CODE = ? ";

		try {
			lsReturn = jdbcTemplate.queryForList(lsSelect,
					new Object[] { foRefCodeKey.getRef_group(), foRefCodeKey.getRef_code() });

			if (lsReturn.size() <= 0) {
				return null;
			}
			obectMapper(loRefCode, lsReturn.get(0), foHttpSession);
			return loRefCode;
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public List<Map<String, Object>> getAllRefCodes(HttpSession foHttpSession, String fsRefGroup) {
		List<Map<String, Object>> lsReturn;
		String lsSelect = "SELECT *  FROM REF_CODES WHERE REF_GROUP = ?";

		try {
			lsReturn = jdbcTemplate.queryForList(lsSelect, new Object[] { fsRefGroup });

			if (lsReturn.size() <= 0) {
				return null;
			}

		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
		return lsReturn;
	}

	private void setCommonAttributes(HttpSession foHttpSession, MstRefCode foRefCode) {
		if (foRefCode.getEmbAudit().getObject_mode().equals(FrameworkConstants.OBJECT_MODE_NEW)) {
			foRefCode.getEmbAudit().setCreated_by(AppUtils.getLoggedInUser(foHttpSession));
			foRefCode.getEmbAudit().setCreated_date(AppUtils.getCurrentTimestamp());
		}
		foRefCode.getEmbAudit().setUpdated_by(AppUtils.getLoggedInUser(foHttpSession));
		foRefCode.getEmbAudit().setUpdated_date(AppUtils.getCurrentTimestamp());

	}
}
