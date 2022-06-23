package com.compsoft.shop.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.compsoft.shop.model.MstCustomer;
import com.compsoft.shop.model.MstCustomerKey;
import com.framework.dao.SuperDao;;

/**
 * @author Pradeep Chawadkar
 *
 */
public interface MstCustomerDao extends SuperDao {
	
	public void addCustomer(Session foSession, MstCustomer foCustomer);

	public MstCustomer getCustomer(MstCustomerKey foCustomerKey);
	
	public List<Map<String, Object>> getCustomerList(MstCustomer foCustomer);
	
	
	public MstCustomer getCustomer(MstCustomer foCustomer);
	 
	}
