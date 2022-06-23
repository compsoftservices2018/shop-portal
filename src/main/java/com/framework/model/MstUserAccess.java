package com.framework.model;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import com.framework.model.MstUserAccessKey;

/**
 * @author Pradeep Chawadkar
 *
 */
@Entity
@Table(name = "mst_user_access")
public class MstUserAccess implements Serializable
{

	private static final long serialVersionUID = -723583058586873479L;

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
	@Valid
	@EmbeddedId
	MstUserAccessKey moMstUserAccessKey;

	@Valid
	@Embedded
	EmbAudit EmbAudit;
	
	@NotNull(message = "Access is Required")
	String show;
	
	@NotNull(message = "Add is Required")
	String addition;
	
	
	@NotNull(message = "Update is required.")
	String updation;
	
	@NotNull(message = "Delete is required")
	String deletion;
	
	@NotNull(message = "Approve is required")
	String approval;

	@Transient
	String module_name;

	
	public MstUserAccess()
	{
		
		moMstUserAccessKey = new MstUserAccessKey();
		setMstUserAccessKey(moMstUserAccessKey);
		EmbAudit = new EmbAudit();
		setEmbAudit(EmbAudit);
	}

	/**
	 * @return the mstUserAccessKey
	 */
	public MstUserAccessKey getMstUserAccessKey() {
		return moMstUserAccessKey;
	}

	/**
	 * @param mstUserAccessKey the mstUserAccessKey to set
	 */
	public void setMstUserAccessKey(MstUserAccessKey mstUserAccessKey) {
		moMstUserAccessKey = mstUserAccessKey;
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

	/**
	 * @return the show
	 */
	public String getShow() {
		return show;
	}

	/**
	 * @param show the show to set
	 */
	public void setShow(String show) {
		this.show = show;
	}

	/**
	 * @return the addition
	 */
	public String getAddition() {
		return addition;
	}

	/**
	 * @param addition the addition to set
	 */
	public void setAddition(String addition) {
		this.addition = addition;
	}

	/**
	 * @return the updation
	 */
	public String getUpdation() {
		return updation;
	}

	/**
	 * @param updation the updation to set
	 */
	public void setUpdation(String updation) {
		this.updation = updation;
	}

	/**
	 * @return the deletion
	 */
	public String getDeletion() {
		return deletion;
	}

	/**
	 * @param deletion the deletion to set
	 */
	public void setDeletion(String deletion) {
		this.deletion = deletion;
	}

	/**
	 * @return the approval
	 */
	public String getApproval() {
		return approval;
	}

	/**
	 * @param approval the approval to set
	 */
	public void setApproval(String approval) {
		this.approval = approval;
	}

	/**
	 * @return the module_name
	 */
	public String getModule_name() {
		return module_name;
	}

	/**
	 * @param module_name the module_name to set
	 */
	public void setModule_name(String module_name) {
		this.module_name = module_name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MstUserAccess [MstUserAccessKey=" + moMstUserAccessKey + ", EmbAudit=" + EmbAudit + ", show=" + show
				+ ", addition=" + addition + ", updation=" + updation + ", deletion=" + deletion + ", approval="
				+ approval + ", module_name=" + module_name + "]";
	}

	/**
	 * @return the moMstUserAccessKey
	 */
	public MstUserAccessKey getMoMstUserAccessKey() {
		return moMstUserAccessKey;
	}

	/**
	 * @param moMstUserAccessKey the moMstUserAccessKey to set
	 */
	public void setMoMstUserAccessKey(MstUserAccessKey moMstUserAccessKey) {
		this.moMstUserAccessKey = moMstUserAccessKey;
	}

	
	
	
}
