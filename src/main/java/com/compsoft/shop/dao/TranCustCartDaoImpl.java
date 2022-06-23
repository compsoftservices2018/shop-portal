package com.compsoft.shop.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.compsoft.shop.model.MstCustomerKey;
import com.compsoft.shop.model.MstProduct;
import com.framework.utils.AppUtils;
import com.framework.utils.FrameworkConstants;

/**
 * @author Pradeep Chawadkar
 *
 */
@Repository("TranCustCartDao")
public class TranCustCartDaoImpl implements TranCustCartDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/*
	 * public void addCart(Session foSession, TranCustCart foTranCustCart) {
	 * Session loSession = sessionFactory.getCurrentSession();
	 * loSession.saveOrUpdate(foTranCustCart); }
	 * 
	 * public void deleteCart(Session foSession, TranCustCart foTranCustCart) {
	 * Session loSession = sessionFactory.getCurrentSession();
	 * loSession.delete(foTranCustCart); }
	 * 
	 * public TranCustCart getCart(TranCustCartKey foTranCustCartKey,
	 * MstProductKey foMstProductKey) { TranCustCart loTranCustCart = null;
	 * List<Map<String, Object>> lsReturn; String lsSelect =
	 * "SELECT *  FROM TRAN_CART_HEADER WHERE COMPANY_CODE = ? " +
	 * "AND CUSTOMER_CODE =? "; try { lsReturn =
	 * jdbcTemplate.queryForList(lsSelect, new Object[] {
	 * foTranCustCartKey.getCompany_code(), foTranCustCartKey.getCustomer_code()
	 * });
	 * 
	 * if (lsReturn.size() == 0) { return null; } loTranCustCart = new
	 * TranCustCart(); loTranCustCart.setTranCustCartKey(foTranCustCartKey);
	 * List<Map<String, Object>> lsReturnDetails = null; if (foMstProductKey ==
	 * null ) { lsSelect =
	 * "SELECT *  FROM TRAN_CART_DETAIL WHERE COMPANY_CODE = ? AND CUSTOMER_CODE = ?"
	 * ; lsReturnDetails = jdbcTemplate.queryForList(lsSelect, new Object[] {
	 * foTranCustCartKey.getCompany_code(),
	 * foTranCustCartKey.getCustomer_code()}); } else { lsSelect =
	 * "SELECT *  FROM TRAN_CART_DETAIL WHERE COMPANY_CODE = ? " +
	 * " AND CUSTOMER_CODE = ? AND PRODUCT_CODE = ? AND VENDOR_CODE = ?";
	 * lsReturnDetails = jdbcTemplate.queryForList(lsSelect, new Object[] {
	 * foTranCustCartKey.getCompany_code(),
	 * foTranCustCartKey.getCustomer_code(), foMstProductKey.getProduct_code(),
	 * foMstProductKey.getVendor_code() }); }
	 * 
	 * 
	 * if (lsReturnDetails.size() != 0) { List<TranCustCartDetail>
	 * loCustCartDetailList = new ArrayList<TranCustCartDetail>();
	 * 
	 * for (Map<String, Object> loCustCartDetailRow : lsReturnDetails) {
	 * 
	 * TranCustCartDetail loTranCustCartDetail = new TranCustCartDetail();
	 * loTranCustCartDetail.getTranCustCartDetailKey()
	 * .setCompany_code(foTranCustCartKey.getCompany_code());
	 * loTranCustCartDetail.getTranCustCartDetailKey()
	 * .setCustomer_code(foTranCustCartKey.getCustomer_code());
	 * loTranCustCartDetail.getTranCustCartDetailKey() .setProduct_code((String)
	 * loCustCartDetailRow.get("product_code"));
	 * loTranCustCartDetail.getTranCustCartDetailKey() .setVendor_code((String)
	 * loCustCartDetailRow.get("Vendor_code"));
	 * loTranCustCartDetail.setOrder_qty((BigDecimal)
	 * loCustCartDetailRow.get("Order_qty"));
	 * loCustCartDetailList.add(loTranCustCartDetail); }
	 * loTranCustCart.setTranCustCartDetail(loCustCartDetailList); }
	 * 
	 * } catch (EmptyResultDataAccessException ex) { return null; } return
	 * loTranCustCart; }
	 */
	public Map<String, Object> getCartSummary(MstCustomerKey loMstCustomerKey) {

		List<Map<String, Object>> lsReturn;
		String lsSelect = "  SELECT TRAN_ORDER_HEADER.COMPANY_CODE, " + " TRAN_ORDER_HEADER.CUSTOMER_CODE,  "
				+ " COUNT(*) TOT_PRODUCTS,  " + " SUM(TRAN_ORDER_DETAIL.ORDER_QTY) TOT_QTY,  "
				+ " SUM(nvl(MST_ONLINE_PRODUCT.LANDING_COST,0) * nvl(TRAN_ORDER_DETAIL.ORDER_QTY,0)) TOT_LANDING_COST,   "
				+ " SUM(nvl(MST_ONLINE_PRODUCT.MRP,0) * nvl(TRAN_ORDER_DETAIL.ORDER_QTY,0)) TOT_MRP,   "
				+ " SUM(nvl(MST_ONLINE_PRODUCT.SELLING_PRICE,0) * nvl(TRAN_ORDER_DETAIL.ORDER_QTY,0)) TOT_SELLING_PRICE,   "
				+ " SUM(nvl(MST_ONLINE_PRODUCT.DISCOUNT,0) * nvl(TRAN_ORDER_DETAIL.ORDER_QTY,0)) TOT_DISCOUNT,   "
				+ " SUM(nvl(MST_ONLINE_PRODUCT.BV,0) * nvl(TRAN_ORDER_DETAIL.ORDER_QTY,0)) TOT_BV "
				+ " FROM TRAN_ORDER_HEADER, TRAN_ORDER_DETAIL,MST_ONLINE_PRODUCT  "
				+ " WHERE TRAN_ORDER_HEADER.COMPANY_CODE = TRAN_ORDER_DETAIL.COMPANY_CODE  "
				+ " AND TRAN_ORDER_HEADER.ORDER_NO = TRAN_ORDER_DETAIL.ORDER_NO  "
				+ " AND TRAN_ORDER_DETAIL.COMPANY_CODE =  MST_ONLINE_PRODUCT.COMPANY_CODE   "
				+ " AND TRAN_ORDER_DETAIL.PRODUCT_CODE =  MST_ONLINE_PRODUCT.PRODUCT_CODE   "
				+ " AND TRAN_ORDER_DETAIL.VENDOR_CODE =  MST_ONLINE_PRODUCT.VENDOR_CODE   "
				+ " AND TRAN_ORDER_DETAIL.ORDER_QTY <>  0  AND  TRAN_ORDER_HEADER.COMPANY_CODE = ?  "
				+ " AND   TRAN_ORDER_HEADER.CUSTOMER_CODE = ?  " + "  AND    TRAN_ORDER_HEADER.STATUS = 'DR' "
				+ " GROUP BY TRAN_ORDER_HEADER.COMPANY_CODE, TRAN_ORDER_HEADER.ORDER_NO, TRAN_ORDER_HEADER.CUSTOMER_CODE  ";
		try {
			lsReturn = jdbcTemplate.queryForList(lsSelect,
					new Object[] { loMstCustomerKey.getCompany_code(), loMstCustomerKey.getCustomer_code() });

			if (lsReturn.size() == 0) {
				return null;
			}

			return lsReturn.get(0);

		} catch (EmptyResultDataAccessException ex) {
			return null;
		}

	}

	public List<Map<String, Object>> getProductForLocation(MstProduct foProduct, String lsPin, String fsOrderNo) {
		List<Map<String, Object>> lsReturn;
		String lsSelect = FrameworkConstants.EMPTY;
		try {

			if (AppUtils.isValueEmpty(fsOrderNo)) {
				lsSelect = " SELECT 0 ORDER_QTY, MST_ONLINE_PRODUCT.* FROM MST_ONLINE_PRODUCT "
						+ " WHERE  MST_ONLINE_PRODUCT.COMPANY_CODE = ?  "
						+ " AND (MST_ONLINE_PRODUCT.GROUP_NAME = ? OR ' ' = ?)  "
						+ " AND UPPER(MST_ONLINE_PRODUCT.PRODUCT_NAME) LIKE UPPER(?)  "
						+ " AND (MST_ONLINE_PRODUCT.end_date is null "
						+ " OR SYSDATE between MST_ONLINE_PRODUCT.start_date and MST_ONLINE_PRODUCT.end_date) "
						+ " AND VENDOR_CODE IN (SELECT VENDOR_CODE FROM MST_ONLINE_VENDOR WHERE COMPANY_CODE = ? " 
                        + " AND  (DELIVERY_PINS LIKE ? OR DELIVERY_PINS IS NULL) )";

				lsReturn = jdbcTemplate.queryForList(lsSelect,
						new Object[] { foProduct.getMstProductKey().getCompany_code(),
								AppUtils.convertNullToSpace(foProduct.getGroup_name()),
								AppUtils.convertNullToSpace(foProduct.getGroup_name()),
								"%" + AppUtils.convertNullToEmpty(foProduct.getProduct_name()) + "%",
								foProduct.getMstProductKey().getCompany_code(),
								"%" + lsPin + "%" });
			} else {

				lsSelect = " SELECT nvl(B.ORDER_QTY,0) ORDER_QTY, A.* FROM MST_ONLINE_PRODUCT A "
						+ " LEFT OUTER JOIN TRAN_ORDER_DETAIL B ON A.COMPANY_CODE = B.COMPANY_CODE "
						+ " AND  A.PRODUCT_CODE = B.PRODUCT_CODE AND  A.VENDOR_CODE = B.VENDOR_CODE "
						+ " AND B.ORDER_NO = ? WHERE  A.COMPANY_CODE = ?  "
						+ "  AND (A.GROUP_NAME = ? OR ' ' = ?)   "
						+ " AND UPPER(A.PRODUCT_NAME) LIKE UPPER(?) AND (A.end_date is null  "
						+ "  OR SYSDATE between A.start_date and A.end_date)  "
						+ "  AND A.VENDOR_CODE IN (SELECT VENDOR_CODE FROM MST_ONLINE_VENDOR WHERE COMPANY_CODE = ? " 
                        + " AND  (DELIVERY_PINS LIKE ? OR DELIVERY_PINS IS NULL) )";

				lsReturn = jdbcTemplate.queryForList(lsSelect,
						new Object[] { fsOrderNo, foProduct.getMstProductKey().getCompany_code(),
								AppUtils.convertNullToSpace(foProduct.getGroup_name()),
								AppUtils.convertNullToSpace(foProduct.getGroup_name()),
								"%" + AppUtils.convertNullToEmpty(foProduct.getProduct_name()) + "%",
								foProduct.getMstProductKey().getCompany_code(),
								"%" + lsPin + "%" });

			}
			if (lsReturn.size() <= 0) {
				return null;
			}
			return lsReturn;
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}

	}

}
