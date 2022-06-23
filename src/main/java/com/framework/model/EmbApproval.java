package com.framework.model;

import java.sql.Timestamp;
import javax.persistence.Embeddable;

@Embeddable
public class EmbApproval {
	String auth_by;
	Timestamp auth_date;

	public EmbApproval() {
	}

	public String getAuth_by() {
		return auth_by;
	}

	public void setAuth_by(String auth_by) {
		this.auth_by = auth_by;
	}

	public Timestamp getAuth_date() {
		return auth_date;
	}

	public void setAuth_date(Timestamp auth_date) {
		this.auth_date = auth_date;
	}

}
