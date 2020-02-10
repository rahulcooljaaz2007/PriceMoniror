package com.bankprice.monitor.queue.processor;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bankprice.monitor.Model.ProductPriceModel;
import com.bankprice.monitor.cache.LatestPriceCache;
import com.bankprice.monitor.listener.PriceListener;
import com.bankprice.monitor.utility.GroupCalculator;

@Component
public class ThirdPartyPriceQueueProcessor {

	@Autowired
	LatestPriceCache cache;

	@Autowired
	LinkedBlockingQueue<ProductPriceModel> thirdPartyPriceQueue;

	@Autowired
	LinkedBlockingQueue<Long> processedGroupQueue;

	@Autowired
	LinkedBlockingQueue<String> notificationQueue;

	
	LinkedBlockingQueue<ProductPriceModel> thirdPartyPriceFutureQueue=new LinkedBlockingQueue<ProductPriceModel>();
	
	public void validateProcessedGroup() {
		
		System.out.println("ThirdPartyPriceQueueProcessor started");
		
		long currentProcessedGroup=-1;
		 while(true) {
			    try {
					currentProcessedGroup=processedGroupQueue.take();
				} catch (InterruptedException e1) {
					currentProcessedGroup=-1;
					e1.printStackTrace();
				}
			   
			    while(true) {
				     ProductPriceModel productPriceModel=null;
				     try {
					          if(!thirdPartyPriceFutureQueue.isEmpty()) {
					        	   productPriceModel= thirdPartyPriceFutureQueue.peek();
					        	     if(GroupCalculator.getGroupCalculator().calculateGroupForThirdPartyNotification(productPriceModel.getTimestamp())>currentProcessedGroup) {
					        	    	 notifyandClearPreviousGroups(currentProcessedGroup,"Motification was expected but was not send.");
					        	    	 break;
					        	     }else if (GroupCalculator.getGroupCalculator().calculateGroupForThirdPartyNotification(productPriceModel.getTimestamp())<=currentProcessedGroup) {
					        	    	 productPriceModel= thirdPartyPriceFutureQueue.poll();
					        	     }
					          }else {
					                  productPriceModel= thirdPartyPriceQueue.poll(30, TimeUnit.SECONDS);
					          }    
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			
			 
			 if(productPriceModel!=null) {
				 long groupOfThirdParyModel=GroupCalculator.getGroupCalculator().calculateGroupForThirdPartyNotification(productPriceModel.getTimestamp());
				 if(groupOfThirdParyModel == currentProcessedGroup ) {
						
						//send  for validateion and notification
						String message=cache.validatePrice(productPriceModel);
						if(message!=null) {
							try {
								notificationQueue.put("Product: "+productPriceModel.getSymbol()+
										" TimeStamp: " +productPriceModel.getTimestamp()+
										" Price: "+productPriceModel.getPrice()+ 
										" Alert: "+message);
							} catch (InterruptedException e) {
								
								e.printStackTrace();
							}

						}

					}else if(groupOfThirdParyModel<currentProcessedGroup){ 
						
							try {
								notificationQueue.put("Product: "+productPriceModel.getSymbol()+
										" TimeStamp: " +productPriceModel.getTimestamp()+
										" Price: "+productPriceModel.getPrice()+ 
										" Alert: "+"Extra unwanted notification send.");
							} catch (InterruptedException e) {
								
								e.printStackTrace();
							}

						
						
					}else {
						    
						try {
							   thirdPartyPriceFutureQueue.put(productPriceModel);
						} catch (InterruptedException e) {
						
							e.printStackTrace();
						}
 						    notifyandClearPreviousGroups(currentProcessedGroup,"Motification was expected but was not send.");
						
					   break;
					}
			 }else {
				 if(currentProcessedGroup!=-1) {
					 notifyandClearPreviousGroups(currentProcessedGroup,"Motification was expected but was not send.");
					 currentProcessedGroup=-1;
				 }
				  break;
			 }
			 
			 }
			
		 }
		}
	
	private void notifyandClearPreviousGroups(long group,String message){
		List<ProductPriceModel> symbols=cache.getMissedNotificationProductList(group);
		symbols.stream().forEach(( symbol)->{
			                                  try {
			                                       	String alert="Product: "+symbol.getSymbol()+
						                                         " TimeStamp: " +symbol.getTimestamp()+
						                                         " Price: "+symbol.getPrice()+ 
						                                         " Alert: " + message ;
				                                    notificationQueue.put(alert);
			                                       } catch (InterruptedException e) {
				                                            //Save alert to db
				                                             e.printStackTrace();
			                                 }

		                                   });		
	                              }
	}
	
	


