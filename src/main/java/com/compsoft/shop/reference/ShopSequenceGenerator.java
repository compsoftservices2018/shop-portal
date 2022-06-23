package com.compsoft.shop.reference;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Pravin
 *
 */
public abstract class ShopSequenceGenerator
{
	private static Object loOrderSeqlock = new Object();
	private static Object loCustomerSeqlock = new Object();
	private static Object loBillSeqlock = new Object();
		
	public static String getNextOrderId(String fsCompanyCode, JdbcTemplate jdbcTemplate)
	{
		synchronized (loOrderSeqlock)
		{
			String llReturn = getNextId(fsCompanyCode, "ORDER", jdbcTemplate);
			return llReturn;
		}
	}
	
	public static String getNextCustomerId(String fsCompanyCode, JdbcTemplate jdbcTemplate)
	{
		synchronized (loCustomerSeqlock)
		{
			String llReturn = getNextId(fsCompanyCode, "CUSTOMER", jdbcTemplate);
			return llReturn;
		}
	}

	public static String getNextBillId(String fsCompanyCode, JdbcTemplate jdbcTemplate)
	{
		synchronized (loBillSeqlock)
		{
			String llReturn = getNextId(fsCompanyCode, "BILL", jdbcTemplate);
			return llReturn;
		}
	}
	/**
	 * @param fsCompanyCode
	 * @param fsLocationCode
	 * @param fsKey
	 * @param jdbcTemplate
	 * @return
	 */
	protected static String getNextId(String fsCompanyCode, String fsKey,
			JdbcTemplate jdbcTemplate)
	{
		String lsSelect = "SELECT NEXT_VAL FROM TRAN_SEQUENCE WHERE COMPANY_CODE = ? AND SEQ_KEY = ? ";
		Map<String, Object> loUser = jdbcTemplate.queryForList(lsSelect, new Object[]
			{ fsCompanyCode, fsKey }).get(0);
		lsSelect = "UPDATE TRAN_SEQUENCE SET NEXT_VAL = NEXT_VAL + 1 WHERE COMPANY_CODE = ? AND SEQ_KEY = ? ";
		jdbcTemplate.update(lsSelect, new Object[]
		{ fsCompanyCode, fsKey });
		
		return String.format("%10s", ((BigDecimal) loUser.get("NEXT_VAL")).longValue()).replace(" ", "0");
		
	}
	
	
}
