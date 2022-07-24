package com.compsoft.shop.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.validator.constraints.NotEmpty;

@Embeddable
public class TranOrderDetailKey implements Serializable
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
	
	@Column(updatable = false)
	@NotEmpty(message = "Product code is required")
	String product_code;
	
	@Column(updatable = false)
	@NotEmpty(message = "Vendor code is required")
	String vendor_code;
	
	@Column(updatable = false)
	@NotEmpty(message = "Selling price is required")
	BigDecimal selling_price;
	
	
	public TranOrderDetailKey()
	{
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

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public String getVendor_code() {
		return vendor_code;
	}

	public void setVendor_code(String vendor_code) {
		this.vendor_code = vendor_code;
	}


	public BigDecimal getSelling_price() {
		if(selling_price == null)
			selling_price = BigDecimal.ZERO;
		return selling_price.setScale(2);
	}


	public void setSelling_price(BigDecimal selling_price) {
		this.selling_price = selling_price;
	}

	
}


