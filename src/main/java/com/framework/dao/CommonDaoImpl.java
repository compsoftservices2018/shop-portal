package com.framework.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;
/*import org.json.JSONException;
import org.json.JSONObject;*/
import org.springframework.stereotype.Repository;

import com.framework.model.ErrorLog;
import com.framework.reference.ReferenceUtils;
/*import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;*/

@Repository("CommonDao")
public class CommonDaoImpl implements CommonDao {
	
	
	
	public boolean sendSMS(HttpSession foHttpSession, String mobile, String msg, String lsFeature) {
		try {
			if (lsFeature.equals("N")) {
				return true;
			}
			// Construct data
			String apiKey = "apikey=" + "NjI0ZjM2MzQzNjdhMzMzMzZhNTI0YjMwNGM2ODc1NWE=";
			String message = "&message=" + "Test";
			String sender = "&sender=" + "600010";
			String numbers = "&numbers=" + "917506939180";
			
			// Send data
			HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
			String data = apiKey + sender + numbers + message;
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
			conn.getOutputStream().write(data.getBytes("UTF-8"));
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();
			
			return true;//stringBuffer.toString();
		} catch (Exception e) {
			System.out.println("Error SMS "+e);
			return false;//"Error "+e;
		}
	}
	

	public boolean sendSMS1(HttpSession foHttpSession, String mobile, String msg, String lsFeature) {
		try {
			if (lsFeature.equals("N")) {
				return true;
			}
			String lsSMSProvider = ReferenceUtils.getCnfigParamValue(foHttpSession, "SMS_PROVIDER");
			String lsSMSToken = ReferenceUtils.getCnfigParamValue(foHttpSession, "SMS_TOKEN");
			String lsSMSSender = ReferenceUtils.getCnfigParamValue(foHttpSession, "SMS_SENDER");
			String lsSMSRoute = ReferenceUtils.getCnfigParamValue(foHttpSession, "SMS_ROUTE");

			String lsSMSUrl = lsSMSProvider + "?api_token=" + lsSMSToken + "&senderid=" + lsSMSSender + "&number="
					+ mobile + "&message=" + URLEncoder.encode(msg, "UTF-8") + "&route=" + lsSMSRoute;

			URL url = new URL(lsSMSUrl);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Accept", "application/json");

			if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void addErrorLog(Session foSession, ErrorLog foErrorLog) {
		/*
		 * Session loSession = sessionFactory.getCurrentSession();
		 * loSession.saveOrUpdate(foErrorLog);
		 */
	}
	
	
	/*
	 * public JSONObject createPayment(JSONObject data) { Order order = null; try {
	 * RazorpayClient razorpay = new RazorpayClient((String) data.get("rzr_id"),
	 * (String) data.get("rzr_key")); JSONObject orderRequest = new JSONObject();
	 * orderRequest.put("amount", data.get("amount")); orderRequest.put("currency",
	 * "INR"); orderRequest.put("receipt", data.get("order_id")); order =
	 * razorpay.Orders.create(orderRequest); return order.toJson(); } catch
	 * (JSONException e) { return null; } catch (RazorpayException e) { return null;
	 * }
	 * 
	 * }
	 */
	


}
