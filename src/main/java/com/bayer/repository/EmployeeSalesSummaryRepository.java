package com.bayer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bayer.domain.Employee;
import com.bayer.domain.EmployeeSalesSummary;

/**
 * Spring Data JPA repository for the EmployeeSalesSummary entity.
 */
public interface EmployeeSalesSummaryRepository extends JpaRepository<EmployeeSalesSummary,Long> {
	EmployeeSalesSummary findOneByYearAndMonthAndEmployee(Integer year, Integer month, Employee employee);
}
