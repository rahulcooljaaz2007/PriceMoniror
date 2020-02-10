package com.bankprice.monitor.listener.impl;

import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bankprice.monitor.Model.ProductPriceModel;
import com.bankprice.monitor.listener.PriceListener;

@Component
public class BankPriceListener implements PriceListener {

	@Autowired
	LinkedBlockingQueue<ProductPriceModel> bankPriceQueue;
	
	@Override
	public void priceUpdate(String symbol, double price) {
		long timeStamp=System.currentTimeMillis();
		ProductPriceModel productPriceModel =new ProductPriceModel(timeStamp,symbol,price);
		try {
			bankPriceQueue.put(productPriceModel);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
	}

}
