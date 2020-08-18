package com.store.login.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.login.model.OrderDetail;
import com.store.login.model.OrderDetailsList;
import com.store.login.service.OrderLoginService;


@RestController
public class LoginController {
	
	@Autowired
	OrderLoginService svc;  
	
	

	@GetMapping("/")
	public ResponseEntity<OrderDetail> orderLogin(Principal principal)
	{
		HttpHeaders headers = new HttpHeaders();
		String username = principal.getName();
		System.out.println(" Login successful for user :  "+ username);
		return svc.processRequest(username,headers);		
	}

	
	@GetMapping("/all")
	public ResponseEntity<OrderDetailsList>  getAllOrder(Principal principal)
	{
		HttpHeaders headers = new HttpHeaders();
		return (ResponseEntity<OrderDetailsList>) svc.getAllOrders(principal,headers);
	}

	
	
	
	
	
	
}
