package com.compsoft.shop.dao;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.compsoft.config.AppConfiguration;
import com.compsoft.shop.model.TranOrder;

@Repository("TranPaymentDao")
public class ExternalAPIDaoImpl implements ExternalAPIDao {

	@Autowired
	AppConfiguration config;

	public JSONObject createPayment(TranOrder foOrder) {

		RestTemplate restTemplate = new RestTemplate();

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("amount", foOrder.getTot_payment_amt().multiply(new BigDecimal("100")).longValue());
		requestBody.put("currency", "INR");
		requestBody.put("receipt", "Receipt no. " + foOrder.getTranOrderKey().getOrder_no());
	
		Map<String, Object> requestBodyNotes = new HashMap<>();
		requestBodyNotes.put("notes_key_1", foOrder.getCustomer_name());
		requestBody.put("notes", requestBodyNotes);

		HttpHeaders headers = new HttpHeaders();
		String auth = config.getPaymentGatewayApiId() + ":" + config.getPaymentGatewayApiKey();
		// String auth = "rzp_test_f8H88RD4IRBS5I" + ":" + "rrIJW7KZUq0rnjY33fumIrs3";
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.set("Authorization", authHeader);

		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

		ResponseEntity<JSONObject> response = restTemplate.postForEntity("https://api.razorpay.com/v1/orders", entity,
				JSONObject.class);

		return response.getBody();

	}

}
