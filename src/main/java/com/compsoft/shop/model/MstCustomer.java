package com.compsoft.shop.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.framework.security.Security;

/**
 * @author Pradeep Chawadkar
 *
 */
@Entity
@Table(name = "mst_online_customer")
public class MstCustomer implements Serializable
{

	private static final long serialVersionUID = -723583058586873479L;

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
	@Valid
	@EmbeddedId
	MstCustomerKey MstCustomerKey;

	
	public MstCustomer()
	{
		MstCustomerKey = new MstCustomerKey();
		setMstCustomerKey(MstCustomerKey);
		setStatus("N");
	}

	
	@NotEmpty(message = "Customer type is required")
	@Size(max=1, message = "Customer type can not be more than 1 characters")
	String customer_type;
	
	
	@NotEmpty(message = "Customer name is required")
	@Size(max=50, message = "Customer name can not be more than 50 characters")
	String customer_name;
	
	
	@Size(max=300, message = "Address can not be more than 300 characters")
	String address;
	
	
	@NotEmpty(message = "Pin is required")
	@Size(max=6, message = "PIN can not be more than 6 characters")
	String pin;
	
	@NotEmpty(message = "Mobile is required")	
	@Size(max=10, message = "Mobile can not be more than 10 characters")
	String mobile;
	
	@Size(max=50, message = "Email can not be more than 50 characters")
	String email;
	
	@Size(max=15, message = "GST number can not be more than 15 characters")
	String gstin_no;
	
	@Size(max=1, message = "Status can not be more than 1 characters")
	String status;
	
	//@NotEmpty(message = "Password is required")
	//@Size(max=12, message = "Password can not be more than 12 characters")
	String password;
	
	//@Size(max=12, message = "Temporary password can not be more than 12 characters")
	String temp_password;
	
	@Size(max=4000, message = "Delivery pins can not be more than 4000 characters")
	String delivery_pins;

	@Size(max=10, message = "Alternate customer code can not be more than 10 characters")
	String alt_customer_code;
	
	@Size(max=10, message = "Parent customer code can not be more than 10 characters")
	String parent_customer_code;
	String tpt_password;
	
	@Transient
	String otp;
	
	@Transient
	String full_address;
	
	public MstCustomerKey getMstCustomerKey() {
		return MstCustomerKey;
	}
	public void setMstCustomerKey(MstCustomerKey mstCustomerKey) {
		MstCustomerKey = mstCustomerKey;
	}
	public String getCustomer_type() {
		return customer_type;
	}
	public void setCustomer_type(String customer_type) {
		this.customer_type = customer_type;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGstin_no() {
		return gstin_no;
	}
	public void setGstin_no(String gstin_no) {
		this.gstin_no = gstin_no;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPassword() {
		return Security.decrypt(password);
	}
	public void setPassword(String password) {
		this.password = Security.encrypt(password);
	}
	
	
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public String getFull_address() {
		return address + " Pin:" + pin;
	}
	public void setFull_address(String full_address) {
		this.full_address = full_address;
	}
	public String getTemp_password() {
		return Security.decrypt(temp_password);
	}
	public void setTemp_password(String temp_password) {
		this.temp_password = Security.encrypt(temp_password);
	}
		
	public String getDelivery_pins() {
		return delivery_pins;
	}
	public void setDelivery_pins(String delivery_pins) {
		this.delivery_pins = delivery_pins;
	}

	public String getAlt_customer_code() {
		return alt_customer_code;
	}
	public void setAlt_customer_code(String alt_customer_code) {
		this.alt_customer_code = alt_customer_code;
	}
	public String getParent_customer_code() {
		return parent_customer_code;
	}
	public void setParent_customer_code(String parent_customer_code) {
		this.parent_customer_code = parent_customer_code;
	}
	
	
	
	public String getTpt_password() {
		return tpt_password;
	}
	public void setTpt_password(String tpt_password) {
		this.tpt_password = tpt_password;
	}
	public String validate(){
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set inputErrors = validator.validate(this);
		Iterator iterator = inputErrors.iterator();
		String lsErrors = "";
		while (iterator.hasNext()) {
			ConstraintViolation setElement = (ConstraintViolation) iterator.next();
			lsErrors = lsErrors + setElement.getMessage() + "<br>";
		}
			
		if (this.getPassword().length() > 12)
		{
			lsErrors = lsErrors + "Password can not be more than 12 characters" + "<br>";
		}
		
				
		return lsErrors;
	}
}
