package com.bankprice.monitor.Model;

public class ProductPriceModel {
 
	private long timestamp;
	
	private String symbol;
	
	private double price;

	public long getTimestamp() {
		return timestamp;
	}

	
	public ProductPriceModel(long timestamp, String symbol, double price) {
		super();
		this.timestamp = timestamp;
		this.symbol = symbol;
		this.price = price;
	}


	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	
}
