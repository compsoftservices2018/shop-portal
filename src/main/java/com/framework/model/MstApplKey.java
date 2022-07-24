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
public class MstApplKey implements Serializable {

	private static final long serialVersionUID = -723583058586873479L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	@Column(updatable = false)
	@NotNull(message = "Application code is required")
	String appl_code;
	
	public MstApplKey() {

	}
	
	public MstApplKey(String appl_code ) {
		this.setAppl_code(appl_code);

	}

	/**
	 * @return the appl_code
	 */
	public String getAppl_code() {
		return appl_code;
	}

	/**
	 * @param appl_code the appl_code to set
	 */
	public void setAppl_code(String appl_code) {
		this.appl_code = appl_code;
	}


	
}