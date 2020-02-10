package com.bankprice.monitor.services;

import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlertServiceImpl implements AlertService {

	@Autowired
	LinkedBlockingQueue<String> notificationQueue;
	
	public void processNotifications() {
		System.out.println("AlertServiceImpl started");
		
		while(true) {
			String alert="";
			try {
				alert = notificationQueue.take();
				alert(alert);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	@Override
	public void alert(String message) {

             System.out.println("Message:::  "+message);

	}

}
