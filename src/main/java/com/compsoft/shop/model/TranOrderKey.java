package com.compsoft.shop.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.validator.constraints.NotEmpty;

@Embeddable
public class TranOrderKey implements Serializable
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
	@NotEmpty(message = "Order number is required")
	String order_no;

	public TranOrderKey(){
		
	}
	
	public TranOrderKey(String fsCompanyCode, String fsOrderNo){
		this.setCompany_code(fsCompanyCode);
		this.setOrder_no(fsOrderNo);
	}

	public String getCompany_code() {
		return company_code;
	}

	public void setCompany_code(String company_code) {
		this.company_code = company_code;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	
	
}