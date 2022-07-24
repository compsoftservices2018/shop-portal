package com.compsoft.shop.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;;

/**
 * @author Pradeep Chawadkar
 *
 */
@Entity
@Table(name = "tran_order_detail")
public class TranOrderDetail implements Serializable
{

	private static final long serialVersionUID = -723583058586873479L;

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	@EmbeddedId
	TranOrderDetailKey TranOrderDetailKey;

	String product_name;
	String alt_product_code;
	String alt_product_name;
	BigDecimal order_qty;
	String hsn_code;
	BigDecimal gst_percentage;
	BigDecimal cess_percentage;
	BigDecimal mrp;
	String add_information;
	BigDecimal landing_cost;
	BigDecimal disc_per;
	BigDecimal bv_per;
	BigDecimal discount;
	BigDecimal bv;
	String image_name;
	
	public TranOrderDetail()
	{
		TranOrderDetailKey = new TranOrderDetailKey();
		setTranOrderDetailKey(TranOrderDetailKey);
	}


	public String getAdd_information() {
		return add_information;
	}


	public void setAdd_information(String add_information) {
		this.add_information = add_information;
	}




	public TranOrderDetailKey getTranOrderDetailKey() {
		return TranOrderDetailKey;
	}


	public void setTranOrderDetailKey(TranOrderDetailKey tranOrderDetailKey) {
		TranOrderDetailKey = tranOrderDetailKey;
	}



	public String getProduct_name() {
		return product_name;
	}


	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}


	public BigDecimal getOrder_qty() {
		if(order_qty == null)
			order_qty = BigDecimal.ZERO;
		return order_qty;
	}


	public void setOrder_qty(BigDecimal order_qty) {
		this.order_qty = order_qty;
	}


	public BigDecimal getMrp() {
		if(mrp == null)
			mrp = BigDecimal.ZERO;
		return mrp.setScale(2);
	}


	public void setMrp(BigDecimal mrp) {
		this.mrp = mrp;
	}



	public String getHsn_code() {
		return hsn_code;
	}


	public void setHsn_code(String hsn_code) {
		this.hsn_code = hsn_code;
	}


	public BigDecimal getGst_percentage() {
		if(gst_percentage == null)
			gst_percentage = BigDecimal.ZERO;
		return gst_percentage.setScale(2);
	}


	public void setGst_percentage(BigDecimal gst_percentage) {
		this.gst_percentage = gst_percentage;
	}


	public BigDecimal getCess_percentage() {
		if(cess_percentage == null)
			cess_percentage = BigDecimal.ZERO;
		return cess_percentage.setScale(2);
	}


	public void setCess_percentage(BigDecimal cess_percentage) {
		this.cess_percentage = cess_percentage;
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


	public BigDecimal getLanding_cost() {
		if(landing_cost == null)
			landing_cost = BigDecimal.ZERO;
		return landing_cost.setScale(2);
	}


	public void setLanding_cost(BigDecimal landing_cost) {
		this.landing_cost = landing_cost;
	}


	public BigDecimal getDisc_per() {
		if(disc_per == null)
			disc_per = BigDecimal.ZERO;
		return disc_per.setScale(2);
	}


	public void setDisc_per(BigDecimal disc_per) {
		this.disc_per = disc_per;
	}


	public BigDecimal getBv_per() {
		if(bv_per == null)
			bv_per = BigDecimal.ZERO;
		return bv_per.setScale(2);
	}


	public void setBv_per(BigDecimal bv_per) {
		this.bv_per = bv_per;
	}


	public BigDecimal getDiscount() {
		if(discount == null)
			discount = BigDecimal.ZERO;
		return discount.setScale(2);
	}


	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}


	public BigDecimal getBv() {
		if(bv == null)
			bv = BigDecimal.ZERO;
		return bv.setScale(2);
	}


	public void setBv(BigDecimal bv) {
		this.bv = bv;
	}


	public String getImage_name() {
		return image_name;
	}


	public void setImage_name(String image_name) {
		this.image_name = image_name;
	}





}
