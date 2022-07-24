package com.framework.model;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

import com.framework.model.MstUserKey;
import com.framework.utils.ConstantsRegEx;
import com.framework.utils.FrameworkConstants;

/**
 * @author Pradeep Chawadkar
 *
 */
@Entity
@Table(name = "mst_user")
public class MstGSTReconProgress implements Serializable
{

	private static final long serialVersionUID = -723583058586873479L;

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	@Valid
	@EmbeddedId
	MstGSTReconProgressKey MstGSTReconProgressKey;

	public MstGSTReconProgress()
	{
		
		MstGSTReconProgressKey = new MstGSTReconProgressKey();
		setMstGSTReconProgressKey(MstGSTReconProgressKey);
	}


	@NotNull(message = "Status is Required")
	String status;

	/**
	 * @return the mstGSTReconProgressKey
	 */
	public MstGSTReconProgressKey getMstGSTReconProgressKey() {
		return MstGSTReconProgressKey;
	}


	/**
	 * @param mstGSTReconProgressKey the mstGSTReconProgressKey to set
	 */
	public void setMstGSTReconProgressKey(MstGSTReconProgressKey mstGSTReconProgressKey) {
		MstGSTReconProgressKey = mstGSTReconProgressKey;
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
	
	
	
}
