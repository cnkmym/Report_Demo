package com.bayer.repository;

import com.bayer.domain.GeneralSalesSummary;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the GeneralSalesSummary entity.
 */
@SuppressWarnings("unused")
public interface GeneralSalesSummaryRepository extends JpaRepository<GeneralSalesSummary,Long> {

}
