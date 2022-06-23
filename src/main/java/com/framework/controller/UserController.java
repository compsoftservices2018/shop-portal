package com.framework.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.framework.model.MstUser;
import com.framework.model.MstUserAccess;
import com.framework.model.MstUserAccessKey;
import com.framework.model.MstUserAccessList;
import com.framework.model.MstUserKey;
import com.framework.reference.ReferenceUtils;
import com.framework.service.MstUserAccessService;
import com.framework.service.MstUserService;
import com.framework.utils.AlertUtils;
import com.framework.utils.FrameworkConstants;
import com.framework.utils.GlobalValues;

@Controller
public class UserController extends SuperController {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private MstUserService MstUserService;

	@Autowired
	private MstUserAccessService MstUserAccessService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@RequestMapping(value = "/AddAdminUser", method = RequestMethod.GET)
	public ModelAndView addAdminUser(@ModelAttribute("MstUser") MstUser loUser, HttpSession foHttpSession) {
		Map<String, Object> model = new HashMap<String, Object>();
		loUser = new MstUser();
		loUser.setUser_type(FrameworkConstants.USER_TYPE_ADMIN);
		model.put("MstUser", loUser);

		MstUserKey loMstUserKey = new MstUserKey();
		loMstUserKey.setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
		model.put("MstUserList", MstUserService.getUsers(loMstUserKey));
		return new ModelAndView("/Admin/Main_User", model);
	}

	@RequestMapping(value = "/AddUser", method = RequestMethod.GET)
	public ModelAndView addUser(@ModelAttribute("MstUser") MstUser loUser, HttpSession foHttpSession) {
		Map<String, Object> model = new HashMap<String, Object>();
		loUser = new MstUser();
		loUser.setUser_type(FrameworkConstants.USER_TYPE_USER);
		model.put("MstUser", loUser);
		MstUserKey loMstUserKey = new MstUserKey();
		loMstUserKey.setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
		model.put("MstUserList", MstUserService.getUsers(loMstUserKey));
		return new ModelAndView("/Admin/Main_User", model);
	}

	@RequestMapping(value = "/SaveUser", method = RequestMethod.POST)
	public ModelAndView saveUser(@ModelAttribute("MstUser") MstUser foMstUser, BindingResult foResult,
			HttpSession foHttpSession, HttpServletRequest foHttpRequest) {
		Map<String, Object> model = new HashMap<String, Object>();

		if (foMstUser.getEmbAudit().getObject_mode().equals(FrameworkConstants.OBJECT_MODE_NEW)
				&& (MstUserService.getUser(foMstUser) != null)) {
			AlertUtils.addError("User record already exists with same User code.", foHttpSession);
			model.put("MstUser", foMstUser);
			MstUserKey loMstUserKey = new MstUserKey();
			loMstUserKey.setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
			model.put("MstUserList", MstUserService.getUsers(loMstUserKey));
			return new ModelAndView("/Admin/Main_User", model);
		}

		Session loSession = startTransation(sessionFactory);

		AlertUtils.validateModel(foMstUser, foHttpSession);
		if (!AlertUtils.hasAppErrors(foHttpSession)) {
			MstUserService.addUser(loSession, foMstUser);
			if (commit(loSession.getTransaction(), loSession, foHttpSession)) {
				foMstUser.getEmbAudit().setObject_mode(FrameworkConstants.OBJECT_MODE_UPDATE);
			}
		}

		ReferenceUtils.setReferenceDataUsers(foHttpSession, jdbcTemplate);

		model.put("MstUser", foMstUser);
		MstUserKey loMstUserKey = new MstUserKey();
		loMstUserKey.setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
		model.put("MstUserList", MstUserService.getUsers(loMstUserKey));
		return new ModelAndView("/Admin/Main_User", model);
	}

	@RequestMapping(value = "/GetUser", method = RequestMethod.POST)
	public ModelAndView getUser(@ModelAttribute("MstUser") MstUser foMstUser, BindingResult foResult,
			String company_code, String user_code, HttpSession foHttpSession, HttpServletRequest foHttpRequest) {
		Map<String, Object> model = new HashMap<String, Object>();
		MstUserKey loUserKey = new MstUserKey(company_code, user_code);
		MstUser loUser = new MstUser();
		loUser.setMstUserKey(loUserKey);
		loUser = MstUserService.getUser(loUser);
		model.put("MstUser", loUser);
		MstUserKey loMstUserKey = new MstUserKey();
		loMstUserKey.setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
		model.put("MstUserList", MstUserService.getUsers(loMstUserKey));
		return new ModelAndView("/Admin/Main_User", model);
	}

	@RequestMapping(value = "/OpenUserAccess", method = RequestMethod.GET)
	public ModelAndView openUserAccess(@ModelAttribute("MstUserAccessList") MstUserAccessList foMstUserAccessList,
			BindingResult foResult, HttpSession foHttpSession, String company_code, String user_code) {
		Map<String, Object> model = new HashMap<String, Object>();

		model.put("MstUser", null);
		MstUserKey loMstUserKey = new MstUserKey();
		loMstUserKey.setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
		model.put("MstUserList", MstUserService.getUsers(loMstUserKey));
		model.put("AttrMstUserAccessList", null);
		return new ModelAndView("/Admin/Main_UserAccess", model);
	}

	@RequestMapping(value = "/AddUserAccess", method = RequestMethod.POST)
	public ModelAndView addUserAccess(@ModelAttribute("MstUserAccessList") MstUserAccessList foMstUserAccessList,
			BindingResult foResult, HttpSession foHttpSession, String company_code, String user_code,
			String user_type) {
		Map<String, Object> model = new HashMap<String, Object>();

		List<Map<String, Object>> loUserAccessList = MstUserAccessService.getUserAccesssForUser(company_code, user_code,
				user_type);
		foMstUserAccessList = new MstUserAccessList();
		MstUser loUser = new MstUser();
		loUser.setMstUserKey(new MstUserKey(company_code, user_code));
		loUser = MstUserService.getUser(loUser);

		List<MstUserAccess> loUserAccessList1 = new ArrayList<MstUserAccess>();

		for (Map<String, Object> loUserAccessListRow : loUserAccessList) {
			MstUserAccess loMstUserAccess = new MstUserAccess();

			loMstUserAccess.setMstUserAccessKey(
					new MstUserAccessKey(company_code, user_code, (String) loUserAccessListRow.get("module_code")));
			loMstUserAccess.setModule_name((String) loUserAccessListRow.get("module_name"));
			loMstUserAccess.setShow((String) loUserAccessListRow.get("show"));
			loMstUserAccess.setAddition((String) loUserAccessListRow.get("addition"));
			loMstUserAccess.setUpdation((String) loUserAccessListRow.get("updation"));
			loMstUserAccess.setDeletion((String) loUserAccessListRow.get("deletion"));
			loMstUserAccess.setApproval((String) loUserAccessListRow.get("Approval"));
			loUserAccessList1.add(loMstUserAccess);
		}
		foMstUserAccessList.setMstUserAccessList(loUserAccessList1);
		model.put("MstUser", loUser);
		MstUserKey loMstUserKey = new MstUserKey();
		loMstUserKey.setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
		model.put("MstUserList", MstUserService.getUsers(loMstUserKey));
		model.put("AttrMstUserAccessList", foMstUserAccessList.getMstUserAccessList());
		return new ModelAndView("/Admin/Main_UserAccess", model);
	}

	@RequestMapping(value = "/SaveUserAccess", method = RequestMethod.POST)
	public ModelAndView saveUserAccess(@ModelAttribute("MstUserAccessList") MstUserAccessList foMstUserAccessList,
			BindingResult foResult, HttpSession foHttpSession, String company_code, String user_code) {
		Map<String, Object> model = new HashMap<String, Object>();
		Session loSession = startTransation(sessionFactory);
		// MstUser loUser = MstUserService.getUser(foHttpSession, new
		// MstUserKey(company_code, user_code));

		List<MstUserAccess> loUserAccessList = foMstUserAccessList.getMstUserAccessList();

		for (MstUserAccess loUserAccessObjectRow : loUserAccessList) {
			company_code = loUserAccessObjectRow.getMstUserAccessKey().getCompany_code();
			user_code = loUserAccessObjectRow.getMstUserAccessKey().getUser_code();

			AlertUtils.validateModel(loUserAccessObjectRow, foHttpSession);
			MstUserAccessService.addUserAccess(foHttpSession, loSession, loUserAccessObjectRow);
		}
		if (!AlertUtils.hasAppErrors(foHttpSession)) {
			commit(loSession.getTransaction(), loSession, foHttpSession);
		}

		loSession = startTransation(sessionFactory);
		MstUser loUser = new MstUser();
		loUser.setMstUserKey(new MstUserKey(company_code, user_code));

		loUser = MstUserService.getUser(loUser);
		model.put("MstUser", loUser);
		MstUserKey loMstUserKey = new MstUserKey();
		loMstUserKey.setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
		model.put("MstUserList", MstUserService.getUsers(loMstUserKey));
		model.put("AttrMstUserAccessList", foMstUserAccessList.getMstUserAccessList());
		return new ModelAndView("/Admin/Main_UserAccess", model);
	}

	@RequestMapping(value = "/UserAccess", method = RequestMethod.GET)
	public ModelAndView userAccess(@ModelAttribute("MstUserAccessList") MstUserAccessList foMstUserAccessList,
			HttpSession foHttpSession) {
		Map<String, Object> model = new HashMap<String, Object>();
		MstUserKey loMstUserKey = new MstUserKey();
		loMstUserKey.setCompany_code(GlobalValues.getCompanyCode(foHttpSession));
		model.put("MstUserList", MstUserService.getUsers(loMstUserKey));
		// List<MstUserAccess> loMstUserAccessList = new
		// ArrayList<MstUserAccess>();
		model.put("AttrMstUserAccessList", foMstUserAccessList.getMstUserAccessList());
		return new ModelAndView("/Admin/Main_UserAccess", model);
	}

	/*
	 * @RequestMapping(value = "/SaveUserAccess", method = RequestMethod.GET)
	 * public ModelAndView saveUserAccess(@ModelAttribute("MstUserAccessList")
	 * MstUserAccessList loMstUserAccessList, HttpSession foHttpSession) {
	 * Map<String, Object> model = new HashMap<String, Object>(); loUser = new
	 * MstUser(); loUser.setUser_type(FrameworkConstants.USER_TYPE_USER);
	 * model.put("MstUser", loUser); model.put("MstUserList",
	 * MstUserService.getUsers(foHttpSession)); return new
	 * ModelAndView("/Administration/Main_User", model); }
	 */
}
