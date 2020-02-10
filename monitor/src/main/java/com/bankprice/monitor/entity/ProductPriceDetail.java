package com.bankprice.monitor.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="PRODUCT_PRICE_EVENT_SOURCE")
public class ProductPriceDetail {

@Id
@GeneratedValue(strategy=GenerationType.AUTO)
private long id;

@Column(name = "PRODUCT_CODE")
private String productCode;

@Column(name = "PRODUCT_PRICE")
private double productPrice;

@Column(name = "CONSUMER_ID")
private int consumerId;

@Column(name = "TIME_STAMP")
private long timeStamp;


@Column(name = "INCEPTION_TIME_STAMP")
private long inceptionTimeStamp;

public long getId() {
	return id;
}

public void setId(long id) {
	this.id = id;
}

public String getProductCode() {
	return productCode;
}

public void setProductCode(String productCode) {
	this.productCode = productCode;
}

public double getProductPrice() {
	return productPrice;
}

public void setProductPrice(double productPrice) {
	this.productPrice = productPrice;
}

public int getConsumerId() {
	return consumerId;
}

public void setConsumerId(int consumerId) {
	this.consumerId = consumerId;
}

public long getTimeStamp() {
	return timeStamp;
}

public void setTimeStamp(long timeStamp) {
	this.timeStamp = timeStamp;
}

public long getInceptionTimeStamp() {
	return inceptionTimeStamp;
}

public void setInceptionTimeStamp(long inceptionTimeStamp) {
	this.inceptionTimeStamp = inceptionTimeStamp;
}



}
