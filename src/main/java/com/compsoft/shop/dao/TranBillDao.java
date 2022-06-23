package com.compsoft.shop.dao;


import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.compsoft.shop.model.TranBill;
import com.compsoft.shop.model.TranBillDetail;
import com.compsoft.shop.model.TranBillKey;
import com.compsoft.shop.model.TranBillPayment;
import com.framework.dao.SuperDao;;

/**
 * @author Pradeep Chawadkar
 *
 */
public interface TranBillDao extends SuperDao {
	
	public void addBill(Session foSession, TranBill foBill);
	//public void deleteBill(Session foSession, TranBill foTranBill);
	public TranBill getBill(TranBillKey foBillKey); 
	public TranBill getBillHeader(TranBillKey foBillKey); 
	public List<Map<String, Object>> getBills(TranBill foTranBill);
	public List<Map<String, Object>> getBillDetails(TranBill foTranBill); 
	public List<Map<String, Object>> getBillPayment(TranBillPayment foBillPayment);
	public void deleteBillDetail(Session foSession, TranBillDetail foTranBillDetail);
	public void deleteBillPayment(Session foSession, TranBillPayment foTranBillPayment);
	/*public BillCart getBillCart( BillCartKey foBillCartKey,  String llProductId);
	
	public  List<Map<String, Object>> getBillsForCustomer(MstCustomer foCustomer);

	public  List<Map<String, Object>> getBillsByStatus( String fsStatus);
	
	public List<Map<String, Object>> getBillsByCustIdStatus(MstCustomer foCustomer, String fsStatus);
	
	public List<Map<String, Object>> getProductListForCart(MstCustomer foCustomer, MstProduct foProduct, String fsSort);
		*/

	}
