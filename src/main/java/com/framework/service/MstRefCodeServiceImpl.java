package com.framework.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.framework.dao.MstRefCodeDao;
import com.framework.model.MstRefCode;
import com.framework.model.MstRefCodeKey;
import com.framework.service.MstRefCodeService;

/**
 * @author Pradeep Chawadkar
 *
 */
@Service("RefCodeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MstRefCodeServiceImpl implements MstRefCodeService {

	@Autowired
	private MstRefCodeDao MstRefCodeDao;
	
	public MstRefCodeServiceImpl()
	{
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void addRefCode(HttpSession foHttpSession, Session foSession, MstRefCode foRefCode) {
		MstRefCodeDao.addRefCode(foHttpSession,  foSession,  foRefCode);
	}

	public MstRefCode getRefCode(HttpSession foHttpSession, MstRefCodeKey foRefCodeKey)
	{
		return MstRefCodeDao.getRefCode(foHttpSession, foRefCodeKey);
	}
	
	public  List<Map<String, Object>> getAllRefCodes(HttpSession foHttpSession, String fsRefGroup)
	{
		return MstRefCodeDao.getAllRefCodes(foHttpSession, fsRefGroup);
	}
}
