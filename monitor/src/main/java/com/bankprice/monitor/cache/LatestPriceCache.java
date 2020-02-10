package com.bankprice.monitor.cache;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.bankprice.monitor.Model.ProductPriceModel;
import com.bankprice.monitor.utility.GroupCalculator;

@Component
public class LatestPriceCache {


	private Map<Long,Map<String,ProductPriceModel>> latestPriceCache=new ConcurrentHashMap<>();

	public boolean isGroupExist(long group) {
		return latestPriceCache.get(group)!=null &&  !latestPriceCache.get(group).isEmpty();
	}
	
	public Long saveLatestPriceForProduct(ProductPriceModel model) {

		long group  = GroupCalculator.getGroupCalculator().calculateGroup(model.getTimestamp());
		Map<String,ProductPriceModel>batchProductMap=latestPriceCache.get(group);
		if(batchProductMap!=null) {
			batchProductMap.put(model.getSymbol(), model);
		}else {
		   Map<String,ProductPriceModel> productPriceMap= new ConcurrentHashMap<>();
		   productPriceMap.put(model.getSymbol(),model);
		   latestPriceCache.put(group,productPriceMap);
		}
		return group;
	}

	public String validatePrice(ProductPriceModel model) {
		String message=null;
		
		Map<String,ProductPriceModel>batchProductMap=latestPriceCache.get(GroupCalculator.getGroupCalculator().calculateGroupForThirdPartyNotification(model.getTimestamp()));
		if(batchProductMap!=null) {
			ProductPriceModel pricemodal=batchProductMap.get(model.getSymbol());
			if(pricemodal!=null) {
				 if(pricemodal.getPrice()!=model.getPrice()) {
					 message="Price Mismatch Error";
				 }
			}else {
				message="Extra unwanted notification send.";
			}
			batchProductMap.remove(model.getSymbol());
		}else {
			message="UnexpectedBatch";
		}
		return message;

	}
	
	public List<ProductPriceModel> getMissedNotificationProductList(long groupId) {
	    List<ProductPriceModel> symbols=new ArrayList<>();
		
		Map<String,ProductPriceModel>batchProductMap=latestPriceCache.get(groupId);
		if(batchProductMap!=null) {
			Collection<ProductPriceModel> symbolsVal=batchProductMap.values();
			    if(symbolsVal!=null) {
			    	symbols.addAll(symbolsVal);
			    }
			    latestPriceCache.remove(groupId);
		}
		
		return symbols;
	}
	
}
