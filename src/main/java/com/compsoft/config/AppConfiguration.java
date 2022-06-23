package com.compsoft.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Value("${payment.api.id}")
    private String paymentGatewayApiId;

    @Value("${payment.api.key}")
    private String paymentGatewayApiKey;

    public String getPaymentGatewayApiId() {
        return paymentGatewayApiId;
    }
    
    public String getPaymentGatewayApiKey() {
        return paymentGatewayApiKey;
    }

}
