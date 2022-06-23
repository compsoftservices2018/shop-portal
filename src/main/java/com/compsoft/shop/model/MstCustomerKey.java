package com.compsoft.shop.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;


@Embeddable
public class MstCustomerKey implements Serializable {

	private static final long serialVersionUID = -723583058586873479L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Column(updatable = false)
	@NotEmpty(message = "Company code is required")
	String company_code;
	
	@Column(updatable = false)
	@NotEmpty(message = "Customer code is required")
	@Size(max=10, message = "Customer code can not be more than 10 characters")
	String customer_code;

	public MstCustomerKey() {

	}

	public MstCustomerKey(String fsCompanyCode, String fsCustomerCode) {
		this.setCompany_code(fsCompanyCode);
		this.setCustomer_code(fsCustomerCode);

	}

	public String getCompany_code() {
		return company_code;
	}

	public void setCompany_code(String company_code) {
		this.company_code = company_code;
	}

	public String getCustomer_code() {
		return customer_code;
	}

	public void setCustomer_code(String customer_code) {
		this.customer_code = customer_code;
	}



}