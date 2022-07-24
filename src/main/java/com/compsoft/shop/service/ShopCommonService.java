package com.compsoft.shop.service;

import javax.servlet.http.HttpSession;


/**
 * @author Pradeep Chawadkar
 *
 */
public interface ShopCommonService {
	
	public String getNextNumber(HttpSession foHttpSession, String fsTranType);
}
