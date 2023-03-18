package com.sheryians.major.model;

import com.sheryians.major.global.GlobalData;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {
	
	private double price = GlobalData.cart.stream().mapToDouble(Product::getPrice).sum();
	private String currency = "USD";
	private String method = "paypal";
	private String intent = "sale";
	private String description = "order placed";
	
	
}