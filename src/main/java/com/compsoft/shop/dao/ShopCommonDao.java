package com.compsoft.shop.dao;

import javax.servlet.http.HttpSession;

import com.framework.dao.SuperDao;;

/**
 * @author Pradeep Chawadkar
 *
 */
public interface ShopCommonDao extends SuperDao {
	
	public String getNextNumber(HttpSession foHttpSession, String fsTranType);
	}
