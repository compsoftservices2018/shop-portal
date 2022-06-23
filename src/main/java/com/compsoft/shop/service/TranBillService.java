package com.compsoft.shop.service;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.compsoft.shop.model.TranBill;
import com.compsoft.shop.model.TranBillDetail;
import com.compsoft.shop.model.TranBillKey;
import com.compsoft.shop.model.TranBillPayment;

/**
 * @author Pradeep Chawadkar
 *
 */
public interface TranBillService {
	

	public void addBill(Session foSession, TranBill foBill);
	//public void deleteBill(Session foSession, TranBill foTranBill);
	public TranBill getBill(TranBillKey foBillKey);  
	public TranBill getBillHeader(TranBillKey foBillKey);  
	public List<Map<String, Object>> getBills(TranBill foTranBill);
	public List<Map<String, Object>> getBillDetails(TranBill foTranBill);
	public List<Map<String, Object>> getBillPayment(TranBillPayment foBillPayment);
	public void deleteBillDetail(Session foSession, TranBillDetail foTranBillDetail);
	public void deleteBillPayment(Session foSession, TranBillPayment foTranBillPayment);
	
}
