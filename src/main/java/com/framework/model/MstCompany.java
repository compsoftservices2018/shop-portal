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
import com.framework.model.MstCompanyKey;
import com.framework.utils.FrameworkConstants;

@Entity
@Table(name = "mst_company")
public class MstCompany implements Serializable {

	private static final long serialVersionUID = -723583058586873479L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Valid
	@EmbeddedId
	MstCompanyKey MstCompanyKey;

	@Valid
	@Embedded
	EmbAudit EmbAudit;

	public MstCompany() {

		MstCompanyKey = new MstCompanyKey();
		setMstCompanyKey(MstCompanyKey);
		EmbAudit = new EmbAudit();
		setEmbAudit(EmbAudit);
		this.setStatus(FrameworkConstants.STATUS_ACTIVE);

	}

	@NotNull(message = "Status is required")
	String status;

	@NotNull(message = "Name is Required")
	@Size(max = 100, message = "Name cannot be more than 100 characters.")
	String company_name;

	@NotNull(message = "State is required")
	String state;

	@NotNull(message = "PIN is required")
	String pin;

	@NotNull(message = "Country  is required")
	String country;

	@NotNull(message = "Mobile is required")
	String mobile;

	@NotNull(message = "E-Mail is required")
	String email;

	@Size(max = 100, message = "Address cannot be more than 100 characters.")
	String address;

	@Size(max = 100, message = "Contact person name cannot be more than 100 characters.")
	String contact_person;
	String telephone;
	String fax;
	String website;
	String city;
	String pan;
	String gst_regn_type;
	String gstin_no;
	String periodicity_gstr;
	String short_name;
	String comp_key;

	/**
	 * @return the mstCompanyKey
	 */
	public MstCompanyKey getMstCompanyKey() {
		return MstCompanyKey;
	}

	/**
	 * @param mstCompanyKey
	 *            the mstCompanyKey to set
	 */
	public void setMstCompanyKey(MstCompanyKey mstCompanyKey) {
		MstCompanyKey = mstCompanyKey;
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
	 * @return the company_name
	 */
	public String getCompany_name() {
		return company_name;
	}

	/**
	 * @param company_name
	 *            the company_name to set
	 */
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the pin
	 */
	public String getPin() {
		return pin;
	}

	/**
	 * @param pin
	 *            the pin to set
	 */
	public void setPin(String pin) {
		this.pin = pin;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the contact_person
	 */
	public String getContact_person() {
		return contact_person;
	}

	/**
	 * @param contact_person
	 *            the contact_person to set
	 */
	public void setContact_person(String contact_person) {
		this.contact_person = contact_person;
	}

	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone
	 *            the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
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
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax
	 *            the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
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
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * @param website
	 *            the website to set
	 */
	public void setWebsite(String website) {
		this.website = website;
	}

	/**
	 * @return the pan
	 */
	public String getPan() {
		return pan;
	}

	/**
	 * @param pan
	 *            the pan to set
	 */
	public void setPan(String pan) {
		this.pan = pan;
	}

	/**
	 * @return the gst_regn_type
	 */
	public String getGst_regn_type() {
		return gst_regn_type;
	}

	/**
	 * @param gst_regn_type
	 *            the gst_regn_type to set
	 */
	public void setGst_regn_type(String gst_regn_type) {
		this.gst_regn_type = gst_regn_type;
	}

	/**
	 * @return the gstin_no
	 */
	public String getGstin_no() {
		return gstin_no;
	}

	/**
	 * @param gstin_no
	 *            the gstin_no to set
	 */
	public void setGstin_no(String gstin_no) {
		this.gstin_no = gstin_no;
	}

	/**
	 * @return the periodicity_gstr
	 */
	public String getPeriodicity_gstr() {
		return periodicity_gstr;
	}

	/**
	 * @param periodicity_gstr
	 *            the periodicity_gstr to set
	 */
	public void setPeriodicity_gstr(String periodicity_gstr) {
		this.periodicity_gstr = periodicity_gstr;
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

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	public String getShort_name() {
		return short_name;
	}

	public void setShort_name(String short_name) {
		this.short_name = short_name;
	}

	public String getComp_key() {
		return comp_key;
	}

	public void setComp_key(String comp_key) {
		this.comp_key = comp_key;
	}

}
