package com.compsoft.shop.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.compsoft.shop.model.MstCustomer;
import com.compsoft.shop.model.MstCustomerKey;
import com.framework.security.Security;

/**
 * @author Pradeep Chawadkar
 *
 */
@Repository("MstCustomerDao")
public class MstCustomerDaoImpl implements MstCustomerDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private void obectMapper(MstCustomer loMstCustomer, Map<String, Object> foResult) {
		loMstCustomer.getMstCustomerKey().setCompany_code((String) foResult.get("company_code"));
		loMstCustomer.getMstCustomerKey().setCustomer_code((String) foResult.get("Customer_code"));
		loMstCustomer.setAddress((String) foResult.get("address"));
		loMstCustomer.setEmail((String) foResult.get("email"));
		loMstCustomer.setGstin_no((String) foResult.get("gstin_no"));
		loMstCustomer.setMobile((String) foResult.get("mobile"));
		loMstCustomer.setPin((String) foResult.get("pin"));
		loMstCustomer.setStatus((String) foResult.get("status"));
		loMstCustomer.setCustomer_name((String) foResult.get("customer_name"));
		loMstCustomer.setCustomer_type((String) foResult.get("customer_type"));
		loMstCustomer.setPassword(Security.decrypt((String) foResult.get("password")));
		loMstCustomer.setTpt_password((String) foResult.get("tpt_password"));
			loMstCustomer.setTemp_password(Security.decrypt((String) foResult.get("temp_password")));
		loMstCustomer.setDelivery_pins((String) foResult.get("delivery_pins"));
		loMstCustomer.setParent_customer_code((String) foResult.get("parent_customer_code"));
		loMstCustomer.setAlt_customer_code((String) foResult.get("alt_customer_code"));
		
		// loMstCustomer.setGroup_name((String) foResult.get("group_name"));

	}

	public void addCustomer(Session foSession, MstCustomer foVendor) {
		//Session loSession = sessionFactory.getCurrentSession();
		foSession.saveOrUpdate(foVendor);
	}

	public MstCustomer getCustomer(MstCustomerKey foCustomerKey) {
		MstCustomer loMstCustomer = new MstCustomer();
		List<Map<String, Object>> lsReturn;

		String lsSelect = "SELECT * FROM MST_ONLINE_CUSTOMER WHERE COMPANY_CODE = ? AND CUSTOMER_CODE = ?  ";

		try {
			lsReturn = jdbcTemplate.queryForList(lsSelect,
					new Object[] { foCustomerKey.getCompany_code(), foCustomerKey.getCustomer_code() });

			if (lsReturn.size() <= 0) {
				return null;
			}
			obectMapper(loMstCustomer, lsReturn.get(0));
			return loMstCustomer;
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public MstCustomer getCustomer(MstCustomer foCustomer) {
		MstCustomer loMstCustomer = new MstCustomer();
		List<Map<String, Object>> lsReturn;

		String lsSelect = "SELECT * FROM MST_ONLINE_CUSTOMER WHERE "
				+ " COMPANY_CODE = ? " 
				+ " AND (CUSTOMER_CODE = ? "
				+ " OR MOBILE = ? "
				+ " OR EMAIL = ?) ";

		try {
			lsReturn = jdbcTemplate.queryForList(lsSelect,
					new Object[] { foCustomer.getMstCustomerKey().getCompany_code(), 
							foCustomer.getMstCustomerKey().getCustomer_code(), 
							foCustomer.getMobile(),
							foCustomer.getEmail()});

			if (lsReturn.size() != 1) {
				return null;
			}
			obectMapper(loMstCustomer, lsReturn.get(0));
			return loMstCustomer;
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	

	public List<Map<String, Object>> getCustomerList(MstCustomer foCustomer) {
		List<Map<String, Object>> lsReturn;
		String lsSelect = "SELECT * FROM MST_ONLINE_CUSTOMER WHERE COMPANY_CODE = ?";

		try {
			lsReturn = jdbcTemplate.queryForList(lsSelect,
					new Object[] { foCustomer.getMstCustomerKey().getCompany_code() });

			if (lsReturn.size() <= 0) {
				return null;
			}
			return lsReturn;
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}

	}

}
