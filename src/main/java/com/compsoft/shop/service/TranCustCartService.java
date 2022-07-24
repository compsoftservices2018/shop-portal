package com.compsoft.shop.service;

import java.util.List;
import java.util.Map;

import com.compsoft.shop.model.MstCustomerKey;
import com.compsoft.shop.model.MstProduct;


/**
 * @author Pradeep Chawadkar
 *
 */
public interface TranCustCartService {
	

	/*public void addCart(Session foSession, TranCustCart foTranCustCart) ;
	public void deleteCart(Session foSession, TranCustCart foTranCustCart) ;
	public TranCustCart getCart(TranCustCartKey foTranCustCartKey, MstProductKey loMstProductKey);
*/	public List<Map<String, Object>> getProductForLocation(MstProduct foProduct, String fsPin, String fsOrderNo);
	public Map<String, Object> getCartSummary(MstCustomerKey loMstCustomerKey);
}
