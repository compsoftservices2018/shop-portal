package com.compsoft.shop.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.compsoft.shop.dao.ShopCommonDao;


/**
 * @author Pradeep Chawadkar
 *
 */
@Service("ShopCommonService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ShopCommonServiceImpl implements ShopCommonService {

	@Autowired
	private ShopCommonDao ShopCommonDao;
	
	public String getNextNumber(HttpSession foHttpSession, String fsTranType){
		return ShopCommonDao.getNextNumber(foHttpSession, fsTranType);
	}
}
