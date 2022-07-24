package com.framework.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.framework.model.ErrorLog;
import com.framework.model.MstUser;
import com.framework.service.CommonService;
import com.framework.utils.AlertUtils;
import com.framework.utils.AppUtils;
import com.framework.utils.GlobalValues;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
public class SuperController {

	public Gson moGson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
	public Logger logger = Logger.getLogger(SuperController.class);

	@Autowired
	private CommonService CommonService;

	@Autowired
	private SessionFactory sessionFactory;

	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	public boolean commit(Transaction foTransaction, Session foSession, HttpSession foHttpSession) {
		try {
			foTransaction.commit();
			foSession.close();
			AlertUtils.addSuccess("Transaction saved successfully.", foHttpSession);
			return true;
		} catch (Exception e) {
			foTransaction.rollback();
			AlertUtils.addError(
					"Severe error occurred while saving transaction...Please contact your administrator.\n\nDetail Message:\n"
							+ e.getMessage(),
					foHttpSession);
			e.printStackTrace();
			return false;
		}
	}

	public boolean commitNoMsg(Transaction foTransaction, Session foSession, HttpSession foHttpSession) {
		try {
			foTransaction.commit();
			foSession.close();
			// AlertUtils.addSuccess("Transaction saved successfully.",
			// foHttpSession);
			return true;
		} catch (Exception e) {
			foTransaction.rollback();
			AlertUtils.addError(
					"Severe error occurred while saving transaction...Please contact your administrator.\n\nDetail Message:\n"
							+ e.getMessage(),
					foHttpSession);
			e.printStackTrace();
			return false;
		}
	}

	public boolean commitNoMsg(Transaction foTransaction, Session foSession) {
		try {
			foTransaction.commit();
			foSession.close();
			// AlertUtils.addSuccess("Transaction saved successfully.",
			// foHttpSession);
			return true;
		} catch (Exception e) {
			foTransaction.rollback();
			e.printStackTrace();
			return false;
		}
	}

	public Session startTransation(SessionFactory foSessionFactory) {
		Session loSession = foSessionFactory.openSession();

		if (loSession.getTransaction().isActive()) {
			loSession.getTransaction();
		} else {
			foSessionFactory.openSession();
			loSession.beginTransaction();
		}
		return loSession;
	}

	protected void logException(Exception ex, HttpSession foHttpSession) {

		logger.error(ex.getStackTrace());
		ex.printStackTrace();
		AlertUtils.addError("Unexpected error has occured. Please contact System Administrator.", foHttpSession);
		Session loSession = startTransation(sessionFactory);
		MstUser loUser = (MstUser) foHttpSession.getAttribute("USEROBJ");
		ErrorLog loErrorLog = new ErrorLog();

		if (loUser != null) {
			loErrorLog.setUser_code(loUser.getMstUserKey().getUser_code());
			loErrorLog.setUser_info(loUser.getUser_name());
		}
		// loErrorLog.setCause(ex.getCause().toString());
		loErrorLog.setClass_name(ex.getClass().toString());
		loErrorLog.setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
		loErrorLog.setErr_date(AppUtils.getCurrentTimestamp());

		StackTraceElement[] loElements = ex.getStackTrace();
		int len = loElements.length < 20 ? loElements.length : 20;

		String lsDetail = "";
		for (int i = 0; i < len; i++) {
			lsDetail = lsDetail + loElements[i] + "\n";
		}

		if (lsDetail.length() > 3600) {
			loErrorLog.setDetail(lsDetail.substring(0, 3500));
		} else {
			loErrorLog.setDetail(lsDetail);
		}
		loErrorLog.setError(ex.getMessage());

		try {

			CommonService.addErrorLog(loSession, loErrorLog);
			if (!commitNoMsg(loSession.getTransaction(), loSession, foHttpSession)) {
				// System.out.println("Error:Unable to save error log.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	public void commitTransaction(Session foSession, HttpSession foHttpSession) {
		try {
			foSession.getTransaction().commit();
			foSession.close();
			AlertUtils.addSuccess("Transaction saved successfully.", foHttpSession);
		} catch (Exception e) {
			foSession.getTransaction().rollback();
			AlertUtils.addError(
					"Severe error occurred while saving transaction...Please contact your administrator.\n\nDetail Message:\n"
							+ e.getMessage(),
					foHttpSession);
		}
	}

	/*
	 * public org.json.simple.JSONObject commitTransactionJResponse(Session
	 * foSession, HttpSession foHttpSession) { org.json.simple.JSONObject loReturn =
	 * new org.json.simple.JSONObject();
	 * 
	 * try { loReturn.put("status", FrameworkConstants.RESPONSE_STATUS_FAILED);
	 * foSession.getTransaction().commit(); loReturn.put("status",
	 * FrameworkConstants.RESPONSE_STATUS_SUCCESS); foSession.close(); } catch
	 * (Exception e) { foSession.getTransaction().rollback(); } return loReturn; }
	 */

}
