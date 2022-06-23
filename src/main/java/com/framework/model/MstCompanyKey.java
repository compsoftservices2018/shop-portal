package com.framework.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;


@Embeddable
public class MstCompanyKey implements Serializable {

	private static final long serialVersionUID = -723583058586873479L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Column(updatable = false)
	@NotNull(message = "Company code is required")
	String company_code;

	public MstCompanyKey() {

	}

	public MstCompanyKey(String company_code) {
		this.setCompany_code(company_code);

	}

	/**
	 * @return the company_code
	 */
	public String getCompany_code() {
		return company_code;
	}

	/**
	 * @param company_code
	 *            the company_code to set
	 */
	public void setCompany_code(String company_code) {
		this.company_code = company_code;
	}

	
}