package com.bayer.repository;

import com.bayer.domain.ProductSalesSummary;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProductSalesSummary entity.
 */
@SuppressWarnings("unused")
public interface ProductSalesSummaryRepository extends JpaRepository<ProductSalesSummary,Long> {

}
