package com.framework.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.framework.model.MstUser;
import com.framework.model.MstUserKey;
import com.framework.security.Security;
import com.framework.utils.AppUtils;
import com.framework.utils.FrameworkConstants;

@Repository("MstUserDao")
public class MstUserDaoImpl implements MstUserDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private void obectMapper(MstUser loMstUser, Map<String, Object> foResult) {
		loMstUser.getEmbAudit().setObject_mode(FrameworkConstants.OBJECT_MODE_UPDATE);
		loMstUser.getEmbAudit().setCreated_by((String) foResult.get("created_by"));
		loMstUser.getEmbAudit().setCreated_date((Timestamp) foResult.get("created_date"));
		loMstUser.getMstUserKey().setCompany_code((String) foResult.get("company_code"));
		loMstUser.getMstUserKey().setUser_code((String) foResult.get("user_code"));
		loMstUser.setEmail((String) foResult.get("email"));
		loMstUser.setPassword(Security.decrypt((String) foResult.get("password")));
		loMstUser.setStatus((String) foResult.get("status"));
		loMstUser.setUser_name((String) foResult.get("user_name"));
		loMstUser.setUser_type((String) foResult.get("user_type"));
		loMstUser.setVendor_code((String) foResult.get("vendor_code"));
	}

	public void addUser(Session foSession, MstUser foUser) {
		//Session loSession = sessionFactory.getCurrentSession();
		foSession.saveOrUpdate(foUser);
	}

	public MstUser getUser(MstUser foUser) {
		MstUser loMstUser = new MstUser();
		List<Map<String, Object>> lsReturn;
		String lsSelect = "SELECT * FROM MST_USER WHERE " + " (COMPANY_CODE=? OR ' '=?) "
				+ " AND (USER_CODE = ? OR ' ' = ?) " + " AND (USER_TYPE = ? OR ' ' = ?)";

		try {
			lsReturn = jdbcTemplate.queryForList(lsSelect,
					new Object[] { AppUtils.convertNullToSpace(foUser.getMstUserKey().getCompany_code()),
							AppUtils.convertNullToSpace(foUser.getMstUserKey().getCompany_code()),
							AppUtils.convertNullToSpace(foUser.getMstUserKey().getUser_code()),
							AppUtils.convertNullToSpace(foUser.getMstUserKey().getUser_code()),
							AppUtils.convertNullToSpace(foUser.getUser_type()),
							AppUtils.convertNullToSpace(foUser.getUser_type()) });

			if (lsReturn.size() != 1) {
				return null;
			}
			obectMapper(loMstUser, lsReturn.get(0));
			return loMstUser;
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public List<Map<String, Object>> getUsers(MstUserKey foUserKey) {
		String lsSelect = "SELECT * FROM MST_USER WHERE COMPANY_CODE=? OR ' ' = ? ";
		List<Map<String, Object>> lsReturn = null;
		try {
			lsReturn = jdbcTemplate.queryForList(lsSelect,
					new Object[] { AppUtils.convertNullToSpace(foUserKey.getCompany_code()),
							AppUtils.convertNullToSpace(foUserKey.getCompany_code()) });

			if (lsReturn.size() <= 0) {
				return null;
			}
			return lsReturn;
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

}
