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

import com.framework.model.MstCompany;
import com.framework.model.MstCompanyKey;
import com.framework.reference.ReferenceUtils;
import com.framework.utils.AppUtils;
import com.framework.utils.FrameworkConstants;

@Repository("MstCompanyDao")
public class MstCompanyDaoImpl implements MstCompanyDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private void obectMapper(MstCompany loMstCompany, Map<String, Object> foResult) {
		loMstCompany.getEmbAudit().setObject_mode(FrameworkConstants.OBJECT_MODE_UPDATE);
		loMstCompany.getEmbAudit().setCreated_by((String) foResult.get("created_by"));
		loMstCompany.getEmbAudit().setCreated_date((Timestamp) foResult.get("created_date"));
		loMstCompany.getMstCompanyKey().setCompany_code((String) foResult.get("company_code"));
		loMstCompany.setAddress((String) foResult.get("address"));
		loMstCompany.setCity((String) foResult.get("city"));
		loMstCompany.setCompany_name((String) foResult.get("company_name"));
		loMstCompany.setShort_name((String) foResult.get("short_name"));
		loMstCompany.setContact_person((String) foResult.get("contact_person"));
		loMstCompany.setCountry((String) foResult.get("country"));
		loMstCompany.setEmail((String) foResult.get("email"));
		loMstCompany.setFax((String) foResult.get("fax"));
		loMstCompany.setGst_regn_type((String) foResult.get("gst_regn_type"));
		loMstCompany.setGstin_no((String) foResult.get("gstin_no"));
		loMstCompany.setMobile((String) foResult.get("mobile"));
		loMstCompany.setPan((String) foResult.get("pan"));
		loMstCompany.setPeriodicity_gstr((String) foResult.get("periodicity_gstr"));
		loMstCompany.setPin((String) foResult.get("pin"));
		loMstCompany.setState((String) foResult.get("state"));
		loMstCompany.setStatus((String) foResult.get("status"));
		loMstCompany.setTelephone((String) foResult.get("telephone"));
		loMstCompany.setWebsite((String) foResult.get("website"));
		loMstCompany.setComp_key((String) foResult.get("comp_key"));
	}

	public void addCompany(HttpSession foHttpSession, Session foSession, MstCompany foCompany) {
		//Session foSession = sessionFactory.getCurrentSession();
		setCommonAttributes(foHttpSession, foCompany);
		foSession.saveOrUpdate(foCompany);
		ReferenceUtils.setReferenceDataCompany(foHttpSession, jdbcTemplate, null);
	}

	public MstCompany getCompany(MstCompanyKey foCompanyKey) {
		MstCompany loMstCompany = new MstCompany();
		List<Map<String, Object>> lsReturn;
		String lsSelect = "SELECT * FROM MST_Company WHERE COMPANY_CODE = ? ";

		try {
			lsReturn = jdbcTemplate.queryForList(lsSelect, new Object[] { foCompanyKey.getCompany_code() });

			if (lsReturn.size() <= 0) {
				return null;
			}
			obectMapper(loMstCompany, lsReturn.get(0));
			return loMstCompany;
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public MstCompany getCompanyByKey(String fsKey) {
		MstCompany loMstCompany = new MstCompany();
		List<Map<String, Object>> lsReturn;
		String lsSelect = "SELECT * FROM MST_Company WHERE COMP_KEY = ? ";

		try {
			lsReturn = jdbcTemplate.queryForList(lsSelect, new Object[] { fsKey });

			if (lsReturn.size() <= 0) {
				return null;
			}
			obectMapper(loMstCompany, lsReturn.get(0));
			return loMstCompany;
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public List<Map<String, Object>> getCompanies(HttpSession foHttpSession) {

		return (List<Map<String, Object>>) foHttpSession.getAttribute("COMPANY");
	}

	private void setCommonAttributes(HttpSession foHttpSession, MstCompany foCompany) {
		if (foCompany.getEmbAudit().getObject_mode().equals(FrameworkConstants.OBJECT_MODE_NEW)) {
			foCompany.getEmbAudit().setCreated_by(AppUtils.getLoggedInUser(foHttpSession));
			foCompany.getEmbAudit().setCreated_date(AppUtils.getCurrentTimestamp());
		}

	}
}
