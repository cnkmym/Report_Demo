package com.bayer.repository;

import com.bayer.domain.EmployeeSalesSummary;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EmployeeSalesSummary entity.
 */
@SuppressWarnings("unused")
public interface EmployeeSalesSummaryRepository extends JpaRepository<EmployeeSalesSummary,Long> {

}
