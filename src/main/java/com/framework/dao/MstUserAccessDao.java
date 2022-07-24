package com.framework.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;

import com.framework.model.MstUserAccess;
import com.framework.model.MstUserAccessKey;;

public interface MstUserAccessDao extends SuperDao {

	public void addUserAccess(HttpSession foHttpSession, Session foSession, MstUserAccess foUserAccess);

	public MstUserAccess getUserAccess(HttpSession foHttpSession, MstUserAccessKey foUserAccessKey);

	public List<Map<String, Object>> getUserAccesssForUser(String fsCompanyCode, String fsUserCode, String fsUserType);

}
