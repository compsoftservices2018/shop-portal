package com.compsoft.shop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.Valid;

import com.framework.utils.AppConstants;
import com.framework.utils.AppUtils;;

/**
 * @author Pradeep Chawadkar
 *
 */
@Entity
@Table(name = "tran_bill_payment")
public class TranBillPayment implements Serializable
{

	private static final long serialVersionUID = -723583058586873479L;

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	@Valid
	@EmbeddedId
	TranBillPaymentKey TranBillPaymentKey;

	String payment_mode;
	String rzr_order_id;
	String rzr_order_status;
	String rzr_payment_id;
	String rzr_sign;
	String status;
	String sub_status;
	
	String remark;
	Timestamp payment_date;
	BigDecimal payment_amt;
	
	public TranBillPayment()
	{
		TranBillPaymentKey = new TranBillPaymentKey();
		setTranBillPaymentKey(TranBillPaymentKey);
	}

	public TranBillPaymentKey getTranBillPaymentKey() {
		return TranBillPaymentKey;
	}

	public void setTranBillPaymentKey(TranBillPaymentKey tranBillPaymentKey) {
		TranBillPaymentKey = tranBillPaymentKey;
	}

	public String getPayment_mode() {
		if (payment_mode == null)
			payment_mode = AppConstants.PAYMENT_MODE_CASH;
		return payment_mode;
	}

	public void setPayment_mode(String payment_mode) {
		this.payment_mode = payment_mode;
	}

	public String getRzr_order_id() {
		return rzr_order_id;
	}

	public void setRzr_order_id(String rzr_order_id) {
		this.rzr_order_id = rzr_order_id;
	}

	public String getRzr_order_status() {
		return rzr_order_status;
	}

	public void setRzr_order_status(String rzr_order_status) {
		this.rzr_order_status = rzr_order_status;
	}

	public String getRzr_payment_id() {
		return rzr_payment_id;
	}

	public void setRzr_payment_id(String rzr_payment_id) {
		this.rzr_payment_id = rzr_payment_id;
	}

	public String getRzr_sign() {
		return rzr_sign;
	}

	public void setRzr_sign(String rzr_sign) {
		this.rzr_sign = rzr_sign;
	}

	public String getStatus() {
		if (status == null)
			status = AppConstants.STATUS_PAYMENT_PENDING;
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Timestamp getPayment_date() {
		return payment_date;
	}

	public void setPayment_date(Timestamp payment_date) {
		if (payment_date ==  null)
			payment_date =  AppUtils.getCurrentDate();
		this.payment_date = payment_date;
	}

	public BigDecimal getPayment_amt() {
		if (payment_amt == null)
			payment_amt = BigDecimal.ZERO;
		return payment_amt.setScale(2);
	}

	public void setPayment_amt(BigDecimal payment_amt) {
		this.payment_amt = payment_amt;
	}

	public String getSub_status() {
		return sub_status;
	}

	public void setSub_status(String sub_status) {
		this.sub_status = sub_status;
	}

	

}
