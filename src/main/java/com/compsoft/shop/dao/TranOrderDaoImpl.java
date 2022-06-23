package com.compsoft.shop.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.compsoft.shop.model.TranOrder;
import com.compsoft.shop.model.TranOrderDetail;
import com.compsoft.shop.model.TranOrderPayment;
import com.framework.utils.AppUtils;

/**
 * @author Pradeep Chawadkar
 *
 */
@Repository("TranOrderDao")
public class TranOrderDaoImpl implements TranOrderDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void addOrder(Session foSession, TranOrder foTranOrder) {
		//Session loSession = sessionFactory.getCurrentSession();
		foSession.saveOrUpdate(foTranOrder);
	}

	public void addPayment(Session foSession, TranOrderPayment foTranOrderPayment) {
		//Session loSession = sessionFactory.getCurrentSession();
		foSession.saveOrUpdate(foTranOrderPayment);
	}
	/*
	 * public void deleteOrder(Session foSession, TranOrder foTranOrder) {
	 * Session loSession = sessionFactory.getCurrentSession();
	 * loSession.delete(foTranOrder); }
	 */

	public List<Map<String, Object>> getOrders(TranOrder foTranOrder) {
		try {
			String lsSelect = "SELECT   TRAN_ORDER_HEADER.COMPANY_CODE, TRAN_ORDER_HEADER.CUSTOMER_CODE,"
					+ " TRAN_ORDER_PAYMENT.STATUS PAYMENT_STATUS,TRAN_ORDER_PAYMENT.PAYMENT_MODE, "
					+ " TRAN_ORDER_HEADER.ALT_CUSTOMER_CODE,  TRAN_ORDER_HEADER.CUSTOMER_NAME, "
					+ " TRAN_ORDER_HEADER.ORDER_NO, NVL(TRAN_ORDER_HEADER.BILL_NO,'') BILL_NO,TRAN_ORDER_HEADER.STATUS, TRAN_ORDER_HEADER.ORDER_DATE, "
					+ " COUNT(*) TOT_PRODUCTS, SUM(ORDER_QTY) TOT_ORDER_QTY,  SUM(MRP  * ORDER_QTY) TOT_MRP, "
					+ " SUM(SELLING_PRICE * ORDER_QTY) TOT_SELLING_PRICE, SUM(DISCOUNT * ORDER_QTY) TOT_DISCOUNT , "
					+ " MIN(TRAN_ORDER_DETAIL.PRODUCT_NAME) FIRST_PRODUCT "
					+ " FROM TRAN_ORDER_HEADER, TRAN_ORDER_DETAIL, TRAN_ORDER_PAYMENT "
					+ " WHERE TRAN_ORDER_HEADER.COMPANY_CODE = TRAN_ORDER_DETAIL.COMPANY_CODE "
					+ " AND TRAN_ORDER_HEADER.ORDER_NO = TRAN_ORDER_DETAIL.ORDER_NO "
					+ " AND TRAN_ORDER_HEADER.COMPANY_CODE = TRAN_ORDER_PAYMENT.COMPANY_CODE "
					+ "  AND TRAN_ORDER_HEADER.ORDER_NO = TRAN_ORDER_PAYMENT.ORDER_NO "
					+ " AND TRAN_ORDER_HEADER.STATUS <> 'DR' " + " AND TRAN_ORDER_HEADER.COMPANY_CODE = ?  "
					+ " AND (TRAN_ORDER_HEADER.CUSTOMER_CODE = ? OR ' ' = ?) "
					+ " AND (TRAN_ORDER_HEADER.STATUS = ? OR ' ' = ?)"
					+ " AND ((TO_DATE(TRAN_ORDER_HEADER.ORDER_DATE,'DD-MON-YY') BETWEEN ? AND ? ) OR ' ' = ?) "
					+ " GROUP BY  TRAN_ORDER_HEADER.COMPANY_CODE, TRAN_ORDER_HEADER.CUSTOMER_CODE, TRAN_ORDER_HEADER.CUSTOMER_NAME, TRAN_ORDER_PAYMENT.STATUS,TRAN_ORDER_PAYMENT.PAYMENT_MODE,"
					+ " TRAN_ORDER_HEADER.ORDER_NO , TRAN_ORDER_HEADER.BILL_NO ,TRAN_ORDER_HEADER.STATUS,TRAN_ORDER_HEADER.ORDER_DATE, "
					+ " TRAN_ORDER_HEADER.ALT_CUSTOMER_CODE " + " ORDER BY TRAN_ORDER_HEADER.ORDER_NO DESC ";
			List<Map<String, Object>> lsReturn;
			lsReturn = jdbcTemplate.queryForList(lsSelect,
					new Object[] { foTranOrder.getTranOrderKey().getCompany_code(),
							AppUtils.convertNullToSpace(foTranOrder.getCustomer_code()),
							AppUtils.convertNullToSpace(foTranOrder.getCustomer_code()),
							AppUtils.convertNullToSpace(foTranOrder.getStatus()),
							AppUtils.convertNullToSpace(foTranOrder.getStatus()),
							AppUtils.convertNullToSpace(AppUtils.getDateForQuery(foTranOrder.getOrder_date())),
							AppUtils.convertNullToSpace(AppUtils.getDateForQuery(foTranOrder.getOrder_to_date())),
							AppUtils.convertNullToSpace(AppUtils.getDateForQuery(foTranOrder.getOrder_to_date())) });

			if (lsReturn.size() <= 0) {
				return null;
			}
			return lsReturn;

		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public List<Map<String, Object>> getOrderPayment(TranOrderPayment foOrderPayment) {
		try {
			String lsSelect = "SELECT * FROM TRAN_ORDER_PAYMENT " + "	WHERE COMPANY_CODE = ? "
					+ " AND (CUSTOMER_CODE = ? OR ' ' = ?) " + " AND (ORDER_NO = ? OR ' ' = ?) "
					+ " AND (PAYMENT_ID = ? OR ' ' = ?) " + " AND (RZR_ORDER_ID = ? OR ' ' = ?) "
					+ " AND (STATUS = ? OR ' ' = ?) ";
			List<Map<String, Object>> lsReturn;
			lsReturn = jdbcTemplate.queryForList(lsSelect,
					new Object[] { foOrderPayment.getTranOrderPaymentKey().getCompany_code(),
							AppUtils.convertNullToSpace(foOrderPayment.getCustomer_code()),
							AppUtils.convertNullToSpace(foOrderPayment.getCustomer_code()),
							AppUtils.convertNullToSpace(foOrderPayment.getTranOrderPaymentKey().getOrder_no()),
							AppUtils.convertNullToSpace(foOrderPayment.getTranOrderPaymentKey().getOrder_no()),
							AppUtils.convertNullToSpace(foOrderPayment.getRzr_order_id()),
							AppUtils.convertNullToSpace(foOrderPayment.getRzr_order_id()),
							AppUtils.convertNullToSpace(foOrderPayment.getTranOrderPaymentKey().getPayment_id()),
							AppUtils.convertNullToSpace(foOrderPayment.getTranOrderPaymentKey().getPayment_id()),
							AppUtils.convertNullToSpace(foOrderPayment.getStatus()),
							AppUtils.convertNullToSpace(foOrderPayment.getStatus()) });

			if (lsReturn.size() <= 0) {
				return null;
			}
			return lsReturn;

		} catch (

		EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public TranOrder getOrder(TranOrder foOrder) {
		TranOrder loOrder = new TranOrder();
		try {
			String lsSelect = "SELECT *  FROM TRAN_ORDER_HEADER WHERE COMPANY_CODE = ?  "
					+ " AND (CUSTOMER_CODE = ? OR ' '  = ? ) " + " AND (ORDER_NO = ? OR ' '  = ? ) "
					+ " AND ( STATUS = ? OR  ' ' = ?)";
			List<Map<String, Object>> lsReturn;
			lsReturn = jdbcTemplate.queryForList(lsSelect,
					new Object[] { foOrder.getTranOrderKey().getCompany_code(),
							AppUtils.convertNullToSpace(foOrder.getCustomer_code()),
							AppUtils.convertNullToSpace(foOrder.getCustomer_code()),
							AppUtils.convertNullToSpace(foOrder.getTranOrderKey().getOrder_no()),
							AppUtils.convertNullToSpace(foOrder.getTranOrderKey().getOrder_no()),
							AppUtils.convertNullToSpace(foOrder.getStatus()),
							AppUtils.convertNullToSpace(foOrder.getStatus()) });
			if (lsReturn.size() <= 0) {
				return null;
			}
			loOrder.getTranOrderKey().setCompany_code((String) lsReturn.get(0).get("company_code"));
			loOrder.getTranOrderKey().setOrder_no((String) lsReturn.get(0).get("order_no"));
			loOrder.setCustomer_code((String) lsReturn.get(0).get("customer_code"));
			loOrder.setCustomer_name((String) lsReturn.get(0).get("customer_name"));
			loOrder.setDelivery_address((String) lsReturn.get(0).get("delivery_address"));
			loOrder.setOrder_date((Timestamp) lsReturn.get(0).get("order_date"));
			loOrder.setPin((String) lsReturn.get(0).get("pin"));
			loOrder.setMobile((String) lsReturn.get(0).get("mobile"));
			loOrder.setEmail((String) lsReturn.get(0).get("email"));
			loOrder.setStatus((String) lsReturn.get(0).get("status"));
			loOrder.setScheduled_delivery_date((Timestamp) lsReturn.get(0).get("scheduled_delivery_date"));
			loOrder.setScheduled_delivery_time((String) lsReturn.get(0).get("scheduled_delivery_time"));
			loOrder.setBill_no((String) lsReturn.get(0).get("bill_no"));

			lsSelect = "SELECT *  FROM TRAN_ORDER_DETAIL WHERE COMPANY_CODE = ? AND  ORDER_NO = ? AND ORDER_QTY <> 0";
			List<Map<String, Object>> lsReturnDetail = jdbcTemplate.queryForList(lsSelect, new Object[] {
					foOrder.getTranOrderKey().getCompany_code(), loOrder.getTranOrderKey().getOrder_no() });

			List<TranOrderDetail> loCustOrderList = loOrder.getTranOrderDetail();
			BigDecimal lbdTotSellingPrice = BigDecimal.ZERO;
			BigDecimal lbdTotProducts = BigDecimal.ZERO;
			BigDecimal lbdTotQty = BigDecimal.ZERO;
			BigDecimal lbdTotMrp = BigDecimal.ZERO;
			BigDecimal lbdTotLandingCost = BigDecimal.ZERO;
			BigDecimal lbdTotDiscount = BigDecimal.ZERO;
			BigDecimal lbdTotBv = BigDecimal.ZERO;
			if (lsReturnDetail != null) {
				for (Map<String, Object> loCustOrderListRow : lsReturnDetail) {
					TranOrderDetail loTranOrderDetail = new TranOrderDetail();

					loTranOrderDetail.getTranOrderDetailKey()
							.setCompany_code((String) loCustOrderListRow.get("company_code"));
					loTranOrderDetail.getTranOrderDetailKey().setOrder_no((String) loCustOrderListRow.get("order_no"));
					loTranOrderDetail.getTranOrderDetailKey()
							.setProduct_code((String) loCustOrderListRow.get("product_code"));
					loTranOrderDetail.getTranOrderDetailKey()
							.setVendor_code((String) loCustOrderListRow.get("vendor_code"));
					loTranOrderDetail.getTranOrderDetailKey().setSelling_price((BigDecimal) loCustOrderListRow.get("selling_price"));
					
					loTranOrderDetail.setAlt_product_code((String) loCustOrderListRow.get("alt_product_code"));
					loTranOrderDetail.setAlt_product_name((String) loCustOrderListRow.get("alt_product_name"));
					loTranOrderDetail.setCess_percentage((BigDecimal) loCustOrderListRow.get("cess_percentage"));
					loTranOrderDetail.setGst_percentage((BigDecimal) loCustOrderListRow.get("gst_percentage"));
					loTranOrderDetail.setHsn_code((String) loCustOrderListRow.get("hsn_code"));
					loTranOrderDetail.setMrp((BigDecimal) loCustOrderListRow.get("mrp"));
					loTranOrderDetail.setOrder_qty((BigDecimal) loCustOrderListRow.get("order_qty"));
					loTranOrderDetail.setProduct_name((String) loCustOrderListRow.get("product_name"));
					loTranOrderDetail.setLanding_cost((BigDecimal) loCustOrderListRow.get("landing_cost"));
					loTranOrderDetail.setDisc_per((BigDecimal) loCustOrderListRow.get("disc_per"));
					loTranOrderDetail.setBv_per((BigDecimal) loCustOrderListRow.get("bv_per"));
					loTranOrderDetail.setDiscount((BigDecimal) loCustOrderListRow.get("discount"));
					loTranOrderDetail.setBv((BigDecimal) loCustOrderListRow.get("bv"));
					loTranOrderDetail.setImage_name((String) loCustOrderListRow.get("image_name"));
					lbdTotSellingPrice = lbdTotSellingPrice
							.add(loTranOrderDetail.getTranOrderDetailKey().getSelling_price().multiply(loTranOrderDetail.getOrder_qty()));
					lbdTotProducts = lbdTotProducts.add(new BigDecimal("1"));
					lbdTotQty = lbdTotQty.add(loTranOrderDetail.getOrder_qty());
					lbdTotMrp = lbdTotMrp.add(loTranOrderDetail.getMrp().multiply(loTranOrderDetail.getOrder_qty()));
					lbdTotLandingCost = lbdTotLandingCost
							.add(loTranOrderDetail.getLanding_cost().multiply(loTranOrderDetail.getOrder_qty()));
					lbdTotDiscount = lbdTotDiscount
							.add(loTranOrderDetail.getDiscount().multiply(loTranOrderDetail.getOrder_qty()));
					lbdTotBv = lbdTotDiscount.add(loTranOrderDetail.getBv().multiply(loTranOrderDetail.getOrder_qty()));
					loCustOrderList.add(loTranOrderDetail);

				}
				loOrder.setTot_selling_price(lbdTotSellingPrice);
				loOrder.setTot_products(lbdTotProducts);
				loOrder.setTot_qty(lbdTotQty);
				loOrder.setTot_mrp(lbdTotMrp);
				loOrder.setTot_landing_cost(lbdTotLandingCost);
				loOrder.setTot_disc(lbdTotDiscount);
				loOrder.setTot_bv(lbdTotBv);
				loOrder.setTranOrderDetail(loCustOrderList);
			}

			lsSelect = "SELECT *  FROM TRAN_ORDER_PAYMENT WHERE COMPANY_CODE = ? " + " AND  ORDER_NO = ? ";
			List<Map<String, Object>> lsReturnPayment = jdbcTemplate.queryForList(lsSelect, new Object[] {
					foOrder.getTranOrderKey().getCompany_code(), loOrder.getTranOrderKey().getOrder_no() });

			List<TranOrderPayment> loOrderPayment = loOrder.getTranOrderPayment();
			BigDecimal lbdTotPaymentAmt = BigDecimal.ZERO;

			if (lsReturnPayment != null) {
				BigDecimal lbdTotPayment = BigDecimal.ZERO;
				for (Map<String, Object> loPaymentRow : lsReturnPayment) {

					TranOrderPayment loTranOrderPayment = new TranOrderPayment();
					loTranOrderPayment.getTranOrderPaymentKey()
							.setCompany_code((String) loPaymentRow.get("company_code"));
					loTranOrderPayment.getTranOrderPaymentKey().setOrder_no((String) loPaymentRow.get("order_no"));
					loTranOrderPayment.getTranOrderPaymentKey().setPayment_id((String) loPaymentRow.get("payment_id"));
					loTranOrderPayment.setCustomer_code((String) loPaymentRow.get("customer_code"));
					loTranOrderPayment.setPayment_amt((BigDecimal) loPaymentRow.get("payment_amt"));
					loTranOrderPayment.setPayment_date((Timestamp) loPaymentRow.get("payment_date"));
					loTranOrderPayment.setPayment_mode((String) loPaymentRow.get("payment_mode"));
					loTranOrderPayment.setRemark((String) loPaymentRow.get("remark"));
					loTranOrderPayment.setRzr_order_id((String) loPaymentRow.get("rzr_order_id"));
					loTranOrderPayment.setRzr_order_status((String) loPaymentRow.get("rzr_order_status"));
					loTranOrderPayment.setRzr_payment_id((String) loPaymentRow.get("rzr_payment_id"));
					loTranOrderPayment.setRzr_sign((String) loPaymentRow.get("rzr_sign"));
					loTranOrderPayment.setStatus((String) loPaymentRow.get("status"));
					lbdTotPayment.add((BigDecimal) loPaymentRow.get("payment_amt"));
					lbdTotPayment = lbdTotPayment.add(loTranOrderPayment.getPayment_amt());
					loOrderPayment.add(loTranOrderPayment);
				}
				loOrder.setTot_payment_amt(lbdTotPayment);

			}

			return loOrder;
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public List<Map<String, Object>> getOrderDetails(TranOrder foTranOrder) {
		try {
			String lsSelect = "select TRAN_ORDER_HEADER.*, TRAN_ORDER_DETAIL.* "
					+ " FROM TRAN_ORDER_HEADER, TRAN_ORDER_DETAIL  "
					+ " WHERE TRAN_ORDER_HEADER.COMPANY_CODE = TRAN_ORDER_DETAIL.COMPANY_CODE  "
					+ " AND TRAN_ORDER_HEADER.ORDER_NO = TRAN_ORDER_DETAIL.ORDER_NO  "
					+ " AND TRAN_ORDER_HEADER.COMPANY_CODE = ?  "
					+ " AND (TRAN_ORDER_HEADER.CUSTOMER_CODE = ? OR ' ' = ?) "
					+ " AND (TRAN_ORDER_HEADER.STATUS = ? OR ' ' = ?)"
					+ " AND ((TO_DATE(TRAN_ORDER_HEADER.ORDER_DATE,'DD-MON-YY') BETWEEN ? AND ? ) OR ' ' = ?) "
					+ " ORDER BY TRAN_ORDER_HEADER.ORDER_NO DESC, TRAN_ORDER_DETAIL.PRODUCT_NAME    ";

			List<Map<String, Object>> lsReturn;
			lsReturn = jdbcTemplate.queryForList(lsSelect,
					new Object[] { foTranOrder.getTranOrderKey().getCompany_code(),
							AppUtils.convertNullToSpace(foTranOrder.getCustomer_code()),
							AppUtils.convertNullToSpace(foTranOrder.getCustomer_code()),
							AppUtils.convertNullToSpace(foTranOrder.getStatus()),
							AppUtils.convertNullToSpace(foTranOrder.getStatus()),
							AppUtils.convertNullToSpace(AppUtils.getDateForQuery(foTranOrder.getOrder_date())),
							AppUtils.convertNullToSpace(AppUtils.getDateForQuery(foTranOrder.getOrder_to_date())),
							AppUtils.convertNullToSpace(AppUtils.getDateForQuery(foTranOrder.getOrder_date())) });

			if (lsReturn.size() <= 0) {
				return null;
			}
			return lsReturn;

		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

}
