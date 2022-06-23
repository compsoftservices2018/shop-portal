package com.framework.dao;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;

/*import org.json.JSONObject;
*/
import com.framework.model.ErrorLog;;

public interface CommonDao extends SuperDao {

	public boolean sendSMS(HttpSession foHttpSession, String mobile, String msg, String lsFeature);

	public void addErrorLog(Session foSession, ErrorLog foErrorLog);
	
	//public JSONObject createPayment(JSONObject data);

}
