package com.compsoft.shop.service;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.compsoft.shop.dao.TranOrderDao;
import com.compsoft.shop.model.TranOrder;
import com.compsoft.shop.model.TranOrderPayment;

/**
 * @author Pradeep Chawadkar
 *
 */
@Service("TranOrderService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TranOrderServiceImpl implements TranOrderService {

	@Autowired
	private TranOrderDao TranOrderDao; 
	
	
	public void addOrder(Session foSession, TranOrder foOrder)
	{
		TranOrderDao.addOrder(foSession, foOrder);
	}
/*	public void deleteOrder(Session foSession, TranOrder foTranOrder)
	{
		TranOrderDao.deleteOrder(foSession, foTranOrder);
	}*/
	public TranOrder getOrder(TranOrder foOrder)
	{
		return TranOrderDao.getOrder(foOrder);
	}
	
	public List<Map<String, Object>> getOrders(TranOrder foTranOrder)
	{
		return TranOrderDao.getOrders(foTranOrder);
	}

	public List<Map<String, Object>> getOrderDetails(TranOrder foTranOrder)
	{
		return TranOrderDao.getOrderDetails(foTranOrder);
	}
	public List<Map<String, Object>> getOrderPayment(TranOrderPayment foOrderPayment) {
		return TranOrderDao.getOrderPayment(foOrderPayment);
	}
}
