package com.compsoft.shop.service;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.json.simple.JSONObject;

import com.compsoft.shop.model.MstProduct;
import com.compsoft.shop.model.MstProductKey;

/**
 * @author Pradeep Chawadkar
 *
 */
public interface MstProductService {
	
	public void addProduct(Session foSession, MstProduct foProduct);
	public void delete(Session foSession, MstProduct foProduct);

	public JSONObject getProduct(MstProductKey foProductKey);
	public JSONObject getProduct(MstProductKey foProductKey, String fsOrderNo);
	public List<Map<String, Object>> getProductList(MstProduct foProduct);

}
