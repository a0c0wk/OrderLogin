package com.store.login.service;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.store.login.model.OrderDetail;
import com.store.login.model.OrderDetailsList;
import com.store.login.model.OrderInput;
import com.store.login.util.Jwtutil;

@Service
public class OrderLoginService {
	
	@Value("${security.oauth2.client.userAuthorizationUri}")
	String authUri;
	
	@Value("${security.oauth2.client.clientId}")
	String clientid;
	
	String uri = "http://localhost:8090/";

	@Autowired
	Jwtutil jwtutil;
	
	String token = null;
	
	public ResponseEntity<OrderDetail> processRequest(String username, HttpHeaders headers) {
		// genrate jwt token
		token = jwtutil.genrateToken(username);
		System.out.println(" token :: " + token + "::   authUri -"+ authUri);
		/*
		 * //call auth service to get code ResponseEntity<String> codeResp =
		 * callAuthservice(authUri,headers); String code = codeResp; System.out.println(
		 * " code ----> "+code);
		 */
		// call order service
		ResponseEntity<OrderDetail> detail = callOrderService(token, headers);
		return detail;

	}
	
	private ResponseEntity<String> callAuthservice(String authUri2, HttpHeaders headers) {
		System.out.println(" calling authservice ");
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
		
		 authUri2 = authUri2+"?response_type=code&"+
				 "client_id="+clientid+"&"+
				 "redirect_uri="+uri+"&"+
				 "scope=read";
				 
		
		return restTemplate.exchange(authUri2, HttpMethod.GET, requestEntity, String.class);	
	}

	private ResponseEntity<OrderDetail> callOrderService(String token,HttpHeaders headers) {
		System.out.println(" calling orderservice  ");
		RestTemplate restTemplate = new RestTemplate();
	
		OrderInput input = new OrderInput(token);
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(input,headers);
		return restTemplate.exchange("http://localhost:8085/neworder", HttpMethod.POST, requestEntity, OrderDetail.class);	
		
	}

	public ResponseEntity<OrderDetailsList> getAllOrders(Principal principal,HttpHeaders headers) {
		
		if(null == token) {
			token = jwtutil.genrateToken(principal.getName());
		}		
		
		RestTemplate restTemplate = new RestTemplate();
		headers.add("jwt", token);		
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
		return restTemplate.exchange("http://localhost:8085/allorder", HttpMethod.GET, requestEntity, OrderDetailsList.class);	
	}
}
