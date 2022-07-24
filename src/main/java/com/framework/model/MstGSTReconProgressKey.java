package com.framework.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.framework.utils.ConstantsRegEx;

@Embeddable
public class MstGSTReconProgressKey implements Serializable {

	private static final long serialVersionUID = -723583058586873479L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	@Column(updatable = false)
	@NotNull(message = "Application code is required")
	String company_code;
	
	@Column(updatable = false)
	@NotNull(message = "User code is required")
	String user_code;
	
	
	public MstGSTReconProgressKey() {

	}
	
	public MstGSTReconProgressKey(String company_code, String user_code ) {
		this.setCompany_code(company_code);
		this.setCompany_code(user_code);

	}

	/**
	 * @return the company_code
	 */
	public String getCompany_code() {
		return company_code;
	}

	/**
	 * @param company_code the company_code to set
	 */
	public void setCompany_code(String company_code) {
		this.company_code = company_code;
	}

	/**
	 * @return the user_code
	 */
	public String getUser_code() {
		return user_code;
	}

	/**
	 * @param user_code the user_code to set
	 */
	public void setUser_code(String user_code) {
		this.user_code = user_code;
	}

	
}