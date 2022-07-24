package com.compsoft.shop.reference;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;

import com.compsoft.shop.model.MstCustomer;
import com.framework.reference.ReferenceUtils;
import com.framework.utils.AppConstants;
import com.framework.utils.GlobalValues;

public class AppReferenceUtils {
	public static void setAppReferenceDataOnLogin(HttpSession fsSession, JdbcTemplate jdbcTemplate) {

		try {

			// AppReferenceUtils.buildListProductGroupNames(session, fsValue,
			// fsEmptyValue).buildListProductGroupNames(fsSession,
			// jdbcTemplate);

			// setReferenceDataCycle(fsSession, jdbcTemplate);
			// getSubmittedOrder(fsSession, jdbcTemplate);
			// setCycleInfo(fsSession);
			// setReferenceDataDistributionMonths(fsSession, jdbcTemplate);
		} catch (Exception E) {
			E.printStackTrace();
		}
	}

	public static void setAppReferenceDataForAdmin(HttpSession fsSession, JdbcTemplate jdbcTemplate) {

		try {
			// ReferenceUtils.setReferenceDataOnlineCustomer(fsSession,
			// jdbcTemplate);
		} catch (Exception E) {
			E.printStackTrace();
		}
	}

	public static void setAppReferenceDataBeforeLogin(HttpSession fsSession, JdbcTemplate jdbcTemplate) {

		try {
			ReferenceUtils.setReferenceDataConfig(fsSession, jdbcTemplate);
			ReferenceUtils.setReferenceData(fsSession, jdbcTemplate);
			setReferenceDataHome(fsSession, jdbcTemplate);
			// ReferenceUtils.setReferenceDataOnlineProduct(fsSession,
			// jdbcTemplate);
			AppReferenceUtils.setReferenceDataProductGroupNames(fsSession, jdbcTemplate);
			// setReferenceDataPin(fsSession, jdbcTemplate);
		} catch (Exception E) {
			E.printStackTrace();
		}
	}

	public static void setReferenceDataPin(HttpSession fsSession, JdbcTemplate jdbcTemplate) {

		try {
			// Load configuration
			String lsSelect = "SELECT * FROM MST_PINS WHERE COMPANY_CODE = ? ";
			List<Map<String, Object>> result = jdbcTemplate.queryForList(lsSelect,
					new Object[] { GlobalValues.getCompanyCode(fsSession) });

			fsSession.setAttribute("PIN", result);
		} catch (Exception E) {
			E.printStackTrace();
		}
	}

	/*
	 * public static void setCycleInfo(HttpSession fsSession) { try {
	 * 
	 * List<Map<String, Object>> loResult = (List<Map<String, Object>>)
	 * fsSession.getAttribute("CYCLE"); Map<String, Object> loCycleInfo = new
	 * HashMap(); MstCustomer loCustomer = (MstCustomer)
	 * fsSession.getAttribute("LOGGEDINCUSTOMER"); if (loResult != null) { int i
	 * = 0; String lsDistributionCycle = "";
	 * 
	 * for (Map<String, Object> row : loResult) { i++; lsDistributionCycle =
	 * (String) row.get("DISTRIBUTION_CYCLE"); loCycleInfo.put("allow_shop",
	 * "N"); if (loCustomer != null &&
	 * lsDistributionCycle.contains(loCustomer.getDistribution_cycle())) {
	 * loCycleInfo.put("distribution_month", (Timestamp)
	 * row.get("DISTRIBUTION_MONTH")); loCycleInfo.put("online_start_date",
	 * (Timestamp) row.get("ONLINE_START_DATE"));
	 * loCycleInfo.put("online_end_date", (Timestamp)
	 * row.get("ONLINE_END_DATE")); loCycleInfo.put("distribution_cycle",
	 * lsDistributionCycle); if
	 * (AppConstants.CUST_TYPE_CUSTOMER.equals(loCustomer.getCustomer_type())) {
	 * loCycleInfo.put("allow_shop", "Y"); } loCycleInfo.put("message", (String)
	 * row.get("MESSAGE")); break; } }
	 * 
	 * } else { loCycleInfo.put("allow_shop", "N"); }
	 * 
	 * fsSession.setAttribute("CYCLE_INFO", loCycleInfo);
	 * 
	 * } catch (Exception E) { E.printStackTrace(); } }
	 */

	public static void getSubmittedOrder(HttpSession fsSession, JdbcTemplate jdbcTemplate) {
		try {
			MstCustomer loCustomer = (MstCustomer) fsSession.getAttribute("LOGGEDINCUSTOMER");
			String lsSelect = "SELECT * FROM TRAN_ORDER_HEADER WHERE COMPANY_CODE = ? AND CUSTOMER_CODE = ? AND STATUS = ?";
			List<Map<String, Object>> loSubmittedOrders = jdbcTemplate.queryForList(lsSelect,
					new Object[] { loCustomer.getMstCustomerKey().getCompany_code(),
							loCustomer.getMstCustomerKey().getCustomer_code(), AppConstants.STATUS_ORDER_SUBMITTED });

			if (loSubmittedOrders != null && loSubmittedOrders.size() > 0) {
				fsSession.setAttribute("SUBMITTED_ORDER", loSubmittedOrders.get(0));
			}

		} catch (Exception E) {
			E.printStackTrace();
		}
	}

	public static void setReferenceDataDistributionMonths(HttpSession fsSession, JdbcTemplate jdbcTemplate) {
		try {
			String lsSelect = "SELECT DISTINCT(TO_CHAR(DISTRIBUTION_MONTH,'Mon-YYYY')) CAL_DISTRIBUTION_MONTH1 , DISTRIBUTION_MONTH "
					+ " FROM TRAN_DISTRIBUTION_CYCLE WHERE COMPANY_CODE = ? ORDER BY DISTRIBUTION_MONTH DESC";
			List<Map<String, Object>> loDistributionMonths = jdbcTemplate.queryForList(lsSelect,
					new Object[] { GlobalValues.getCompanyCode(fsSession) });

			if (loDistributionMonths != null && loDistributionMonths.size() > 0) {
				fsSession.setAttribute("DISTRIBUTION_MONTH", loDistributionMonths);
			}
		} catch (Exception E) {
			E.printStackTrace();
		}
	}

	public static String buildListDistributionMonth(HttpSession fsSession) {
		String lsReturnOptions = "<option value=''></option>";
		;
		try {
			List<Map<String, Object>> loDistributionMonths = (List<Map<String, Object>>) fsSession
					.getAttribute("DISTRIBUTION_MONTH");
			;

			if (loDistributionMonths != null && loDistributionMonths.size() > 0) {

				for (Map<String, Object> loDistributionMonthsRow : loDistributionMonths) {
					lsReturnOptions = lsReturnOptions + "<option value='"
							+ (String) loDistributionMonthsRow.get("CAL_DISTRIBUTION_MONTH1") + "' >"
							+ (String) loDistributionMonthsRow.get("CAL_DISTRIBUTION_MONTH1") + "</option>";
				}
			}
			return lsReturnOptions;

		} catch (Exception E) {
			E.printStackTrace();
			return lsReturnOptions;
		}
	}

	public static void setReferenceDataProductGroupNames(HttpSession fsSession, JdbcTemplate jdbcTemplate) {
		try {
			String lsSelect = "select DISTINCT group_name  from mst_online_product"
					+ " WHERE COMPANY_CODE = ?  order by group_name ";
			List<Map<String, Object>> loResult = jdbcTemplate.queryForList(lsSelect,
					new Object[] { GlobalValues.getCompanyCode(fsSession) });
			fsSession.setAttribute("PRODUCT_GROUP", loResult);
		} catch (Exception E) {
			E.printStackTrace();
		}
	}

	public static String buildListProductGroupNames(HttpSession session, String fsValue, boolean fsEmptyValue) {
		List<Map<String, Object>> loMapProductGroup = (List<Map<String, Object>>) session.getAttribute("PRODUCT_GROUP");
		String lsReturnOptions = "";
		if (loMapProductGroup == null) {
			return lsReturnOptions;
		}

		if (fsEmptyValue) {
			lsReturnOptions = "<option value=''>All</option>";
		}

		for (Map<String, Object> loMapProductGroupRow : loMapProductGroup) {
			if (fsValue != null && fsValue.equals((String) loMapProductGroupRow.get("GROUP_NAME"))) {
				lsReturnOptions = lsReturnOptions + "<option value='" + (String) loMapProductGroupRow.get("GROUP_NAME")
						+ "' selected>" + (String) loMapProductGroupRow.get("GROUP_NAME") + "</option>";
			} else {
				lsReturnOptions = lsReturnOptions + "<option value='" + (String) loMapProductGroupRow.get("GROUP_NAME")
						+ "' >" + (String) loMapProductGroupRow.get("GROUP_NAME") + "</option>";
			}
		}
		return lsReturnOptions;
	}

	public static void setReferenceDataHome(HttpSession fsSession, JdbcTemplate jdbcTemplate) {
		try {

			String lsSelect = "SELECT * FROM ( SELECT * FROM  mst_online_product "
					+ " WHERE COMPANY_CODE = ? AND HOME_PAGE_VIEW = 'PR' )	"
					+ " WHERE  ROWNUM < 7  AND COMPANY_CODE = ? ";
			List<Map<String, Object>> loResult = jdbcTemplate.queryForList(lsSelect,
					new Object[] { GlobalValues.getCompanyCode(fsSession), GlobalValues.getCompanyCode(fsSession) });

			fsSession.setAttribute("PR_PRODUCTS", loResult);

			lsSelect = "SELECT * FROM ( SELECT * FROM  mst_online_product "
					+ " WHERE COMPANY_CODE = ? AND HOME_PAGE_VIEW = 'OP' )	"
					+ " WHERE  ROWNUM < 21  AND COMPANY_CODE = ? ";
			loResult = jdbcTemplate.queryForList(lsSelect,
					new Object[] { GlobalValues.getCompanyCode(fsSession), GlobalValues.getCompanyCode(fsSession) });

			if (loResult.size() <= 0) {
				lsSelect = "SELECT * FROM (  SELECT *   FROM   MST_ONLINE_PRODUCT "
						+ "ORDER BY DBMS_RANDOM.RANDOM)	WHERE  ROWNUM < 21  AND COMPANY_CODE = ? ";
				loResult = jdbcTemplate.queryForList(lsSelect, new Object[] { GlobalValues.getCompanyCode(fsSession) });
			}

			fsSession.setAttribute("OP_PRODUCTS", loResult);

			lsSelect = "SELECT SUBSTR(product_name, 0, 10) product_short_name, "
					+ " ROUND(((NVL(MRP,SELLING_PRICE)  - SELLING_PRICE)*100/NVL(MRP,SELLING_PRICE)),0) discount, "
					+ " mst_online_product.*	 "
					+ " FROM   MST_ONLINE_PRODUCT WHERE  ROWNUM < 13 AND COMPANY_CODE = ? AND MRP<>0 AND HOME_PAGE_VIEW = 'TO'  "
					+ " ORDER BY ROUND(((NVL(MRP,SELLING_PRICE)  - SELLING_PRICE)*100/NVL(MRP,SELLING_PRICE)),0) DESC ";

			loResult = jdbcTemplate.queryForList(lsSelect, new Object[] { GlobalValues.getCompanyCode(fsSession) });
			if (loResult.size() <= 0) {
				lsSelect = "SELECT SUBSTR(product_name, 0, 10) product_short_name, "
						+ " ROUND(((NVL(MRP,SELLING_PRICE)  - SELLING_PRICE)*100/NVL(MRP,SELLING_PRICE)),0) discount, "
						+ " mst_online_product.*	 "
						+ " FROM   MST_ONLINE_PRODUCT WHERE  ROWNUM < 13 AND COMPANY_CODE = ? AND MRP<>0   "
						+ " ORDER BY ROUND(((NVL(MRP,SELLING_PRICE)  - SELLING_PRICE)*100/NVL(MRP,SELLING_PRICE)),0) DESC ";
				loResult = jdbcTemplate.queryForList(lsSelect, new Object[] { GlobalValues.getCompanyCode(fsSession) });
			}

			fsSession.setAttribute("TO_PRODUCTS", loResult);

		} catch (Exception E) {
			E.printStackTrace();
		}
	}

	public static void setReferenceDataProducts(HttpSession fsSession, JdbcTemplate jdbcTemplate) {
		try {
			String lsSelect = "SELECT PRODUCT_CODE LOV_CODE, PRODUCT_NAME || ' / Price: ' || SELLING_PRICE LOV_VALUE, "
					+ " '{\"VENDOR_CODE\":' || VENDOR_CODE || ', \"PRODUCT_CODE\":' || PRODUCT_CODE || ', \"SELLING_PRICE\":' || SELLING_PRICE || '}' LOV_DATAKEY , MST_ONLINE_PRODUCT.* "
					+ " FROM MST_ONLINE_PRODUCT WHERE COMPANY_CODE = ? "
					+ " AND (MST_ONLINE_PRODUCT.END_DATE IS NULL OR SYSDATE BETWEEN MST_ONLINE_PRODUCT.START_DATE AND MST_ONLINE_PRODUCT.END_DATE) "
					+ " ORDER BY PRODUCT_NAME";
			List<Map<String, Object>> loProducts = jdbcTemplate.queryForList(lsSelect,
					new Object[] { GlobalValues.getCompanyCode(fsSession) });
			if (loProducts != null && loProducts.size() > 0) {
				fsSession.setAttribute("PRODUCTS", loProducts);
			}
		} catch (Exception E) {
			E.printStackTrace();
		}
	}

	public static void setReferenceDataCustomers(HttpSession fsSession, JdbcTemplate jdbcTemplate) {
		try {
			String lsSelect = "SELECT  CUSTOMER_CODE LOV_CODE, CUSTOMER_NAME LOV_VALUE, MST_ONLINE_CUSTOMER.* "
					+ " FROM MST_ONLINE_CUSTOMER WHERE  COMPANY_CODE = ? ORDER BY CUSTOMER_NAME ";
			List<Map<String, Object>> loProducts = jdbcTemplate.queryForList(lsSelect,
					new Object[] { GlobalValues.getCompanyCode(fsSession) });
			if (loProducts != null && loProducts.size() > 0) {
				fsSession.setAttribute("CUSTOMERS", loProducts);
			}
		} catch (Exception E) {
			E.printStackTrace();
		}
	}

}
