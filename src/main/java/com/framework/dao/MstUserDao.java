package com.framework.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.framework.model.MstUser;
import com.framework.model.MstUserKey;;

public interface MstUserDao extends SuperDao {

	public void addUser(Session foSession, MstUser foUser);

	public MstUser getUser(MstUser foUser);

	public List<Map<String, Object>> getUsers(MstUserKey foUserKey);

}
