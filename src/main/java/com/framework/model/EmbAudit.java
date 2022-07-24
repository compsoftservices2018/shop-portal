package com.framework.model;


import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import com.framework.utils.AppUtils;
import com.framework.utils.FrameworkConstants;


/**
 * @author Pradeep Chawadkar
 *
 */
@Embeddable
public class EmbAudit
{

	@Column(updatable = false)
	String created_by;
	@Column(updatable = false)
	Timestamp created_date;
	String updated_by;
	Timestamp updated_date;
	@Transient
	String object_mode;
	@Transient
	String str_created_date;
	@Transient
	String str_updated_date;
	
	
	public EmbAudit()
	{
		setObject_mode(FrameworkConstants.OBJECT_MODE_NEW);
	}
	
	/**
	 * @return the created_by
	 */
	public String getCreated_by() {
		return created_by;
	}
	/**
	 * @param created_by the created_by to set
	 */
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	/**
	 * @return the created_date
	 */
	public Timestamp getCreated_date() {
		return created_date;
	}
	/**
	 * @param created_date the created_date to set
	 */
	public void setCreated_date(Timestamp created_date) {
		this.created_date = created_date;
		setStr_created_date(AppUtils.getFormattedDateTime(created_date));
	}
	/**
	 * @return the updated_by
	 */
	public String getUpdated_by() {
		return updated_by;
	}
	/**
	 * @param updated_by the updated_by to set
	 */
	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}
	/**
	 * @return the updated_date
	 */
	public Timestamp getUpdated_date() {
		return updated_date;
	}
	/**
	 * @param updated_date the updated_date to set
	 */
	public void setUpdated_date(Timestamp updated_date) {
		this.updated_date = updated_date;
		setStr_updated_date(AppUtils.getFormattedDateTime(updated_date));
	}
	/**
	 * @return the object_mode
	 */
	public String getObject_mode() {
		return object_mode;
	}
	/**
	 * @param object_mode the object_mode to set
	 */
	public void setObject_mode(String object_mode) {
		this.object_mode = object_mode;
	}
	
	
	/**
	 * @return the str_created_date
	 */
	public String getStr_created_date() {
		return str_created_date;
	}

	/**
	 * @param str_created_date the str_created_date to set
	 */
	public void setStr_created_date(String str_created_date) {
		this.str_created_date = str_created_date;
	}

	/**
	 * @return the str_updated_date
	 */
	public String getStr_updated_date() {
		return str_updated_date;
	}

	/**
	 * @param str_updated_date the str_updated_date to set
	 */
	public void setStr_updated_date(String str_updated_date) {
		this.str_updated_date = str_updated_date;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EmbAudit [created_by=" + created_by + ", created_date=" + created_date + ", updated_by=" + updated_by
				+ ", updated_date=" + updated_date + ", object_mode=" + object_mode + "]";
	}
	
	
}
