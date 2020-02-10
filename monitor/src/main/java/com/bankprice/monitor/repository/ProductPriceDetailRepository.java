package com.bankprice.monitor.repository;

import org.springframework.stereotype.Repository;

import com.bankprice.monitor.entity.ProductPriceDetail;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface ProductPriceDetailRepository extends  CrudRepository<ProductPriceDetail,Long>{

	
	  public ProductPriceDetail save(ProductPriceDetail entity) ;		
		
}
