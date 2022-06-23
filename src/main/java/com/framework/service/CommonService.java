package com.framework.service;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
/*import org.json.JSONObject;*/

import com.framework.model.ErrorLog;
import com.framework.model.MstUser;
import com.framework.model.MstUserKey;

/**
 * @author Pradeep Chawadkar
 *
 */
public interface CommonService {
	
	public boolean sendSMS(HttpSession foHttpSession,String mobile, String msg, String lsFeature); 
	public void addErrorLog(Session foSession, ErrorLog foErrorLog);
	
}
