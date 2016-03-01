package com.example.rapidscholar;

public class GlobalPrice {
	public double price;
	
	public double getPrice() {
		return price;
	}
	
	public void addPrice(double add) {
		this.price += add;
	}
	
	public void clearPrice() {
		this.price = 0;
	}
	
	public void subPrice(double sub) {
		this.price -= sub;
	}
}
