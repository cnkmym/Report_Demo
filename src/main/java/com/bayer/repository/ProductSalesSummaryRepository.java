package com.bayer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bayer.domain.Product;
import com.bayer.domain.ProductSalesSummary;

/**
 * Spring Data JPA repository for the ProductSalesSummary entity.
 */
public interface ProductSalesSummaryRepository extends JpaRepository<ProductSalesSummary,Long> {
	ProductSalesSummary findOneByYearAndMonthAndProduct(Integer year, Integer month, Product product);
}
