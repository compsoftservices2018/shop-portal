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

import com.compsoft.shop.model.TranBill;
import com.compsoft.shop.model.TranBillDetail;
import com.compsoft.shop.model.TranBillKey;
import com.compsoft.shop.model.TranBillPayment;
import com.framework.utils.AppConstants;
import com.framework.utils.AppUtils;

/**
 * @author Pradeep Chawadkar
 *
 */
@Repository("TranBillDao")
public class TranBillDaoImpl implements TranBillDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void addBill(Session foSession, TranBill foTranBill) {
		//Session loSession = sessionFactory.getCurrentSession();
		foSession.saveOrUpdate(foTranBill);
	}

	public void addPayment(Session foSession, TranBillPayment foTranBillPayment) {
		//Session loSession = sessionFactory.getCurrentSession();
		foSession.saveOrUpdate(foTranBillPayment);
	}

	public void deleteBillDetail(Session foSession, TranBillDetail foTranBillDetail) {
		//Session loSession = sessionFactory.getCurrentSession();
		foSession.delete(foTranBillDetail);
	}

	public void deleteBillPayment(Session foSession, TranBillPayment foTranBillPayment) {
		//Session loSession = sessionFactory.getCurrentSession();
		foSession.delete(foTranBillPayment);
	}

	/*
	 * public void deleteBill(Session foSession, TranBill foTranBill) { Session
	 * loSession = sessionFactory.getCurrentSession();
	 * loSession.delete(foTranBill); }
	 */

	public List<Map<String, Object>> getBills(TranBill foTranBill) {
		try {
			String lsSelect = "SELECT   TRAN_BILL_HEADER.COMPANY_CODE, TRAN_BILL_HEADER.CUSTOMER_CODE, "
					+ " TRAN_BILL_HEADER.CUSTOMER_NAME, TRAN_BILL_HEADER.SCHEDULED_DELIVERY_DATE, TRAN_BILL_HEADER.DELIVERED_BY, "
					+ " TRAN_BILL_HEADER.BILL_NO,  TRAN_BILL_HEADER.ORDER_NO, TRAN_BILL_HEADER.STATUS, TRAN_BILL_HEADER.BILL_DATE, "
					+ " SUM(nvl(MRP,0)) MRP, SUM(nvl(SELLING_PRICE,0)) SELLING_PRICE, "
					+ " SUM(nvl(DISCOUNT,0)) DISCOUNT, SUM(nvl(BV,0)) BV, " 
					+ " SUM(nvl(BILL_QTY,0)) BILL_QTY, SUM(nvl(ORDER_QTY,0)) ORDER_QTY,"
					+ " SUM(nvl(RETURNED_QTY,0)) RETURNED_QTY, SUM(nvl(MRP-DISCOUNT,0)) BILL_AMT "
					+ " FROM TRAN_BILL_HEADER, TRAN_BILL_DETAIL, TRAN_BILL_PAYMENT "
					+ " WHERE TRAN_BILL_HEADER.COMPANY_CODE = TRAN_BILL_DETAIL.COMPANY_CODE  "
					+ " AND TRAN_BILL_HEADER.BILL_NO = TRAN_BILL_DETAIL.BILL_NO  "
					+ " AND  TRAN_BILL_HEADER.COMPANY_CODE = TRAN_BILL_PAYMENT.COMPANY_CODE  "
					+ " AND TRAN_BILL_HEADER.BILL_NO = TRAN_BILL_PAYMENT.BILL_NO  "
					+ " AND TRAN_BILL_HEADER.COMPANY_CODE = ?   "
					//+ " AND (TRAN_BILL_HEADER.CUSTOMER_CODE = ? OR ' ' = ?)  "
					+ " AND (TRAN_BILL_HEADER.STATUS = ? OR ' ' = ?) "
					+ " AND ((TO_DATE(TRAN_BILL_HEADER.BILL_DATE,'DD-MON-YY') BETWEEN ? AND ? ) OR ' ' = ?)  "
					+ " GROUP BY  TRAN_BILL_HEADER.COMPANY_CODE, TRAN_BILL_HEADER.CUSTOMER_CODE,  "
					+ " TRAN_BILL_HEADER.CUSTOMER_NAME, TRAN_BILL_HEADER.SCHEDULED_DELIVERY_DATE, TRAN_BILL_HEADER.DELIVERED_BY,  "
					+ " TRAN_BILL_HEADER.BILL_NO,  TRAN_BILL_HEADER.ORDER_NO, TRAN_BILL_HEADER.STATUS, TRAN_BILL_HEADER.BILL_DATE ";
			List<Map<String, Object>> lsReturn;
			lsReturn = jdbcTemplate.queryForList(lsSelect,
					new Object[] { foTranBill.getTranBillKey().getCompany_code(),
							//AppUtils.convertNullToSpace(foTranBill.getCustomer_code()),
							//AppUtils.convertNullToSpace(foTranBill.getCustomer_code()),
							AppUtils.convertNullToSpace(foTranBill.getStatus()),
							AppUtils.convertNullToSpace(foTranBill.getStatus()),
							AppUtils.convertNullToSpace(AppUtils.getDateForQuery(foTranBill.getBill_date())),
							AppUtils.convertNullToSpace(AppUtils.getDateForQuery(foTranBill.getBill_to_date())),
							AppUtils.convertNullToSpace(AppUtils.getDateForQuery(foTranBill.getBill_to_date())) });

			if (lsReturn.size() <= 0) {
				return null;
			}
			return lsReturn;

		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public List<Map<String, Object>> getBillPayment(TranBillPayment foBillPayment) {
		try {
			String lsSelect = "SELECT * FROM TRAN_BILL_PAYMENT " + "	WHERE COMPANY_CODE = ? "
					+ " AND (ORDER_NO = ? OR ' ' = ?) " + " AND (PAYMENT_ID = ? OR ' ' = ?) "
					+ " AND (RZR_BILL_ID = ? OR ' ' = ?) " + " AND (STATUS = ? OR ' ' = ?) ";
			List<Map<String, Object>> lsReturn;
			lsReturn = jdbcTemplate.queryForList(lsSelect,
					new Object[] { foBillPayment.getTranBillPaymentKey().getCompany_code(),
							AppUtils.convertNullToSpace(foBillPayment.getTranBillPaymentKey().getBill_no()),
							AppUtils.convertNullToSpace(foBillPayment.getTranBillPaymentKey().getBill_no()),
							AppUtils.convertNullToSpace(foBillPayment.getRzr_order_id()),
							AppUtils.convertNullToSpace(foBillPayment.getRzr_order_id()),
							AppUtils.convertNullToSpace(foBillPayment.getTranBillPaymentKey().getPayment_id()),
							AppUtils.convertNullToSpace(foBillPayment.getTranBillPaymentKey().getPayment_id()),
							AppUtils.convertNullToSpace(foBillPayment.getStatus()),
							AppUtils.convertNullToSpace(foBillPayment.getStatus()) });

			if (lsReturn.size() <= 0) {
				return null;
			}
			return lsReturn;

		} catch (

		EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public TranBill getBill(TranBillKey foBillKey) {
		TranBill loBill = new TranBill();
		try {
			String lsSelect = "SELECT *  FROM TRAN_BILL_HEADER WHERE COMPANY_CODE = ?  " + " AND BILL_NO = ?";
			List<Map<String, Object>> lsReturn;
			lsReturn = jdbcTemplate.queryForList(lsSelect,
					new Object[] { foBillKey.getCompany_code(), AppUtils.convertNullToSpace(foBillKey.getBill_no()) });
			if (lsReturn.size() <= 0) {
				return null;
			}
			loBill.getTranBillKey().setCompany_code((String) lsReturn.get(0).get("company_code"));
			loBill.getTranBillKey().setBill_no((String) lsReturn.get(0).get("bill_no"));
			loBill.setCustomer_code((String) lsReturn.get(0).get("customer_code"));
			loBill.setCustomer_name((String) lsReturn.get(0).get("customer_name"));
			loBill.setDelivery_address((String) lsReturn.get(0).get("delivery_address"));
			loBill.setBill_date((Timestamp) lsReturn.get(0).get("bill_date"));
			loBill.setPin((String) lsReturn.get(0).get("pin"));
			loBill.setMobile((String) lsReturn.get(0).get("mobile"));
			loBill.setEmail((String) lsReturn.get(0).get("email"));
			loBill.setStatus((String) lsReturn.get(0).get("status"));
			loBill.setOrder_no((String) lsReturn.get(0).get("order_no"));
			loBill.setScheduled_delivery_date((Timestamp) lsReturn.get(0).get("scheduled_delivery_date"));
			loBill.setScheduled_delivery_time((String) lsReturn.get(0).get("scheduled_delivery_time"));
			loBill.setRemark((String) lsReturn.get(0).get("remark"));
			loBill.setCancellation_date((Timestamp) lsReturn.get(0).get("cancellation_date"));
			loBill.setDelivery_date((Timestamp) lsReturn.get(0).get("delivery_date"));
			loBill.setDelivered_by((String) lsReturn.get(0).get("delivered_by"));
			loBill.setDelivered_to((String) lsReturn.get(0).get("delivered_to"));
			loBill.setBill_type((String) lsReturn.get(0).get("bill_type"));
			
			lsSelect = "SELECT *  FROM TRAN_BILL_DETAIL WHERE COMPANY_CODE = ? AND  BILL_NO = ? ";
			List<Map<String, Object>> lsReturnDetail = jdbcTemplate.queryForList(lsSelect,
					new Object[] { foBillKey.getCompany_code(), foBillKey.getBill_no() });

			List<TranBillDetail> loCustBillList = loBill.getTranBillDetailList();
			BigDecimal lbdTotSellingPrice = BigDecimal.ZERO;
			BigDecimal lbdTotProducts = BigDecimal.ZERO;
			BigDecimal lbdTotOrderQty = BigDecimal.ZERO;
			BigDecimal lbdTotBillQty = BigDecimal.ZERO;
			BigDecimal lbdTotReturnedQty = BigDecimal.ZERO;
			BigDecimal lbdTotMrp = BigDecimal.ZERO;
			BigDecimal lbdTotLandingCost = BigDecimal.ZERO;
			BigDecimal lbdTotDiscount = BigDecimal.ZERO;
			BigDecimal lbdTotBv = BigDecimal.ZERO;
			BigDecimal lbdTotDueAmt = BigDecimal.ZERO;
			if (lsReturnDetail != null) {
				for (Map<String, Object> loCustBillListRow : lsReturnDetail) {
					TranBillDetail loTranBillDetail = new TranBillDetail();

					loTranBillDetail.getTranBillDetailKey()
							.setCompany_code((String) loCustBillListRow.get("company_code"));
					loTranBillDetail.getTranBillDetailKey().setBill_no((String) loCustBillListRow.get("bill_no"));
					loTranBillDetail.getTranBillDetailKey()
							.setProduct_code((String) loCustBillListRow.get("product_code"));
					loTranBillDetail.getTranBillDetailKey()
							.setVendor_code((String) loCustBillListRow.get("vendor_code"));
					loTranBillDetail.getTranBillDetailKey()
							.setSelling_price((BigDecimal) loCustBillListRow.get("selling_price"));

					loTranBillDetail.setAlt_product_code((String) loCustBillListRow.get("alt_product_code"));
					loTranBillDetail.setCess_percentage((BigDecimal) loCustBillListRow.get("cess_percentage"));
					loTranBillDetail.setGst_percentage((BigDecimal) loCustBillListRow.get("gst_percentage"));
					loTranBillDetail.setHsn_code((String) loCustBillListRow.get("hsn_code"));
					loTranBillDetail.setMrp((BigDecimal) loCustBillListRow.get("mrp"));
					loTranBillDetail.setBill_qty((BigDecimal) loCustBillListRow.get("bill_qty"));
					loTranBillDetail.setOrder_qty((BigDecimal) loCustBillListRow.get("order_qty"));
					loTranBillDetail.setReturned_qty((BigDecimal) loCustBillListRow.get("returned_qty"));
					loTranBillDetail.setProduct_name((String) loCustBillListRow.get("product_name"));
					loTranBillDetail.setLanding_cost((BigDecimal) loCustBillListRow.get("landing_cost"));
					loTranBillDetail.setDisc_per((BigDecimal) loCustBillListRow.get("disc_per"));
					loTranBillDetail.setBv_per((BigDecimal) loCustBillListRow.get("bv_per"));
					loTranBillDetail.setDiscount((BigDecimal) loCustBillListRow.get("discount"));
					loTranBillDetail.setBv((BigDecimal) loCustBillListRow.get("bv"));

					lbdTotSellingPrice = lbdTotSellingPrice.add(loTranBillDetail.getTranBillDetailKey()
							.getSelling_price().multiply(loTranBillDetail.getBill_qty()));
					lbdTotProducts = lbdTotProducts.add(new BigDecimal("1"));
					lbdTotOrderQty = lbdTotOrderQty.add(loTranBillDetail.getOrder_qty());
					lbdTotBillQty = lbdTotBillQty.add(loTranBillDetail.getBill_qty());
					lbdTotReturnedQty = lbdTotReturnedQty.add(loTranBillDetail.getReturned_qty());
					lbdTotMrp = lbdTotMrp.add(loTranBillDetail.getMrp().multiply(loTranBillDetail.getBill_qty()));
					lbdTotLandingCost = lbdTotLandingCost
							.add(loTranBillDetail.getLanding_cost().multiply(loTranBillDetail.getBill_qty()));
					lbdTotDiscount = lbdTotDiscount
							.add(loTranBillDetail.getDiscount().multiply(loTranBillDetail.getBill_qty()));
					lbdTotBv = lbdTotDiscount.add(loTranBillDetail.getBv().multiply(loTranBillDetail.getBill_qty()));
					loCustBillList.add(loTranBillDetail);

				}
				loBill.setTot_selling_price(lbdTotSellingPrice);
				loBill.setTot_products(lbdTotProducts);
				loBill.setTot_order_qty(lbdTotOrderQty);
				loBill.setTot_bill_qty(lbdTotBillQty);
				loBill.setTot_returned_qty(lbdTotReturnedQty);
				loBill.setTot_mrp(lbdTotMrp);
				loBill.setTot_landing_cost(lbdTotLandingCost);
				loBill.setTot_discount(lbdTotDiscount);
				loBill.setTot_bv(lbdTotBv);
				// loBill.setTot_payment_amt(loBill.getTot_selling_price());
				loBill.setTranBillDetailList(loCustBillList);
			}

			lsSelect = "SELECT *  FROM TRAN_BILL_PAYMENT WHERE COMPANY_CODE = ?  AND  BILL_NO = ? ORDER BY PAYMENT_ID";
			List<Map<String, Object>> lsReturnPayment = jdbcTemplate.queryForList(lsSelect,
					new Object[] { foBillKey.getCompany_code(), foBillKey.getBill_no() });

			List<TranBillPayment> loBillPayment = loBill.getTranBillPaymentList();
			if (lsReturnPayment != null) {
				BigDecimal lbdTotPayment = BigDecimal.ZERO;
				for (Map<String, Object> loPaymentRow : lsReturnPayment) {

					TranBillPayment loTranBillPayment = new TranBillPayment();
					loTranBillPayment.getTranBillPaymentKey()
							.setCompany_code((String) loPaymentRow.get("company_code"));
					loTranBillPayment.getTranBillPaymentKey().setBill_no((String) loPaymentRow.get("bill_no"));
					loTranBillPayment.getTranBillPaymentKey().setPayment_id((String) loPaymentRow.get("payment_id"));
					loTranBillPayment.setPayment_amt((BigDecimal) loPaymentRow.get("payment_amt"));
					loTranBillPayment.setPayment_date((Timestamp) loPaymentRow.get("payment_date"));
					loTranBillPayment.setPayment_mode((String) loPaymentRow.get("payment_mode"));
					loTranBillPayment.setRemark((String) loPaymentRow.get("remark"));
					loTranBillPayment.setRzr_order_id((String) loPaymentRow.get("rzr_order_id"));
					loTranBillPayment.setRzr_order_status((String) loPaymentRow.get("rzr_order_status"));
					loTranBillPayment.setRzr_payment_id((String) loPaymentRow.get("rzr_payment_id"));
					loTranBillPayment.setRzr_sign((String) loPaymentRow.get("rzr_sign"));
					loTranBillPayment.setStatus((String) loPaymentRow.get("status"));
					loTranBillPayment.setSub_status((String) loPaymentRow.get("sub_status"));
					lbdTotPayment.add((BigDecimal) loPaymentRow.get("payment_amt"));
					if (((String) loPaymentRow.get("status")).equals(AppConstants.STATUS_PAYMENT_PAID)) {
						lbdTotPayment = lbdTotPayment.add(loTranBillPayment.getPayment_amt());
					}
					loBillPayment.add(loTranBillPayment);
				}
				loBill.setTot_payment_amt(lbdTotPayment);
				loBill.setTot_due_amt(loBill.getTot_selling_price().subtract(loBill.getTot_payment_amt()));
			}
			return loBill;
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public List<Map<String, Object>> getBillDetails(TranBill foTranBill) {
		try {
			String lsSelect = "select TRAN_BILL_HEADER.*, TRAN_BILL_DETAIL.* "
					+ " FROM TRAN_BILL_HEADER, TRAN_BILL_DETAIL  "
					+ " WHERE TRAN_BILL_HEADER.COMPANY_CODE = TRAN_BILL_DETAIL.COMPANY_CODE  "
					+ " AND TRAN_BILL_HEADER.ORDER_NO = TRAN_BILL_DETAIL.ORDER_NO  "
					+ " AND TRAN_BILL_HEADER.COMPANY_CODE = ?  "
					+ " AND (TRAN_BILL_HEADER.CUSTOMER_CODE = ? OR ' ' = ?) "
					+ " AND (TRAN_BILL_HEADER.STATUS = ? OR ' ' = ?)"
					+ " AND ((TO_DATE(TRAN_BILL_HEADER.ORDER_DATE,'DD-MON-YY') BETWEEN ? AND ? ) OR ' ' = ?) "
					+ " ORDER BY TRAN_BILL_HEADER.ORDER_NO DESC, TRAN_BILL_DETAIL.PRODUCT_NAME    ";

			List<Map<String, Object>> lsReturn;
			lsReturn = jdbcTemplate.queryForList(lsSelect,
					new Object[] { foTranBill.getTranBillKey().getCompany_code(),
							AppUtils.convertNullToSpace(foTranBill.getCustomer_code()),
							AppUtils.convertNullToSpace(foTranBill.getCustomer_code()),
							AppUtils.convertNullToSpace(foTranBill.getStatus()),
							AppUtils.convertNullToSpace(foTranBill.getStatus()),
							AppUtils.convertNullToSpace(AppUtils.getDateForQuery(foTranBill.getBill_date())),
							AppUtils.convertNullToSpace(AppUtils.getDateForQuery(foTranBill.getBill_to_date())),
							AppUtils.convertNullToSpace(AppUtils.getDateForQuery(foTranBill.getBill_date())) });

			if (lsReturn.size() <= 0) {
				return null;
			}
			return lsReturn;

		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public TranBill getBillHeader(TranBillKey foBillKey) {
		TranBill loBill = new TranBill();
		try {
			String lsSelect = "SELECT *  FROM TRAN_BILL_HEADER WHERE COMPANY_CODE = ?  " + " AND BILL_NO = ?";
			List<Map<String, Object>> lsReturn;
			lsReturn = jdbcTemplate.queryForList(lsSelect,
					new Object[] { foBillKey.getCompany_code(), AppUtils.convertNullToSpace(foBillKey.getBill_no()) });
			if (lsReturn.size() <= 0) {
				return null;
			}
			loBill.getTranBillKey().setCompany_code((String) lsReturn.get(0).get("company_code"));
			loBill.getTranBillKey().setBill_no((String) lsReturn.get(0).get("bill_no"));
			loBill.setCustomer_code((String) lsReturn.get(0).get("customer_code"));
			loBill.setCustomer_name((String) lsReturn.get(0).get("customer_name"));
			loBill.setDelivery_address((String) lsReturn.get(0).get("delivery_address"));
			loBill.setBill_date((Timestamp) lsReturn.get(0).get("bill_date"));
			loBill.setPin((String) lsReturn.get(0).get("pin"));
			loBill.setMobile((String) lsReturn.get(0).get("mobile"));
			loBill.setEmail((String) lsReturn.get(0).get("email"));
			loBill.setStatus((String) lsReturn.get(0).get("status"));
			loBill.setOrder_no((String) lsReturn.get(0).get("order_no"));
			loBill.setScheduled_delivery_date((Timestamp) lsReturn.get(0).get("scheduled_delivery_date"));
			loBill.setScheduled_delivery_time((String) lsReturn.get(0).get("scheduled_delivery_time"));
			loBill.setRemark((String) lsReturn.get(0).get("remark"));
			loBill.setCancellation_date((Timestamp) lsReturn.get(0).get("cancellation_date"));
			loBill.setDelivery_date((Timestamp) lsReturn.get(0).get("delivery_date"));
			loBill.setDelivered_by((String) lsReturn.get(0).get("delivered_by"));
			loBill.setDelivered_to((String) lsReturn.get(0).get("delivered_to"));
				
			
			return loBill;
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}
	
}
