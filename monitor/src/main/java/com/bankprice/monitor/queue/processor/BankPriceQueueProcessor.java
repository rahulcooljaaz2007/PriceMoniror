package com.bankprice.monitor.queue.processor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bankprice.monitor.Model.ProductPriceModel;
import com.bankprice.monitor.cache.LatestPriceCache;
import com.bankprice.monitor.entity.ProductPriceDetail;
import com.bankprice.monitor.repository.ProductPriceDetailRepository;
import com.bankprice.monitor.utility.GroupCalculator;

@Component
public class BankPriceQueueProcessor {

	@Autowired
	LatestPriceCache cache;

	@Autowired
	LinkedBlockingQueue<ProductPriceModel> bankPriceQueue;

	@Autowired
	LinkedBlockingQueue<Long> processedGroupQueue;

	@Autowired
	ProductPriceDetailRepository productPriceDetailRepository;

	public void processQueue() {
		System.out.println("BankPriceQueueProcessor started");
		
		long previousGroup=-1;
		long currentGroup=-1;
		while(true) {
			ProductPriceModel model=null;
			try {
				model = bankPriceQueue.poll(30, TimeUnit.SECONDS);
			} catch (InterruptedException e1) {

				e1.printStackTrace();
			}

			if(model!=null){
				currentGroup=cache.saveLatestPriceForProduct(model);
				
				if(previousGroup!=currentGroup) {
					try {
						if(previousGroup!=-1) {
						     processedGroupQueue.put(previousGroup);
						     previousGroup=-1;
						}
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
				}
				previousGroup=currentGroup;
				
				ProductPriceDetail  priceDetail=new ProductPriceDetail();
				priceDetail.setProductCode(model.getSymbol());
				priceDetail.setProductPrice(model.getPrice());
				priceDetail.setTimeStamp(model.getTimestamp());
				priceDetail.setInceptionTimeStamp(GroupCalculator.getGroupCalculator().getInceptionTimeStamp());
				priceDetail.setConsumerId(1);			
				productPriceDetailRepository.save(priceDetail);
			}else {
				try {
					if(previousGroup!=-1) {
					     processedGroupQueue.put(previousGroup);
					     previousGroup=-1;
					}
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}
	}


}
