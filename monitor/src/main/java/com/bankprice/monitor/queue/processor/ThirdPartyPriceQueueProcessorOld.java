//package com.bankprice.monitor.queue.processor;
//
//import java.util.List;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.TimeUnit;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.bankprice.monitor.Model.ProductPriceModel;
//import com.bankprice.monitor.cache.LatestPriceCache;
//import com.bankprice.monitor.listener.PriceListener;
//import com.bankprice.monitor.utility.GroupCalculator;
//
//@Component
//public class ThirdPartyPriceQueueProcessorOld {
//
//	@Autowired
//	LatestPriceCache cache;
//
//	@Autowired
//	LinkedBlockingQueue<ProductPriceModel> thirdPartyPriceQueue;
//
//	@Autowired
//	LinkedBlockingQueue<Long> processedGroupQueue;
//
//	@Autowired
//	LinkedBlockingQueue<String> notificationQueue;
//
//	public void validateProcessedGroup() {
//		
//		long currentProcessedGroup=-1;
//		 while(true) {
//			    currentProcessedGroup=processedGroupQueue.poll();
//			   
//			    while(true) {
//				     ProductPriceModel productPriceModel=null;
//				     try {
//					 
//					         productPriceModel= thirdPartyPriceQueue.peek();
//					         if(productPriceModel!=null) {
//						           if(GroupCalculator.getGroupCalculator().calculateGroup(productPriceModel.getTimestamp())>currentProcessedGroup) {
//							             notifyandClearPreviousGroups(currentProcessedGroup,"Motification was expected but was not send.");
//							             break; // break to get next group
//						                }
//					            }
//					    productPriceModel= thirdPartyPriceQueue.poll(30, TimeUnit.SECONDS);
//				} catch (InterruptedException e) {
//					
//					e.printStackTrace();
//				}
//			
//			 
//			 if(productPriceModel!=null) {
//				 long groupOfThirdParyModel=GroupCalculator.getGroupCalculator().calculateGroup(productPriceModel.getTimestamp());
//				 if(groupOfThirdParyModel == currentProcessedGroup ) {
//						
//						//send  for validateion and notification
//						String message=cache.validatePrice(productPriceModel);
//						if(message!=null) {
//							try {
//								notificationQueue.put("Product: "+productPriceModel.getSymbol()+
//										" TimeStamp: " +productPriceModel.getTimestamp()+
//										" Price: "+productPriceModel.getPrice()+ 
//										" Alert: "+message);
//							} catch (InterruptedException e) {
//								
//								e.printStackTrace();
//							}
//
//						}
//
//					}else if(groupOfThirdParyModel<currentProcessedGroup){ 
//						
//							try {
//								notificationQueue.put("Product: "+productPriceModel.getSymbol()+
//										" TimeStamp: " +productPriceModel.getTimestamp()+
//										" Price: "+productPriceModel.getPrice()+ 
//										" Alert: "+"Extra unwanted notification send.");
//							} catch (InterruptedException e) {
//								
//								e.printStackTrace();
//							}
//
//						
//						
//					}else {
//						
//						
//						    notifyandClearPreviousGroups(currentProcessedGroup,"Motification was expected but was not send.");
//						
//					   break;
//					}
//			 }else {
//				 if(currentProcessedGroup!=-1) {
//					 notifyandClearPreviousGroups(currentProcessedGroup,"Motification was expected but was not send.");
//					 currentProcessedGroup=-1;
//				 }
//				  break;
//			 }
//			 
//			 }
//			
//		 }
//		}
//	
//	private void notifyandClearPreviousGroups(long group,String message){
//		List<ProductPriceModel> symbols=cache.getMissedNotificationProductList(group);
//		symbols.stream().forEach(( symbol)->{
//			                                  try {
//			                                       	String alert="Product: "+symbol.getSymbol()+
//						                                         " TimeStamp: " +symbol.getTimestamp()+
//						                                         " Price: "+symbol.getPrice()+ 
//						                                         " Alert: " + message ;
//				                                    notificationQueue.put(alert);
//			                                       } catch (InterruptedException e) {
//				                                            //Save alert to db
//				                                             e.printStackTrace();
//			                                 }
//
//		                                   });		
//	                              }
//	}
//	
//	
//
//
