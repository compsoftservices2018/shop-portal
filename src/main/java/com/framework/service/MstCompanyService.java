package com.framework.service;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;

import com.framework.model.MstCompany;
import com.framework.model.MstCompanyKey;

/**
 * @author Pradeep Chawadkar
 *
 */
public interface MstCompanyService {
	
	public void addCompany(HttpSession foHttpSession, Session foSession, MstCompany foCompany);

	public MstCompany getCompany(MstCompanyKey foCompanyKey);
	public MstCompany getCompanyByKey( String fsKey);
	
	public List<Map<String, Object>> getCompanies(HttpSession foHttpSession);
}
