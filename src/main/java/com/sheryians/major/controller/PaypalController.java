package com.sheryians.major.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.sheryians.major.model.Order;
import com.sheryians.major.service.PaypalService;

@Controller
public class PaypalController {

	@Autowired
	PaypalService paypalService;
	
	public static final String success_url = "pay/success";
	public static final String cancel_url = "pay/cancel";
	
	@PostMapping("/pay")
	public String payment(@ModelAttribute("order") Order order) {
		
		try {
			Payment payment = paypalService.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),
					order.getIntent(), order.getDescription(), "http://localhost:8080/"+cancel_url,
					"http://localhost:8080/"+success_url);
			for(Links link:payment.getLinks()) {
				if(link.getRel().equals("approval_url")) {
					return "redirect:"+link.getHref();
				}
			}
		} catch(PayPalRESTException e) {
			
			e.printStackTrace();
		}
		
		return "redirect:/checkout";
	}
	
	@GetMapping(value = cancel_url)
	public String cancelPay() {
		return "cancel";
	}
	
	@GetMapping(value = success_url)
	public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId){
		try {
			Payment payment = paypalService.executePayment(paymentId, payerId);
			System.out.println(payment.toJSON());
			if(payment.getState().equals("approved")) {
				return "success";
			}
		} catch(PayPalRESTException e) {
			System.out.println(e.getMessage());
		}
		return "redirect:/checkout";
	}
	
}
