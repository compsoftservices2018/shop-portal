package com.framework.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;

import com.framework.model.MstCompany;
import com.framework.model.MstCompanyKey;;

public interface MstCompanyDao extends SuperDao {

	public void addCompany(HttpSession foHttpSession, Session foSession, MstCompany foCompany);

	public MstCompany getCompanyByKey(String fsKey);

	public MstCompany getCompany(MstCompanyKey foCompanyKey);

	public List<Map<String, Object>> getCompanies(HttpSession foHttpSession);

}
