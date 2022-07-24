package com.compsoft.shop.service;

import org.json.simple.JSONObject;

import com.compsoft.shop.model.TranOrder;

/**
 * @author Pradeep Chawadkar
 *
 */
public interface ExternalAPIService {
	
	public JSONObject  createPayment(TranOrder  data);
}
