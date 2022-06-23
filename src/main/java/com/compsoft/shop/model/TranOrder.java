package com.compsoft.shop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;

/**
 * @author Pradeep Chawadkar
 *
 */
@Entity
@Table(name = "tran_order_header")
public class TranOrder implements Serializable
{

	private static final long serialVersionUID = -723583058586873479L;

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	@EmbeddedId
	TranOrderKey TranOrderKey;
	
	@Valid
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumns(
	{ @JoinColumn(name = "company_code", updatable = false),
		@JoinColumn(name = "order_no", updatable = false)})
	List<TranOrderDetail> TranOrderDetail; 
	
	@Valid
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumns(
	{ @JoinColumn(name = "company_code", updatable = false),
		@JoinColumn(name = "order_no", updatable = false)})
	List<TranOrderPayment> TranOrderPayment; 

	Timestamp order_date;
	String customer_code;
	String customer_name;
	String alt_customer_code;
	String alt_customer_name;
	
	String status;
	String remark;
	Timestamp cancellation_date;
	Timestamp delivery_date;
	String delivery_address;
	String delivered_by;
	String delivered_to;
	String pin;
	String mobile;
	String email;
	Timestamp scheduled_delivery_date;
	String scheduled_delivery_time;
	String bill_no;
	
	
	@Transient
	Timestamp order_to_date;
	@Transient
	BigDecimal tot_selling_price;
	@Transient
	BigDecimal tot_products;
	@Transient
	BigDecimal tot_qty;
	@Transient
	BigDecimal tot_mrp;
	@Transient
	BigDecimal tot_landing_cost;
	@Transient
	BigDecimal tot_disc;
	@Transient
	BigDecimal tot_bv;
	@Transient
	BigDecimal tot_payment_amt;

	
	public TranOrder()
	{
		TranOrderKey = new TranOrderKey();
		setTranOrderKey(TranOrderKey);
		TranOrderDetail = new ArrayList<TranOrderDetail>();
		TranOrderPayment = new ArrayList<TranOrderPayment>();
	}


	public TranOrderKey getTranOrderKey() {
		return TranOrderKey;
	}


	public void setTranOrderKey(TranOrderKey tranOrderKey) {
		TranOrderKey = tranOrderKey;
	}


	public List<TranOrderDetail> getTranOrderDetail() {
		return TranOrderDetail;
	}


	public void setTranOrderDetail(List<TranOrderDetail> tranOrderDetail) {
		TranOrderDetail = tranOrderDetail;
	}


	public List<TranOrderPayment> getTranOrderPayment() {
		return TranOrderPayment;
	}


	public void setTranOrderPayment(List<TranOrderPayment> tranOrderPayment) {
		TranOrderPayment = tranOrderPayment;
	}


	public Timestamp getOrder_date() {
			return order_date;
	}


	public void setOrder_date(Timestamp order_date) {
		this.order_date = order_date;
	}


	public String getCustomer_code() {
		return customer_code;
	}


	public void setCustomer_code(String customer_code) {
		this.customer_code = customer_code;
	}


	public String getCustomer_name() {
		return customer_name;
	}


	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}


	public String getAlt_customer_code() {
		return alt_customer_code;
	}


	public void setAlt_customer_code(String alt_customer_code) {
		this.alt_customer_code = alt_customer_code;
	}


	public String getAlt_customer_name() {
		return alt_customer_name;
	}


	public void setAlt_customer_name(String alt_customer_name) {
		this.alt_customer_name = alt_customer_name;
	}


	public String getStatus() {
		/*if (status == null)
			status = AppConstants.STATUS_ORDER_NOT_SUBMITTED;*/
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


	public Timestamp getCancellation_date() {
		return cancellation_date;
	}


	public void setCancellation_date(Timestamp cancellation_date) {
		this.cancellation_date = cancellation_date;
	}


	public Timestamp getDelivery_date() {
		return delivery_date;
	}


	public void setDelivery_date(Timestamp delivery_date) {
		this.delivery_date = delivery_date;
	}


	public String getDelivery_address() {
		return delivery_address;
	}


	public void setDelivery_address(String delivery_address) {
		this.delivery_address = delivery_address;
	}


	public String getDelivered_by() {
		return delivered_by;
	}


	public void setDelivered_by(String delivered_by) {
		this.delivered_by = delivered_by;
	}


	public String getDelivered_to() {
		return delivered_to;
	}


	public void setDelivered_to(String delivered_to) {
		this.delivered_to = delivered_to;
	}


	public String getPin() {
		return pin;
	}


	public void setPin(String pin) {
		this.pin = pin;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Timestamp getScheduled_delivery_date() {
		return scheduled_delivery_date;
	}


	public void setScheduled_delivery_date(Timestamp scheduled_delivery_date) {
		this.scheduled_delivery_date = scheduled_delivery_date;
	}


	public String getScheduled_delivery_time() {
		return scheduled_delivery_time;
	}


	public void setScheduled_delivery_time(String scheduled_delivery_time) {
		this.scheduled_delivery_time = scheduled_delivery_time;
	}


	public Timestamp getOrder_to_date() {
		return order_to_date;
	}


	public void setOrder_to_date(Timestamp order_to_date) {
		this.order_to_date = order_to_date;
	}


	public BigDecimal getTot_selling_price() {
		if (tot_selling_price == null)
			tot_selling_price = BigDecimal.ZERO;
		return tot_selling_price.setScale(2);
	}


	public void setTot_selling_price(BigDecimal tot_selling_price) {
		this.tot_selling_price = tot_selling_price;
	}


	public BigDecimal getTot_products() {
		if (tot_products == null)
			tot_products = BigDecimal.ZERO;
		return tot_products;
	}


	public void setTot_products(BigDecimal tot_products) {
		this.tot_products = tot_products;
	}


	public BigDecimal getTot_qty() {
		if (tot_qty == null)
			tot_qty = BigDecimal.ZERO;
		return tot_qty;
	}


	public void setTot_qty(BigDecimal tot_qty) {
		this.tot_qty = tot_qty;
	}


	public BigDecimal getTot_mrp() {
		if (tot_mrp == null)
			tot_mrp = BigDecimal.ZERO;
		return tot_mrp;
	}


	public void setTot_mrp(BigDecimal tot_mrp) {
		this.tot_mrp = tot_mrp;
	}


	public BigDecimal getTot_landing_cost() {
		if (tot_landing_cost == null)
			tot_landing_cost = BigDecimal.ZERO;
		return tot_landing_cost.setScale(2);
	}


	public void setTot_landing_cost(BigDecimal tot_landing_cost) {
		this.tot_landing_cost = tot_landing_cost;
	}


	public BigDecimal getTot_disc() {
		if (tot_disc == null)
			tot_disc = BigDecimal.ZERO;
		return tot_disc.setScale(2);
	}


	public void setTot_disc(BigDecimal tot_disc) {
		this.tot_disc = tot_disc;
	}


	public BigDecimal getTot_bv() {
		if (tot_bv == null)
			tot_bv = BigDecimal.ZERO;
		return tot_bv;
	}


	public void setTot_bv(BigDecimal tot_bv) {
		this.tot_bv = tot_bv;
	}


	public BigDecimal getTot_payment_amt() {
		if (tot_payment_amt == null)
			tot_payment_amt = BigDecimal.ZERO;
		return tot_payment_amt.setScale(2);
	}


	public void setTot_payment_amt(BigDecimal tot_payment_amt) {
		this.tot_payment_amt = tot_payment_amt;
	}


	public String getBill_no() {
		return bill_no;
	}


	public void setBill_no(String bill_no) {
		this.bill_no = bill_no;
	}


}
