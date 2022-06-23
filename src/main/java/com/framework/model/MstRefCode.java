package com.framework.model;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.framework.utils.FrameworkConstants;

@Entity
@Table(name="ref_codes")
public class MstRefCode implements Serializable{

	private static final long serialVersionUID = -723583058586873479L;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Valid
	@EmbeddedId
	MstRefCodeKey MstRefCodeKey;
	
	@Valid
	@Embedded
	EmbAudit EmbAudit;
	
	@NotNull(message = "Name is required")
	String ref_name;
	@NotNull(message = "Status is required")
	String status;

	public MstRefCode()
	{
		
		MstRefCodeKey = new MstRefCodeKey();
		setMstRefCodeKey(MstRefCodeKey);
		EmbAudit = new EmbAudit();
		setEmbAudit(EmbAudit);
		this.setStatus(FrameworkConstants.STATUS_ACTIVE);
	}
	
	
	/**
	 * @return the MstRefCodeKey
	 */
	public MstRefCodeKey getMstRefCodeKey() {
		return MstRefCodeKey;
	}
	/**
	 * @param MstRefCodeKey the MstRefCodeKey to set
	 */
	public void setMstRefCodeKey(MstRefCodeKey MstRefCodeKey) {
		this.MstRefCodeKey = MstRefCodeKey;
	}
	/**
	 * @return the ref_name
	 */
	public String getRef_name() {
		return ref_name;
	}
	/**
	 * @param ref_name the ref_name to set
	 */
	public void setRef_name(String ref_name) {
		this.ref_name = ref_name;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	/**
	 * @return the embAudit
	 */
	public EmbAudit getEmbAudit() {
		return EmbAudit;
	}
	/**
	 * @param embAudit the embAudit to set
	 */
	public void setEmbAudit(EmbAudit embAudit) {
		EmbAudit = embAudit;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */

	
}
