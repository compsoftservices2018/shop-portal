package com.framework.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.framework.dao.MstUserAccessDao;
import com.framework.model.MstUser;
import com.framework.model.MstUserAccess;
import com.framework.model.MstUserAccessKey;
import com.framework.model.MstUserKey;

/**
 * @author Pradeep Chawadkar
 *
 */
@Service("MstUserAccessService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MstUserAccessServiceImpl implements MstUserAccessService {

	@Autowired
	private MstUserAccessDao MstUserAccessDao;
	
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void addUserAccess(HttpSession foHttpSession, Session foSession, MstUserAccess foUser) {
	
		MstUserAccessDao.addUserAccess(foHttpSession, foSession, foUser);
	}
	
	public MstUserAccess getUserAccess(HttpSession foHttpSession, MstUserAccessKey foUserAccessKey)
	{
		return MstUserAccessDao.getUserAccess( foHttpSession,  foUserAccessKey);
	}

	public List<Map<String, Object>> getUserAccesssForUser(String fsCompanyCode, String fsUserCode,  String fsUserType)
	{
		return MstUserAccessDao.getUserAccesssForUser( fsCompanyCode,  fsUserCode,  fsUserType);
	}
	
	
	

}
