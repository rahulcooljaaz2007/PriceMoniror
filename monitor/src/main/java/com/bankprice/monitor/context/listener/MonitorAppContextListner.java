package com.bankprice.monitor.context.listener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.bankprice.monitor.queue.processor.BankPriceQueueProcessor;
import com.bankprice.monitor.queue.processor.ThirdPartyPriceQueueProcessor;
import com.bankprice.monitor.services.AlertServiceImpl;
import com.bankprice.monitor.utility.GroupCalculator;

@Component
public class MonitorAppContextListner implements
ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	AlertServiceImpl alertServiceImpl;


	@Autowired
	ThirdPartyPriceQueueProcessor thirdPartyPriceQueueProcessor;

	@Autowired
	BankPriceQueueProcessor bankPriceQueueProcessor;

	Runnable alertServiceProcess;

	Runnable thirdPartyPriceQueueProcessorProcess;

	Runnable bankPriceQueueProcessorProcess;

	@Override public void onApplicationEvent(ContextRefreshedEvent event) {

		GroupCalculator.getGroupCalculator();

		ExecutorService executorService = Executors.newFixedThreadPool(5);
		alertServiceProcess=new Runnable() {

			@Override
			public void run() {
				alertServiceImpl.processNotifications();

			}
		};

		executorService.submit(alertServiceProcess);
		
		thirdPartyPriceQueueProcessorProcess=new Runnable() {

			@Override
			public void run() {
				thirdPartyPriceQueueProcessor.validateProcessedGroup();

			}
		};

		executorService.submit(thirdPartyPriceQueueProcessorProcess);
		
		bankPriceQueueProcessorProcess=new Runnable() {

			@Override
			public void run() {
				bankPriceQueueProcessor.processQueue();

			}
		};

		executorService.submit(bankPriceQueueProcessorProcess);
	}
}