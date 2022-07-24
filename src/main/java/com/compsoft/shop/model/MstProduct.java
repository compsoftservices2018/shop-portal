package com.compsoft.shop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.framework.utils.FrameworkConstants;

/**
 * @author Pradeep Chawadkar
 *
 */
@Entity
@Table(name = "mst_online_product")
public class MstProduct implements Serializable {

	private static final long serialVersionUID = -723583058586873479L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Valid
	@EmbeddedId
	MstProductKey MstProductKey;

	/*
	 * @Valid
	 * 
	 * @Embedded EmbAudit EmbAudit;
	 */

	public MstProduct() {
		MstProductKey = new MstProductKey();
		setMstProductKey(MstProductKey);
		this.page_mode = FrameworkConstants.OBJECT_MODE_NEW;

	}

	public String validate() {

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set inputErrors = validator.validate(this);
		Iterator iterator = inputErrors.iterator();
		String lsErrors = "";
		while (iterator.hasNext()) {
			ConstraintViolation setElement = (ConstraintViolation) iterator.next();
			lsErrors = lsErrors + setElement.getMessage() + "<br>";
		}

		if (this.getEnd_date() != null && this.getEnd_date().compareTo(this.getStart_date()) < 0) {
			lsErrors = lsErrors + "End date should be greater than Start date" + "<br>";
		}

		if (this.getMrp() != null && this.getMrp().compareTo(this.getMstProductKey().getSelling_price()) < 0) {
			lsErrors = lsErrors + "MRP should be greater than Selling price" + "<br>";
		}

		return lsErrors;
	}

	@Size(max = 50, message = "Product name can not be more than 50 characters")
	@NotEmpty(message = "Product name is required")
	String product_name;

	@Size(max = 50, message = "Group name can not be more than 50 characters")
	@NotEmpty(message = "Group name is required")
	String group_name;

	@NotNull(message = "GST percentage is required")
	BigDecimal gst_percentage;
	
	@NotNull(message = "CESS percentage is required")
	BigDecimal cess_percentage;
	
	@Size(max = 8, message = "HSN code can not be more than 8 characters")
	String hsn_code;
	
	@NotNull(message = "MRP is required")
	BigDecimal mrp;
	@Size(max = 50, message = "Image name can not be more than 50 characters")
	String image_name;
	/* @NotNull(message = "Stock is required") */
	BigDecimal stock;

	@NotNull(message = "Start date is required")
	Timestamp start_date;
	
	Timestamp end_date;

	@Size(max = 1500, message = "Additional information can not be more than 1500 characters")
	String add_information;
	String home_page_view;

	
	@NotNull(message = "Landing cost is required")
	BigDecimal landing_cost;
	@NotNull(message = "Discount percentage is required")
	BigDecimal disc_per;
	@NotNull(message = "BV percentage is required")
	BigDecimal bv_per;
	@NotNull(message = "Discount is required")
	BigDecimal discount;
	@NotNull(message = "BV is required")
	BigDecimal bv;
	String alt_product_code;
	String alt_product_name;

	
	@Transient
	String page_mode;


	
	public String getAdd_information() {
		return add_information;
	}

	public void setAdd_information(String add_information) {
		this.add_information = add_information;
	}

	public MstProductKey getMstProductKey() {
		return MstProductKey;
	}

	public void setMstProductKey(MstProductKey mstProductKey) {
		MstProductKey = mstProductKey;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public BigDecimal getGst_percentage() {
		if (gst_percentage == null)
			gst_percentage =  BigDecimal.ZERO;
		return gst_percentage.setScale(2);
	}

	public void setGst_percentage(BigDecimal gst_percentage) {
		this.gst_percentage = gst_percentage;
	}

	public BigDecimal getCess_percentage() {
		if (cess_percentage == null)
			cess_percentage =  BigDecimal.ZERO;
		return cess_percentage.setScale(2);
	}

	public void setCess_percentage(BigDecimal cess_percentage) {
		this.cess_percentage = cess_percentage;
	}

	public String getHsn_code() {
		return hsn_code;
	}

	public void setHsn_code(String hsn_code) {
		this.hsn_code = hsn_code;
	}

	public BigDecimal getMrp() {
		if (mrp == null)
			mrp =  BigDecimal.ZERO;
		return mrp.setScale(2);
	}

	public void setMrp(BigDecimal mrp) {
		this.mrp = mrp;
	}

	public String getImage_name() {
		return image_name;
	}

	public void setImage_name(String image_name) {
		this.image_name = image_name;
	}

	public BigDecimal getStock() {
		if (stock == null)
			stock = BigDecimal.ZERO;
		return stock;
	}

	public void setStock(BigDecimal stock) {
		this.stock = stock;
	}

	public Timestamp getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Timestamp end_date) {
		this.end_date = end_date;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getHome_page_view() {
		return home_page_view;
	}

	public void setHome_page_view(String home_page_view) {
		this.home_page_view = home_page_view;
	}

	public BigDecimal getLanding_cost() {
		if (landing_cost == null)
			landing_cost =  BigDecimal.ZERO;
		return landing_cost.setScale(2);
	}

	public void setLanding_cost(BigDecimal landing_cost) {

		this.landing_cost = landing_cost;
	}

	public BigDecimal getDisc_per() {
		if (disc_per == null)
			disc_per = BigDecimal.ZERO;
		return disc_per.setScale(2);
	}

	public void setDisc_per(BigDecimal disc_per) {
		this.disc_per = disc_per;
	}

	public BigDecimal getBv_per() {
		if (bv_per == null)
			bv_per =  BigDecimal.ZERO;
		return bv_per.setScale(2);
	}

	public void setBv_per(BigDecimal bv_per) {
		this.bv_per = bv_per;
	}

	public BigDecimal getDiscount() {
		if (discount == null)
			discount =  BigDecimal.ZERO;
		return discount.setScale(2);
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getBv() {
		if (bv == null)
			bv =  BigDecimal.ZERO;
		return bv.setScale(2);
	}

	public void setBv(BigDecimal bv) {
		this.bv = bv;
	}

	public Timestamp getStart_date() {
		return start_date;
	}

	public void setStart_date(Timestamp start_date) {
		this.start_date = start_date;
	}

	public String getPage_mode() {
		return page_mode;
	}

	public void setPage_mode(String page_mode) {
		this.page_mode = page_mode;
	}

	public String getAlt_product_code() {
		return alt_product_code;
	}

	public void setAlt_product_code(String alt_product_code) {
		this.alt_product_code = alt_product_code;
	}

	public String getAlt_product_name() {
		return alt_product_name;
	}

	public void setAlt_product_name(String alt_product_name) {
		this.alt_product_name = alt_product_name;
	}

	
	

}
