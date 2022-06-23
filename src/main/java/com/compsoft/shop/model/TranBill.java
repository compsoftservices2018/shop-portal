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

import org.hibernate.validator.constraints.NotEmpty;

import com.framework.utils.AppConstants;
import com.framework.utils.AppUtils;

/**
 * @author Pradeep Chawadkar
 *
 */
@Entity
@Table(name = "tran_bill_header")
public class TranBill implements Serializable
{

	private static final long serialVersionUID = -723583058586873479L;

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	@Valid
	@EmbeddedId
	TranBillKey TranBillKey;
	
	@Valid
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumns(
	{ @JoinColumn(name = "company_code", updatable = false),
		@JoinColumn(name = "bill_no", updatable = false)})
	List<TranBillDetail> TranBillDetailList; 
	
	@Valid
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumns(
	{ @JoinColumn(name = "company_code", updatable = false),
		@JoinColumn(name = "bill_no", updatable = false)})
	List<TranBillPayment> TranBillPaymentList; 

	String order_no;
	Timestamp bill_date;
	
	@NotEmpty(message = "Customer is required")
	String customer_code;
	
	@NotEmpty(message = "Invalid customer")
	String customer_name;
	
	String status;
	String remark;
	Timestamp cancellation_date;
	Timestamp delivery_date;
	String delivery_address;
	String delivered_by;
	String delivered_to;
	String pin;
	
	@NotEmpty(message = "Mobile number is required")
	String mobile;
	String email;
	Timestamp scheduled_delivery_date;
	String scheduled_delivery_time;
	String bill_type;
	

	@Transient
	Timestamp bill_to_date;
	@Transient
	BigDecimal tot_selling_price;
	@Transient
	BigDecimal tot_products;
	@Transient
	BigDecimal tot_order_qty;
	@Transient
	BigDecimal tot_bill_qty;
	@Transient
	BigDecimal tot_returned_qty;
	@Transient
	BigDecimal tot_mrp;
	@Transient
	BigDecimal tot_landing_cost;
	@Transient
	BigDecimal tot_discount;
	@Transient
	BigDecimal tot_bv;
	@Transient
	BigDecimal tot_payment_amt;
	@Transient
	BigDecimal tot_due_amt;
	

	
	public TranBill()
	{
		TranBillKey = new TranBillKey();
		setTranBillKey(TranBillKey);
		TranBillDetailList = new ArrayList<TranBillDetail>();
		TranBillPaymentList = new ArrayList<TranBillPayment>();
	}
	
	public TranBill(boolean emptyrow)
	{
		TranBillKey = new TranBillKey();
		this.status = AppConstants.STATUS_BILL_NOT_SUBMITTED;
		this.setTot_selling_price(BigDecimal.ZERO);
		this.setTot_products(BigDecimal.ZERO);
		this.setTot_order_qty(BigDecimal.ZERO);
		this.setTot_bill_qty(BigDecimal.ZERO);
		this.setTot_returned_qty(BigDecimal.ZERO);
		this.setTot_mrp(BigDecimal.ZERO);
		this.setTot_landing_cost(BigDecimal.ZERO);
		this.setTot_discount(BigDecimal.ZERO);
		this.setTot_bv(BigDecimal.ZERO);
		this.setTot_payment_amt(BigDecimal.ZERO);
		this.setTot_due_amt(BigDecimal.ZERO);
		setTranBillKey(TranBillKey);
		TranBillDetailList = new ArrayList<TranBillDetail>();
		TranBillDetail loTranBillDetail = new TranBillDetail();
		TranBillDetailList.add(loTranBillDetail);
		
		TranBillPaymentList = new ArrayList<TranBillPayment>();
		TranBillPayment loTranBillPayment = new TranBillPayment();
		loTranBillPayment.setPayment_amt(BigDecimal.ZERO);
		loTranBillPayment.setPayment_mode(AppConstants.PAYMENT_MODE_CASH);
		loTranBillPayment.setStatus(AppConstants.STATUS_PAYMENT_PENDING);
		TranBillPaymentList.add(loTranBillPayment);
	}


	public TranBillKey getTranBillKey() {
		return TranBillKey;
	}


	public void setTranBillKey(TranBillKey tranBillKey) {
		TranBillKey = tranBillKey;
	}


	




	public List<TranBillDetail> getTranBillDetailList() {
		return TranBillDetailList;
	}

	public void setTranBillDetailList(List<TranBillDetail> tranBillDetailList) {
		TranBillDetailList = tranBillDetailList;
	}

	public List<TranBillPayment> getTranBillPaymentList() {
		return TranBillPaymentList;
	}

	public void setTranBillPaymentList(List<TranBillPayment> tranBillPaymentList) {
		TranBillPaymentList = tranBillPaymentList;
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




	public String getStatus() {
		/*if (status == null)
			status = AppConstants.STATUS_bill_NOT_SUBMITTED;*/
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


	public Timestamp getbill_to_date() {
		return bill_to_date;
	}


	public void setbill_to_date(Timestamp bill_to_date) {
		this.bill_to_date = bill_to_date;
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





	public BigDecimal getTot_mrp() {
		if (tot_mrp == null)
			tot_mrp = BigDecimal.ZERO;
		return tot_mrp.setScale(2);
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


	public String getOrder_no() {
		return order_no;
	}


	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}




	public Timestamp getBill_to_date() {
		return bill_to_date;
	}


	public void setBill_to_date(Timestamp bill_to_date) {
		this.bill_to_date = bill_to_date;
	}


	public Timestamp getBill_date() {
		if (bill_date == null)
			bill_date = AppUtils.getCurrentDate();
			return bill_date;
	}


	public void setBill_date(Timestamp bill_date) {
		this.bill_date = bill_date;
	}

	public BigDecimal getTot_order_qty() {
		if (tot_order_qty == null)
			tot_order_qty = BigDecimal.ZERO;
		return tot_order_qty.setScale(2);
		
	}

	public void setTot_order_qty(BigDecimal tot_order_qty) {
		this.tot_order_qty = tot_order_qty;
	}

	public BigDecimal getTot_bill_qty() {
		if (tot_bill_qty == null)
			tot_bill_qty = BigDecimal.ZERO;
		return tot_bill_qty;
	}

	public void setTot_bill_qty(BigDecimal tot_bill_qty) {
		this.tot_bill_qty = tot_bill_qty;
	}

	public BigDecimal getTot_returned_qty() {
		if (tot_returned_qty == null)
			tot_returned_qty = BigDecimal.ZERO;
		return tot_returned_qty;
	}

	public void setTot_returned_qty(BigDecimal tot_returned_qty) {
		this.tot_returned_qty = tot_returned_qty;
	}

	public BigDecimal getTot_discount() {
		if (tot_discount == null)
			tot_discount = BigDecimal.ZERO;
		return tot_discount.setScale(2);
	}

	public void setTot_discount(BigDecimal tot_discount) {
		this.tot_discount = tot_discount;
	}

	public BigDecimal getTot_due_amt() {
		if (tot_due_amt == null)
			tot_due_amt = BigDecimal.ZERO;
		return tot_due_amt.setScale(2);
	}

	public void setTot_due_amt(BigDecimal tot_due_amt) {
		this.tot_due_amt = tot_due_amt;
	}

	public String getBill_type() {
		return bill_type;
	}

	public void setBill_type(String bill_type) {
		this.bill_type = bill_type;
	}

	

}
