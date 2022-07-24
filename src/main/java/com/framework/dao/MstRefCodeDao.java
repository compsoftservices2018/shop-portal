package com.framework.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;

import com.framework.model.MstRefCode;
import com.framework.model.MstRefCodeKey;;

public interface MstRefCodeDao extends SuperDao {

	public void addRefCode(HttpSession foHttpSession, Session foSession, MstRefCode foRefCode);

	public MstRefCode getRefCode(HttpSession foHttpSession, MstRefCodeKey foRefCodeKey);

	public List<Map<String, Object>> getAllRefCodes(HttpSession foHttpSession, String fsRefGroup);
}
