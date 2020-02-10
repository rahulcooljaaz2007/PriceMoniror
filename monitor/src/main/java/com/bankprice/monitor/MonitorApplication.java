package com.bankprice.monitor;

import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bankprice.monitor.Model.ProductPriceModel;

@Configuration
@SpringBootApplication
public class MonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitorApplication.class, args);
	}

@Bean
    public LinkedBlockingQueue<ProductPriceModel> bankPriceQueue() {
        return new LinkedBlockingQueue<ProductPriceModel>();
    }	

@Bean
public LinkedBlockingQueue<ProductPriceModel> thirdPartyPriceQueue() {
    return new LinkedBlockingQueue<ProductPriceModel>();
}


@Bean
public LinkedBlockingQueue<Long> processedGroupQueue() {
    return new LinkedBlockingQueue<Long>();
}

@Bean
public LinkedBlockingQueue<String> notificationQueue() {
    return new LinkedBlockingQueue<String>();
}

}
