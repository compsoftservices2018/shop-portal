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
public class MstAppl implements Serializable
{

	private static final long serialVersionUID = -723583058586873479L;

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	@Valid
	@EmbeddedId
	MstApplKey MstApplKey;

	public MstAppl()
	{
		
		MstApplKey = new MstApplKey();
		setMstApplKey(MstApplKey);
	}


	@NotNull(message = "Home page is Required")
	String homepage;
	
	@NotNull(message = "name is Required")
	String name;

	/**
	 * @return the mstApplKey
	 */
	public MstApplKey getMstApplKey() {
		return MstApplKey;
	}

	/**
	 * @param mstApplKey the mstApplKey to set
	 */
	public void setMstApplKey(MstApplKey mstApplKey) {
		MstApplKey = mstApplKey;
	}

	/**
	 * @return the homepage
	 */
	public String getHomepage() {
		return homepage;
	}

	/**
	 * @param homepage the homepage to set
	 */
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	
}
