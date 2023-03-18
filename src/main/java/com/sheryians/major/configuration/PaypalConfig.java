package com.sheryians.major.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

@Configuration
public class PaypalConfig {
	
	@Value("${paypal.client.id}")
	private String clientId;
	
	@Value("${paypal.client.secret}")
	private String clientSecret;
	
	@Value("${paypal.mode}")
	private String mode;
	
	@Bean
	public Map<String, String> paypalSdkConfig(){
		
		Map<String, String> configMap = new HashMap<>();
		configMap.put("mode", mode);
		
		return configMap;
	}
	
	@Bean
	public APIContext apiContext() throws PayPalRESTException {
		
		APIContext context = new APIContext(clientId, clientSecret, mode);
		context.setConfigurationMap(paypalSdkConfig());
		return context;
	}	
	
}