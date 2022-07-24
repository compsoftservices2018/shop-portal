package com.compsoft.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;





@SpringBootApplication(scanBasePackages = { "com.framework.*", "com.compsoft.*", "com.razorpay.*","com.mgp.*" })
@EntityScan("com.compsoft.shop.model")
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ServletInitializer.class);
	}
	public static void main(String[] args) {
		SpringApplication.run(ServletInitializer.class, args);
	}
}
