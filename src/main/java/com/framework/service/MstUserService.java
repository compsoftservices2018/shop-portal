package com.framework.service;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;

import com.framework.model.MstUser;
import com.framework.model.MstUserKey;

/**
 * @author Pradeep Chawadkar
 *
 */
public interface MstUserService {
	
	public void addUser(Session foSession, MstUser foUser);

	public MstUser getUser(MstUser foUser);
	
	public List<Map<String, Object>> getUsers(MstUserKey foUserKey);
}
