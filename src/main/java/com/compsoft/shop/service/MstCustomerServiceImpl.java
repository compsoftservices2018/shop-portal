package com.compsoft.shop.service;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.compsoft.shop.dao.MstCustomerDao;
import com.compsoft.shop.model.MstCustomer;
import com.compsoft.shop.model.MstCustomerKey;

/**
 * @author Pradeep Chawadkar
 *
 */
@Service("MstCustomerService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MstCustomerServiceImpl implements MstCustomerService {

	@Autowired
	private MstCustomerDao MstCustomerDao;
	
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void addCustomer(Session foSession, MstCustomer foCustomer) {
	
		MstCustomerDao.addCustomer(foSession, foCustomer);
	}
	
	
	public MstCustomer getCustomer( MstCustomerKey foCustomerKey)
	{
		return MstCustomerDao.getCustomer(foCustomerKey);
	}
	
	public List<Map<String, Object>> getCustomerList( MstCustomer foCustomer)
	{
		return MstCustomerDao.getCustomerList(foCustomer);
	}
	
	
	public MstCustomer getCustomer(MstCustomer foCustomer)
	{
		return MstCustomerDao.getCustomer(foCustomer);
	}
	

}
