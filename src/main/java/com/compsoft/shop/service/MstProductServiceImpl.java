package com.compsoft.shop.service;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.compsoft.shop.dao.MstProductDao;
import com.compsoft.shop.model.MstProduct;
import com.compsoft.shop.model.MstProductKey;

/**
 * @author Pradeep Chawadkar
 *
 */
@Service("MstProductService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MstProductServiceImpl implements MstProductService {

	@Autowired
	private MstProductDao MstProductDao;
	
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void addProduct(Session foSession, MstProduct foProduct) {
	
		MstProductDao.addProduct(foSession, foProduct);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void delete(Session foSession, MstProduct foProduct) {
	
		MstProductDao.delete(foSession, foProduct);
	}
	
	
	public JSONObject getProduct(MstProductKey foProductKey)
	{
		return MstProductDao.getProduct( foProductKey);
	}
	
	public JSONObject getProduct(MstProductKey foProductKey, String fsOrderNo) {
		return MstProductDao.getProduct( foProductKey, fsOrderNo);
	}
	
	public List<Map<String, Object>> getProductList(MstProduct foProduct)
	{
		return MstProductDao.getProductList( foProduct);
	}
	


}
