package com.compsoft.shop.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.compsoft.shop.model.MstProduct;
import com.compsoft.shop.model.MstProductKey;
import com.framework.utils.AppUtils;
import com.framework.utils.FrameworkConstants;

/**
 * @author Pradeep Chawadkar
 *
 */
@Repository("MstProductDao")
public class MstProductDaoImpl implements MstProductDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private void obectMapper(MstProduct loMstProduct, Map<String, Object> foResult) {
		loMstProduct.getMstProductKey().setCompany_code((String) foResult.get("company_code"));
		loMstProduct.getMstProductKey().setProduct_code((String) foResult.get("product_code"));
		loMstProduct.getMstProductKey().setSelling_price((BigDecimal) foResult.get("selling_price"));
		loMstProduct.getMstProductKey().setProduct_code((String) foResult.get("vendor_code"));

		loMstProduct.setAlt_product_name((String) foResult.get("alt_product_name"));
		loMstProduct.setAlt_product_code((String) foResult.get("alt_product_code"));
		
		loMstProduct.setStart_date((Timestamp) foResult.get("start_date"));
		loMstProduct.setCess_percentage((BigDecimal) foResult.get("cess_percentage"));
		loMstProduct.setEnd_date((Timestamp) foResult.get("end_date"));
		loMstProduct.setGroup_name((String) foResult.get("group_name"));
		loMstProduct.setGst_percentage((BigDecimal) foResult.get("gst_percentage"));
		loMstProduct.setHsn_code((String) foResult.get("hsn_code"));
		loMstProduct.setImage_name((String) foResult.get("image_name"));
		loMstProduct.setMrp((BigDecimal) foResult.get("mrp"));
		loMstProduct.setStock((BigDecimal) foResult.get("stock"));
		loMstProduct.setProduct_name((String) foResult.get("product_name"));
		loMstProduct.setAdd_information((String) foResult.get("add_information"));
		loMstProduct.setHome_page_view((String) foResult.get("home_page_view"));
		loMstProduct.setLanding_cost((BigDecimal) foResult.get("landing_cost"));
		loMstProduct.setDisc_per((BigDecimal) foResult.get("disc_per"));
		loMstProduct.setBv_per((BigDecimal) foResult.get("bv_per"));
		loMstProduct.setBv_per((BigDecimal) foResult.get("bv_per"));
		loMstProduct.setDiscount((BigDecimal) foResult.get("discount"));
		loMstProduct.setBv((BigDecimal) foResult.get("bv"));
		loMstProduct.setPage_mode(FrameworkConstants.OBJECT_MODE_UPDATE);
		

	}

	public void addProduct(Session foSession, MstProduct foProduct) {
		//Session loSession = sessionFactory.getCurrentSession();
		foSession.saveOrUpdate(foProduct);
	}

	public void delete(Session foSession, MstProduct foProduct) {
		//Session loSession = sessionFactory.getCurrentSession();
		foSession.delete(foProduct);
	}

	public JSONObject getProduct(MstProductKey foProductKey) {
		JSONObject lsReturnJObj = new JSONObject();
		List<Map<String, Object>> lsReturn;
		String lsSelect = "SELECT * " + " FROM MST_ONLINE_PRODUCT  " + " WHERE COMPANY_CODE = ? AND PRODUCT_CODE = ? "
				+ " AND vendor_code = ? AND selling_price = ? ";
		try {
			lsReturn = jdbcTemplate.queryForList(lsSelect, new Object[] { foProductKey.getCompany_code(),
					foProductKey.getProduct_code(), foProductKey.getVendor_code(),
					foProductKey.getSelling_price()});

			if (lsReturn.size() <= 0) {
				return null;
			}
			lsReturnJObj.putAll(lsReturn.get(0));
			return lsReturnJObj;
			
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public JSONObject getProduct(MstProductKey foProductKey, String fsOrderNo) {
		JSONObject lsReturnJObj = new JSONObject();
		List<Map<String, Object>> lsReturn;

		try {
			String lsSelect = FrameworkConstants.EMPTY;

			if (fsOrderNo == null) {
				lsSelect = "SELECT '' ORDER_NO, 0 ORDER_QTY, MST_ONLINE_PRODUCT.* FROM MST_ONLINE_PRODUCT "
						+ " WHERE COMPANY_CODE = ? AND PRODUCT_CODE = ? " + " AND VENDOR_CODE = ? ";
				lsReturn = jdbcTemplate.queryForList(lsSelect, new Object[] { foProductKey.getCompany_code(),
						foProductKey.getProduct_code(), foProductKey.getVendor_code() });

			} else {
				lsSelect = "SELECT B.ORDER_NO , B.ORDER_QTY, A.*   FROM MST_ONLINE_PRODUCT A "
						+ " LEFT OUTER JOIN  TRAN_ORDER_DETAIL B   ON  A.COMPANY_CODE  = B.COMPANY_CODE  "
						+ " AND A.PRODUCT_CODE  = B.PRODUCT_CODE    AND A.VENDOR_CODE  = B.VENDOR_CODE  "
                        + "  AND B.COMPANY_CODE  = ? AND  B.ORDER_NO = ? "
						 + " where A.COMPANY_CODE  = ?   AND A.PRODUCT_CODE  = ?  AND A.VENDOR_CODE  = ?";
				lsReturn = jdbcTemplate.queryForList(lsSelect, 
						new Object[] { foProductKey.getCompany_code(),fsOrderNo,
								foProductKey.getCompany_code(), 
								foProductKey.getProduct_code(),
								foProductKey.getVendor_code() });

			}

			if (lsReturn.size() <= 0) {
				return null;
			}
			lsReturnJObj.putAll(lsReturn.get(0));
			return lsReturnJObj;
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public List<Map<String, Object>> getProductList(MstProduct foProduct) {
		List<Map<String, Object>> lsReturn;

		String lsSelect = "SELECT NVL(MRP,SELLING_PRICE) CAL_MRP, NVL(STOCK,0) CAL_STOCK, "
				+ " CASE WHEN SYSDATE > END_DATE THEN 'I' END ACTIVE, MST_ONLINE_PRODUCT.*  FROM MST_ONLINE_PRODUCT "
				+ " WHERE MST_ONLINE_PRODUCT.COMPANY_CODE = ? " + " AND MST_ONLINE_PRODUCT.VENDOR_CODE = ?  "
				+ "  AND (MST_ONLINE_PRODUCT.GROUP_NAME = ? OR ' ' = ?)  "
				+ " AND UPPER(MST_ONLINE_PRODUCT.PRODUCT_NAME) LIKE UPPER(?) ORDER BY PRODUCT_NAME";
		try {
			lsReturn = jdbcTemplate.queryForList(lsSelect,
					new Object[] { foProduct.getMstProductKey().getCompany_code(),
							foProduct.getMstProductKey().getVendor_code(),
							AppUtils.convertNullToSpace(foProduct.getGroup_name()),
							AppUtils.convertNullToSpace(foProduct.getGroup_name()),
							"%" + AppUtils.convertNullToEmpty(foProduct.getProduct_name()) + "%" });

			if (lsReturn.size() <= 0) {
				return null;
			}
			return lsReturn;
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}

	}

}
