package com.compsoft.shop.dao;


import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.compsoft.shop.model.TranOrder;
import com.compsoft.shop.model.TranOrderPayment;
import com.framework.dao.SuperDao;;

/**
 * @author Pradeep Chawadkar
 *
 */
public interface TranOrderDao extends SuperDao {
	
	public void addOrder(Session foSession, TranOrder foOrder);
	//public void deleteOrder(Session foSession, TranOrder foTranOrder);
	public TranOrder getOrder(TranOrder foOrder); 
	public List<Map<String, Object>> getOrders(TranOrder foTranOrder);
	public List<Map<String, Object>> getOrderDetails(TranOrder foTranOrder); 
	public List<Map<String, Object>> getOrderPayment(TranOrderPayment foOrderPayment);

	/*public OrderCart getOrderCart( OrderCartKey foOrderCartKey,  String llProductId);
	
	public  List<Map<String, Object>> getOrdersForCustomer(MstCustomer foCustomer);

	public  List<Map<String, Object>> getOrdersByStatus( String fsStatus);
	
	public List<Map<String, Object>> getOrdersByCustIdStatus(MstCustomer foCustomer, String fsStatus);
	
	public List<Map<String, Object>> getProductListForCart(MstCustomer foCustomer, MstProduct foProduct, String fsSort);
		*/

	}
