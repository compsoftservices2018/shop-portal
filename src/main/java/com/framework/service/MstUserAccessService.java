package com.framework.service;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;

import com.framework.model.MstUser;
import com.framework.model.MstUserAccess;
import com.framework.model.MstUserAccessKey;
import com.framework.model.MstUserKey;

/**
 * @author Pradeep Chawadkar
 *
 */
public interface MstUserAccessService {
	
public void addUserAccess(HttpSession foHttpSession, Session foSession, MstUserAccess foUserAccess);
	
	public MstUserAccess getUserAccess(HttpSession foHttpSession, MstUserAccessKey foUserAccessKey);

	public List<Map<String, Object>> getUserAccesssForUser(String fsCompanyCode, String fsUserCode,  String fsUserType);
	
	
}
