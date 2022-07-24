package com.compsoft.shop.dao;

import org.json.simple.JSONObject;

import com.compsoft.shop.model.TranOrder;
import com.framework.dao.SuperDao;;

public interface ExternalAPIDao extends SuperDao {

	public JSONObject createPayment(TranOrder foOrder);

}
