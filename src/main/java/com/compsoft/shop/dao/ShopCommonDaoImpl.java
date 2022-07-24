package com.compsoft.shop.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.framework.utils.AlertUtils;
import com.framework.utils.GlobalValues;

/**
 * @author Pradeep Chawadkar
 *
 */
@Repository("ShopCommonDao")
public class ShopCommonDaoImpl implements ShopCommonDao {


	@Autowired
	private JdbcTemplate jdbcTemplate;

	public String getNextNumber(HttpSession foHttpSession, String fsTranType) {
		List<Map<String, Object>> lsReturn;
		
		String lsSelect = "SELECT * FROM MST_SEQUENCE WHERE COMPANY_CODE = ? AND TRAN_TYPE = ?";
		lsReturn = jdbcTemplate.queryForList(lsSelect,
				new Object[] { GlobalValues.getCompanyCode(foHttpSession), fsTranType});
		if (lsReturn.size() <= 0) {
			AlertUtils.addError("Sequence generator not defined",foHttpSession);
			return null;
		}
		
		String lsTableName = "";
		String lsColumnName = "";
			if (fsTranType.equals("OLORD"))
		{
			lsTableName = "TRAN_ORDER_HEADER";
			lsColumnName = "ORDER_NO";
		}
		else
		{
			AlertUtils.addError("Invalid transaction type",foHttpSession);
			return null;
		}
			
		int lsNumberSize = ((BigDecimal)lsReturn.get(0).get("LEN")).intValue();
		int liStartNumber = ((BigDecimal)lsReturn.get(0).get("START_NO")).intValue();
		String lsIncludeFY = (String)lsReturn.get(0).get("WITHFY");
		String lsFY = "";
		if (lsIncludeFY.equals("Y"))
		{
			lsFY = "/" + GlobalValues.getFiscalYear(foHttpSession);
		}	
		
		lsSelect = "SELECT ROWNUM +" +liStartNumber+ " NEXTVAL FROM ALL_OBJECTS "
				+ " WHERE ROWNUM +" +liStartNumber+ " <= (SELECT MAX(TO_NUMBER(SUBSTR("+lsColumnName+" ,1," + lsNumberSize +"))) + 1 FROM " 
				+ lsTableName + " WHERE COMPANY_CODE = " + GlobalValues.getCompanyCode(foHttpSession) + " ) "
				+ " MINUS SELECT TO_NUMBER(SUBSTR("+lsColumnName+",1,"+ lsNumberSize +")) FROM "
				+ lsTableName 
				+" WHERE COMPANY_CODE = " + GlobalValues.getCompanyCode(foHttpSession);
		
		try {
			lsReturn = jdbcTemplate.queryForList(lsSelect);
			int liNextNumber = 0;
			if (lsReturn.size() <= 0) {
				liNextNumber = liStartNumber;
			} else {
				liNextNumber = ((BigDecimal)lsReturn.get(0).get("NEXTVAL")).intValue();;
			}
			return String.format("%" + lsNumberSize + "s", liNextNumber).replace(" ", "0") + lsFY;
		} catch (Exception ex) {
			throw ex;
		}
	}
}
