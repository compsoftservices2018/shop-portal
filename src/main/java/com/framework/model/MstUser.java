package com.framework.model;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

import com.framework.model.MstUserKey;
import com.framework.utils.FrameworkConstants;

@Entity
@Table(name = "mst_user")
public class MstUser implements Serializable {

	private static final long serialVersionUID = -723583058586873479L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Valid
	@EmbeddedId
	MstUserKey MstUserKey;

	@Valid
	@Embedded
	EmbAudit EmbAudit;

	public MstUser() {

		MstUserKey = new MstUserKey();
		setMstUserKey(MstUserKey);
		EmbAudit = new EmbAudit();
		setEmbAudit(EmbAudit);
		this.setStatus(FrameworkConstants.STATUS_ACTIVE);
	}

	@NotNull(message = "User name is Required")
	@Size(max = 50, message = "User name can not be more than 50 characters.")
	String user_name;

	@NotNull(message = "Password is Required")
	String password;

	@NotNull(message = "User type is Required")
	@Size(max = 12, message = "User type can not be more than 5 characters.")
	String user_type;

	String mobile;

	String email;

	@NotNull(message = "Status is required")
	String status;

	String vendor_code;

	/**
	 * @return the mstUserKey
	 */
	public MstUserKey getMstUserKey() {
		return MstUserKey;
	}

	/**
	 * @param mstUserKey
	 *            the mstUserKey to set
	 */
	public void setMstUserKey(MstUserKey mstUserKey) {
		MstUserKey = mstUserKey;
	}

	/**
	 * @return the embAudit
	 */
	public EmbAudit getEmbAudit() {
		return EmbAudit;
	}

	/**
	 * @param embAudit
	 *            the embAudit to set
	 */
	public void setEmbAudit(EmbAudit embAudit) {
		EmbAudit = embAudit;
	}

	/**
	 * @return the user_name
	 */
	public String getUser_name() {
		return user_name;
	}

	/**
	 * @param user_name
	 *            the user_name to set
	 */
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the user_type
	 */
	public String getUser_type() {
		return user_type;
	}

	/**
	 * @param user_type
	 *            the user_type to set
	 */
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile
	 *            the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	public String getVendor_code() {
		return vendor_code;
	}

	public void setVendor_code(String vendor_code) {
		this.vendor_code = vendor_code;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MstUser [MstUserKey=" + MstUserKey + ", EmbAudit=" + EmbAudit + ", user_name=" + user_name
				+ ", password=" + password + ", user_type=" + user_type + ", mobile=" + mobile + ", email=" + email
				+ ", status=" + status + "]";
	}

}
