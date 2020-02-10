package com.bankprice.monitor.utility;

public class GroupCalculator {

	private GroupCalculator(long timeStamp) {
		inceptionTimeStamp=timeStamp;
		System.out.println("************Group Calculator Initialized****************");
	}

	private long inceptionTimeStamp=-1;

	private  static class  GroupCalculatorHolder{
		private static GroupCalculator groupCalculatorInstance= new GroupCalculator(System.currentTimeMillis());
		
	}

	public static GroupCalculator getGroupCalculator() {
		return GroupCalculatorHolder.groupCalculatorInstance;
	}

	public long getInceptionTimeStamp() {
		return inceptionTimeStamp;
	}


	public  long calculateGroup(long timeStamp) {
		return (timeStamp-inceptionTimeStamp)/30000;
	}
	

	public  long calculateGroupForThirdPartyNotification(long timeStamp) {
		
			return (calculateGroup(timeStamp))-1;
		
	}
	

}
