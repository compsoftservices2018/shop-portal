package com.compsoft.shop.service;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.compsoft.shop.dao.TranBillDao;
import com.compsoft.shop.model.TranBill;
import com.compsoft.shop.model.TranBillDetail;
import com.compsoft.shop.model.TranBillKey;
import com.compsoft.shop.model.TranBillPayment;

/**
 * @author Pradeep Chawadkar
 *
 */
@Service("TranBillService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TranBillServiceImpl implements TranBillService {

	@Autowired
	private TranBillDao TranBillDao; 
	
	
	public void addBill(Session foSession, TranBill foBill)
	{
		TranBillDao.addBill(foSession, foBill);
	}
/*	public void deleteBill(Session foSession, TranBill foTranBill)
	{
		TranBillDao.deleteBill(foSession, foTranBill);
	}*/
	public TranBill getBill(TranBillKey foBillKey)
	{
		return TranBillDao.getBill(foBillKey);
	}
	public TranBill getBillHeader(TranBillKey foBillKey)
	{
		return TranBillDao.getBillHeader(foBillKey);
	}
	
	public List<Map<String, Object>> getBills(TranBill foTranBill)
	{
		return TranBillDao.getBills(foTranBill);
	}

	public List<Map<String, Object>> getBillDetails(TranBill foTranBill)
	{
		return TranBillDao.getBillDetails(foTranBill);
	}
	public List<Map<String, Object>> getBillPayment(TranBillPayment foBillPayment) {
		return TranBillDao.getBillPayment(foBillPayment);
	}
	public void deleteBillDetail(Session foSession, TranBillDetail foTranBillDetail) {
		TranBillDao.deleteBillDetail(foSession, foTranBillDetail); 
	}
	public void deleteBillPayment(Session foSession, TranBillPayment foTranBillPayment) {
		TranBillDao.deleteBillPayment(foSession, foTranBillPayment); 
	}
}
