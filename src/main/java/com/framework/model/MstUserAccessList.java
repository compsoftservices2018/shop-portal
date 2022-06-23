package com.framework.model;

import java.io.Serializable;
import java.util.List;

public class MstUserAccessList implements Serializable {

	private static final long serialVersionUID = -723583058586873479L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	List<MstUserAccess> MstUserAccessList;

	/**
	 * @return the mstUserAccessList
	 */
	public List<MstUserAccess> getMstUserAccessList() {
		return MstUserAccessList;
	}

	/**
	 * @param mstUserAccessList
	 *            the mstUserAccessList to set
	 */
	public void setMstUserAccessList(List<MstUserAccess> mstUserAccessList) {
		MstUserAccessList = mstUserAccessList;
	}

}
