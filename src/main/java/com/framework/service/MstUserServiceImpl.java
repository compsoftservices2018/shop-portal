package com.framework.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.framework.dao.MstUserDao;
import com.framework.model.MstUser;
import com.framework.model.MstUserKey;

/**
 * @author Pradeep Chawadkar
 *
 */
@Service("MstUserService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MstUserServiceImpl implements MstUserService {

	@Autowired
	private MstUserDao MstUserDao;
	
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void addUser(Session foSession, MstUser foUser) {
	
		MstUserDao.addUser(foSession, foUser);
	}
	
	
	public MstUser getUser(MstUser foUser)
	{
		return MstUserDao.getUser(foUser);
	}
	
	public List<Map<String, Object>> getUsers(MstUserKey foUserKey)
	{
		return MstUserDao.getUsers(foUserKey);
	}
	

}
