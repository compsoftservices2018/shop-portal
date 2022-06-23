package com.framework.generator;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Pravin
 *
 */
public abstract class SequenceGenerator
{
	private static Object loAgentSeqlock = new Object();
	private static Object loMemberSeqlock = new Object();
	private static Object loMembershipSeqlock = new Object();
	private static Object loPaymentSeqlock = new Object();
	private static Object loProductSeqlock = new Object();
	private static Object loAdjustmentSeqlock = new Object();
	private static Object loBillSeqlock = new Object();
	private static Object loSMSSeqlock = new Object();
	private static Object loOnlineOrder = new Object();
	
	
	public static Long getNextAgentId(String fsCompanyCode, String fsLocationCode, JdbcTemplate jdbcTemplate)
	{
		synchronized (loAgentSeqlock)
		{
			Long llReturn = getNextId(fsCompanyCode, fsLocationCode, "AGENT", jdbcTemplate);
			return llReturn;
		}
	}

	


	public static Long getNextMemberId(String fsCompanyCode, String fsLocationCode, JdbcTemplate jdbcTemplate)
	{
		synchronized (loMemberSeqlock)
		{
			Long llReturn = getNextId(fsCompanyCode, fsLocationCode, "MEMBER", jdbcTemplate);
			return llReturn;
		}
	}

	public static Long getNextMembershipId(String fsCompanyCode, String fsLocationCode, JdbcTemplate jdbcTemplate)
	{
		synchronized (loMembershipSeqlock)
		{
			Long llReturn = getNextId(fsCompanyCode, fsLocationCode, "MEMBERSHIP", jdbcTemplate);
			return llReturn;
		}
	}

	public static Long getNextPaymentId(String fsCompanyCode, String fsLocationCode, JdbcTemplate jdbcTemplate)
	{
		synchronized (loPaymentSeqlock)
		{
			Long llReturn = getNextId(fsCompanyCode, fsLocationCode, "CONTACT", jdbcTemplate);
			return llReturn;
		}
	}

	public static Long getNextProductId(String fsCompanyCode, String fsLocationCode, JdbcTemplate jdbcTemplate)
	{
		synchronized (loProductSeqlock)
		{
			Long llReturn = getNextId(fsCompanyCode, fsLocationCode, "PRODUCT", jdbcTemplate);
			return llReturn;
		}
	}

	public static Long getNextAdjustmentId(String fsCompanyCode, String fsLocationCode, JdbcTemplate jdbcTemplate)
	{
		synchronized (loAdjustmentSeqlock)
		{
			Long llReturn = getNextId(fsCompanyCode, fsLocationCode, "ADJUSTMENT", jdbcTemplate);
			return llReturn;
		}
	}
	
	public static Long getNextBillId(String fsCompanyCode, String fsLocationCode, JdbcTemplate jdbcTemplate)
	{
		synchronized (loBillSeqlock)
		{
			Long llReturn = getNextId(fsCompanyCode, fsLocationCode, "BILL", jdbcTemplate);
			return llReturn;
		}
	}
	
	public static Long getNextMembershipId_BL05K(String fsCompanyCode, String fsLocationCode, JdbcTemplate jdbcTemplate)
	{
		synchronized (loMembershipSeqlock)
		{
			Long llReturn = getNextId(fsCompanyCode, fsLocationCode, "BL05K_MEMBERSHIP", jdbcTemplate);
			return llReturn;
		}
	}

	public static Long getNextMembershipId_BL500(String fsCompanyCode, String fsLocationCode, JdbcTemplate jdbcTemplate)
	{
		synchronized (loMembershipSeqlock)
		{
			Long llReturn = getNextId(fsCompanyCode, fsLocationCode, "BL500_MEMBERSHIP", jdbcTemplate);
			return llReturn;
		}
	}
	

	public static Long getNextSMSId(String fsCompanyCode, String fsLocationCode, JdbcTemplate jdbcTemplate)
	{
		synchronized (loSMSSeqlock)
		{
			Long llReturn = getNextId(fsCompanyCode, fsLocationCode, "SMS", jdbcTemplate);
			return llReturn;
		}
	}
	
	public static Long getNextOnlineOrder(JdbcTemplate jdbcTemplate)
	{
		synchronized (loOnlineOrder)
		{
			Long llReturn = getNextId("ONLINEORDER", jdbcTemplate);
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
	protected static Long getNextId(String fsCompanyCode, String fsLocationCode, String fsKey,
			JdbcTemplate jdbcTemplate)
	{
		String lsSelect = "SELECT NEXT_VAL FROM MST_SEQUENCE WHERE COMPANY_CODE = ? AND LOCATION_CODE = ? AND SEQ_KEY = ? ";
		Map<String, Object> loUser = jdbcTemplate.queryForList(lsSelect, new Object[]
			{ fsCompanyCode, fsLocationCode, fsKey }).get(0);
		lsSelect = "UPDATE MST_SEQUENCE SET NEXT_VAL = NEXT_VAL + 1 WHERE COMPANY_CODE = ? AND LOCATION_CODE = ? AND SEQ_KEY = ? ";
		jdbcTemplate.update(lsSelect, new Object[]
		{ fsCompanyCode, fsLocationCode, fsKey });
		return ((BigDecimal) loUser.get("NEXT_VAL")).longValue();
	}
	
	protected static Long getNextId(String fsKey,
			JdbcTemplate jdbcTemplate)
	{
		String lsSelect = "SELECT NEXT_VAL FROM MST_SEQUENCE WHERE SEQ_KEY = ? ";
		Map<String, Object> loUser = jdbcTemplate.queryForList(lsSelect, new Object[]
			{ fsKey }).get(0);
		lsSelect = "UPDATE MST_SEQUENCE SET NEXT_VAL = NEXT_VAL + 1 WHERE SEQ_KEY = ? ";
		jdbcTemplate.update(lsSelect, new Object[]
		{ fsKey });
		return ((BigDecimal) loUser.get("NEXT_VAL")).longValue();
	}
}
