package com.bayer.repository;

import com.bayer.domain.SalesTransaction;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SalesTransaction entity.
 */
@SuppressWarnings("unused")
public interface SalesTransactionRepository extends JpaRepository<SalesTransaction,Long> {

}
