package com.compsoft.shop.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.validator.constraints.NotEmpty;

@Embeddable
public class TranBillPaymentKey implements Serializable
{

	private static final long serialVersionUID = -723583058586873479L;

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	
	@Column(updatable = false)
	@NotEmpty(message = "Company code is required")
	String company_code;
	
	@Column(updatable = false)
	@NotEmpty(message = "Bill number is required")
	String bill_no;
	
	@Column(updatable = false)
	@NotEmpty(message = "Payment id is required")
	String payment_id;
	

	
	public TranBillPaymentKey()
	{
	}



	public String getCompany_code() {
		return company_code;
	}



	public void setCompany_code(String company_code) {
		this.company_code = company_code;
	}





	public String getBill_no() {
		return bill_no;
	}



	public void setBill_no(String bill_no) {
		this.bill_no = bill_no;
	}



	public String getPayment_id() {
		return payment_id;
	}



	public void setPayment_id(String payment_id) {
		this.payment_id = payment_id;
	}
	

}


