package com.framework.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Embeddable
public class MstUserKey implements Serializable {

	private static final long serialVersionUID = -723583058586873479L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	@Column(updatable = false)
	@NotNull(message = "Company code is required")
	String company_code;
	
	@Column(updatable = false)
	@NotNull(message = "User code is Required")
	String user_code;
	
	public MstUserKey() {

	}
	
	public MstUserKey(String company_code, String user_code ) {
		this.setCompany_code(company_code);
		this.setUser_code(user_code);
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MstUserKey [company_code=" + company_code + ", user_code=" + user_code + "]";
	}

	

	
	
}