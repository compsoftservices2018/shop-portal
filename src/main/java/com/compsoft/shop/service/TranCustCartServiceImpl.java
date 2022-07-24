package com.compsoft.shop.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.compsoft.shop.dao.TranCustCartDao;
import com.compsoft.shop.model.MstCustomerKey;
import com.compsoft.shop.model.MstProduct;

/**
 * @author Pradeep Chawadkar
 *
 */
@Service("TranCustCartService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TranCustCartServiceImpl implements TranCustCartService {

	@Autowired
	private TranCustCartDao TranCustCartDao; 
	
	
	
	/*public void addCart(Session foSession, TranCustCart foTranCustCart) {
		TranCustCartDao.addCart( foSession,  foTranCustCart);
	}
	public void deleteCart(Session foSession, TranCustCart foTranCustCart) {
		TranCustCartDao.deleteCart( foSession,  foTranCustCart);
	}
	public TranCustCart getCart(TranCustCartKey foTranCustCartKey, MstProductKey loMstProductKey){
		return TranCustCartDao.getCart( foTranCustCartKey, loMstProductKey);
	}
	*/public List<Map<String, Object>> getProductForLocation(MstProduct foProduct, String fsPin, String fsOrderNo){
		return TranCustCartDao.getProductForLocation(foProduct,fsPin, fsOrderNo);
	}
	public Map<String, Object> getCartSummary(MstCustomerKey loMstCustomerKey)
	{
		return TranCustCartDao.getCartSummary(loMstCustomerKey);
	}
}
