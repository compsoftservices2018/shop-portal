package com.framework.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;
/*import org.json.JSONObject;*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.framework.dao.MstCompanyDao;
import com.framework.dao.CommonDao;
import com.framework.model.ErrorLog;
import com.framework.model.MstUser;
import com.framework.model.MstUserKey;

/**
 * @author Pradeep Chawadkar
 *
 */
@Service("CommonService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CommonServiceImpl implements CommonService {

	@Autowired
	private CommonDao CommonDao;
	
	//@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean sendSMS(HttpSession foHttpSession,String mobile, String msg, String lsFeature){ 
		return CommonDao.sendSMS(foHttpSession,mobile,  msg, lsFeature);
	}

	public void addErrorLog(Session foSession, ErrorLog foErrorLog){
		CommonDao.addErrorLog(foSession, foErrorLog);
	}
	
	
}
