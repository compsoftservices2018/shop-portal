package com.framework.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.framework.dao.MstCompanyDao;
import com.framework.model.MstCompany;
import com.framework.model.MstCompanyKey;

/**
 * @author Pradeep Chawadkar
 *
 */
@Service("MstCompanyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MstCompanyServiceImpl implements MstCompanyService {

	@Autowired
	private MstCompanyDao MstCompanyDao;
	
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void addCompany(HttpSession foHttpSession, Session foSession, MstCompany foCompany) {
	
		MstCompanyDao.addCompany(foHttpSession, foSession, foCompany);
	}
	
	
	public MstCompany getCompany( MstCompanyKey foCompanyKey)
	{
		return MstCompanyDao.getCompany( foCompanyKey);
	}
	
	public MstCompany getCompanyByKey( String fsKey)
	{
		return MstCompanyDao.getCompanyByKey( fsKey);
	}
	
	public List<Map<String, Object>> getCompanies(HttpSession foHttpSession)
	{
		return MstCompanyDao.getCompanies(foHttpSession);
	}
	

}
