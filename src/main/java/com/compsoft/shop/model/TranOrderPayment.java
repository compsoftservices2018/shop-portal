package com.compsoft.shop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.framework.utils.AppConstants;
import com.framework.utils.AppUtils;;

/**
 * @author Pradeep Chawadkar
 *
 */
@Entity
@Table(name = "tran_order_payment")
public class TranOrderPayment implements Serializable
{

	private static final long serialVersionUID = -723583058586873479L;

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	@EmbeddedId
	TranOrderPaymentKey TranOrderPaymentKey;

	String payment_mode;
	String customer_code;
	String rzr_order_id;
	String rzr_order_status;
	String rzr_payment_id;
	String rzr_sign;
	String status;
	String remark;
	Timestamp payment_date;
	BigDecimal payment_amt;
	
	public TranOrderPayment()
	{
		TranOrderPaymentKey = new TranOrderPaymentKey();
		setTranOrderPaymentKey(TranOrderPaymentKey);
	}

	public TranOrderPaymentKey getTranOrderPaymentKey() {
		return TranOrderPaymentKey;
	}

	public void setTranOrderPaymentKey(TranOrderPaymentKey tranOrderPaymentKey) {
		TranOrderPaymentKey = tranOrderPaymentKey;
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
		return status;
	}

	public void setStatus(String status) {
		if (status == null)
			status = AppConstants.STATUS_PAYMENT_PENDING;
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

	public String getCustomer_code() {
		return customer_code;
	}

	public void setCustomer_code(String customer_code) {
		this.customer_code = customer_code;
	}

	

}
