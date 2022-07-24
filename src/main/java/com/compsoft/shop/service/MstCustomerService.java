package com.compsoft.shop.service;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.compsoft.shop.model.MstCustomer;
import com.compsoft.shop.model.MstCustomerKey;

/**
 * @author Pradeep Chawadkar
 *
 */
public interface MstCustomerService {
	
	public void addCustomer(Session foSession, MstCustomer foCustomer);

	public MstCustomer getCustomer(MstCustomerKey foCustomerKey);
	
	public List<Map<String, Object>> getCustomerList(MstCustomer foCustomer);
	public MstCustomer getCustomer(MstCustomer foCustomer);
}
