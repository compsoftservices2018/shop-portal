package com.compsoft.shop.dao;

import java.util.List;
import java.util.Map;

import com.compsoft.shop.model.MstCustomerKey;
import com.compsoft.shop.model.MstProduct;
import com.framework.dao.SuperDao;


/**
 * @author Pradeep Chawadkar
 *
 */
public interface TranCustCartDao extends SuperDao {
	
	/*public void addCart(Session foSession, TranCustCart foTranCustCart) ;
	public void deleteCart(Session foSession, TranCustCart foTranCustCart) ;
	public TranCustCart getCart(TranCustCartKey foTranCustCartKey, MstProductKey loMstProductKey);
*/	public List<Map<String, Object>> getProductForLocation(MstProduct foProduct,
			String fsPin, String fsOrderNo);
	public Map<String, Object> getCartSummary(MstCustomerKey loMstCustomerKey);


	}
