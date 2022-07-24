package com.compsoft.shop.service;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.compsoft.shop.model.TranOrder;
import com.compsoft.shop.model.TranOrderPayment;

/**
 * @author Pradeep Chawadkar
 *
 */
public interface TranOrderService {
	

	public void addOrder(Session foSession, TranOrder foOrder);
	//public void deleteOrder(Session foSession, TranOrder foTranOrder);
	public TranOrder getOrder(TranOrder foOrder);  
	public List<Map<String, Object>> getOrders(TranOrder foTranOrder);
	public List<Map<String, Object>> getOrderDetails(TranOrder foTranOrder);
	public List<Map<String, Object>> getOrderPayment(TranOrderPayment foOrderPayment);

	
}
