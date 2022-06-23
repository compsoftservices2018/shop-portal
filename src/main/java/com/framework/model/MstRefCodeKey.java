package com.framework.model;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Embeddable
public class MstRefCodeKey implements Serializable { 
	
	private static final long serialVersionUID = -723583058586873479L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	 
	@NotNull(message = "Group  is required")
	 String ref_group;
	@NotNull(message = "Code  is required")
	 String ref_code;
	 
	 public MstRefCodeKey ()
	 {
		 
	 }
	 
	 public MstRefCodeKey(	 String	ref_group,
		 String	ref_code)
	 {
		 this.setRef_group(ref_group);
		 this.setRef_code(ref_code);
	 }

	/**
	 * @return the ref_group
	 */
	public String getRef_group()
	{
		return ref_group;
	}

	/**
	 * @param ref_group the ref_group to set
	 */
	public void setRef_group(String ref_group)
	{
		this.ref_group = ref_group;
	}

	/**
	 * @return the ref_code
	 */
	public String getRef_code()
	{
		return ref_code;
	}

	/**
	 * @param ref_code the ref_code to set
	 */
	public void setRef_code(String ref_code)
	{
		this.ref_code = ref_code;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MstRefCodeKey [ref_group=" + ref_group + ", ref_code=" + ref_code + "]";
	}

	
	 
}