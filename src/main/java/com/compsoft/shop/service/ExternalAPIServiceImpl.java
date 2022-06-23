package com.compsoft.shop.service;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.compsoft.shop.dao.ExternalAPIDao;
import com.compsoft.shop.model.TranOrder;

/**
 * @author Pradeep Chawadkar
 *
 */
@Service("MstPaymentService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ExternalAPIServiceImpl implements ExternalAPIService {

	@Autowired
	private ExternalAPIDao TranPaymentDao;
	
	public JSONObject  createPayment(TranOrder  data) {
		return TranPaymentDao.createPayment(data);
	}
	

}
