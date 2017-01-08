package com.bayer.service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bayer.domain.Employee;
import com.bayer.domain.EmployeeSalesSummary;
import com.bayer.domain.GeneralSalesSummary;
import com.bayer.domain.Product;
import com.bayer.domain.ProductSalesSummary;
import com.bayer.domain.SalesTransaction;
import com.bayer.repository.EmployeeSalesSummaryRepository;
import com.bayer.repository.GeneralSalesSummaryRepository;
import com.bayer.repository.ProductSalesSummaryRepository;
import com.bayer.repository.SalesTransactionRepository;
import com.bayer.service.dto.SalesTransactionDTO;
import com.bayer.service.mapper.SalesTransactionMapper;

/**
 * Service Implementation for managing SalesTransaction.
 */
@Service
@Transactional
public class SalesTransactionService {

	private final Logger log = LoggerFactory.getLogger(SalesTransactionService.class);

	@Inject
	private SalesTransactionRepository salesTransactionRepository;
	@Inject
	private GeneralSalesSummaryRepository generalSummaryRepository;
	@Inject
	private EmployeeSalesSummaryRepository employeeSummaryRepository;
	@Inject
	private ProductSalesSummaryRepository productSummaryRepository;

	@Inject
	private SalesTransactionMapper salesTransactionMapper;

	/**
	 * Save a salesTransaction.
	 *
	 * @param salesTransactionDTO
	 *            the entity to save
	 * @return the persisted entity
	 */
	@Transactional
	public SalesTransactionDTO save(SalesTransactionDTO salesTransactionDTO) {
		log.debug("Request to save SalesTransaction : {}", salesTransactionDTO);
		if (salesTransactionDTO.getTransactionDate() == null) {
			salesTransactionDTO.setTransactionDate(ZonedDateTime.now());
		}
		SalesTransaction salesTransaction = salesTransactionMapper
				.salesTransactionDTOToSalesTransaction(salesTransactionDTO);

		if (salesTransactionDTO.getId() != null) {
			// update existing transaction
			subtractExistingSummary(salesTransactionDTO.getId());
		}
		// insert new transaction
		return insertNewTransaction(salesTransaction);
	}

	private void subtractExistingSummary(Long salesTransactionId) {
		SalesTransaction existingValue = salesTransactionRepository.findOne(salesTransactionId);
		if (existingValue != null) {
			Integer year = existingValue.getTransactionDate().getYear();
			Integer month = existingValue.getTransactionDate().getMonthValue();
			subtractGeneralSummary(existingValue, year, month);
			subtractEmployeeSummary(existingValue, year, month);
			substractProductSummary(existingValue, year, month);
		}
	}

	private SalesTransactionDTO insertNewTransaction(SalesTransaction salesTransaction) {
		SalesTransaction result = updateSalesTransaction(salesTransaction);
		Integer year = salesTransaction.getTransactionDate().getYear();
		Integer month = salesTransaction.getTransactionDate().getMonthValue();
		updateGeneralSummary(result, year, month);
		updateEmployeeSummary(result, year, month);
		updateProductSummary(result, year, month);
		SalesTransactionDTO ret = salesTransactionMapper.salesTransactionToSalesTransactionDTO(salesTransaction);
		return ret;
	}

	private GeneralSalesSummary createEmptyGeneralSummary(Integer year, Integer month) {
		GeneralSalesSummary result = new GeneralSalesSummary();
		result.setYear(year);
		result.setMonth(month);
		result.setTotalAmount(new BigDecimal("0.00"));
		result.setTransactions(new HashSet<SalesTransaction>());
		return result;
	}

	private EmployeeSalesSummary createEmptyEmployeeSummary(Integer year, Integer month, Employee employee) {
		EmployeeSalesSummary result = new EmployeeSalesSummary();
		result.setYear(year);
		result.setMonth(month);
		result.setTotalAmount(new BigDecimal("0.00"));
		result.setEmployee(employee);
		result.setTransactions(new HashSet<SalesTransaction>());
		return result;
	}

	private ProductSalesSummary createEmptyProductSummary(Integer year, Integer month, Product product) {
		ProductSalesSummary result = new ProductSalesSummary();
		result.setYear(year);
		result.setMonth(month);
		result.setTotalAmount(new BigDecimal("0.00"));
		result.setProduct(product);
		result.setTransactions(new HashSet<SalesTransaction>());
		return result;
	}

	private void updateGeneralSummary(SalesTransaction salesTransaction, Integer year, Integer month) {
		GeneralSalesSummary summary = generalSummaryRepository.findOneByYearAndMonth(year, month);
		if (summary == null) {
			summary = generalSummaryRepository.save(createEmptyGeneralSummary(year, month));
		}

		summary.setTotalAmount(summary.getTotalAmount().add(salesTransaction.getTransactionAmount()));
		summary.getTransactions().add(salesTransaction);
		generalSummaryRepository.save(summary);
	}

	private void subtractGeneralSummary(SalesTransaction salesTransaction, Integer year, Integer month) {
		GeneralSalesSummary summary = generalSummaryRepository.findOneByYearAndMonth(year, month);
		if (summary == null) {
			return;
		}

		summary.setTotalAmount(summary.getTotalAmount().subtract(salesTransaction.getTransactionAmount()));
		summary.getTransactions().remove(salesTransaction);
		generalSummaryRepository.save(summary);
	}

	private void updateEmployeeSummary(SalesTransaction salesTransaction, Integer year, Integer month) {
		if (salesTransaction.getEmpolyee() == null){
			return;
		}
		EmployeeSalesSummary summary = employeeSummaryRepository.findOneByYearAndMonthAndEmployee(year, month,
				salesTransaction.getEmpolyee());
		if (summary == null) {
			summary = employeeSummaryRepository
					.save(createEmptyEmployeeSummary(year, month, salesTransaction.getEmpolyee()));
		}

		summary.setTotalAmount(summary.getTotalAmount().add(salesTransaction.getTransactionAmount()));
		summary.getTransactions().add(salesTransaction);
		employeeSummaryRepository.save(summary);
	}

	private void subtractEmployeeSummary(SalesTransaction salesTransaction, Integer year, Integer month) {
		EmployeeSalesSummary summary = employeeSummaryRepository.findOneByYearAndMonthAndEmployee(year, month,
				salesTransaction.getEmpolyee());
		if (summary == null) {
			return;
		}

		summary.setTotalAmount(summary.getTotalAmount().subtract(salesTransaction.getTransactionAmount()));
		summary.getTransactions().remove(salesTransaction);
		employeeSummaryRepository.save(summary);
	}

	private void updateProductSummary(SalesTransaction salesTransaction, Integer year, Integer month) {
		if (salesTransaction.getProduct() == null){
			return;
		}
		ProductSalesSummary summary = productSummaryRepository.findOneByYearAndMonthAndProduct(year, month,
				salesTransaction.getProduct());
		if (summary == null) {
			summary = productSummaryRepository
					.save(createEmptyProductSummary(year, month, salesTransaction.getProduct()));
		}

		summary.setTotalAmount(summary.getTotalAmount().add(salesTransaction.getTransactionAmount()));
		summary.getTransactions().add(salesTransaction);
		productSummaryRepository.save(summary);
	}

	private void substractProductSummary(SalesTransaction salesTransaction, Integer year, Integer month) {
		ProductSalesSummary summary = productSummaryRepository.findOneByYearAndMonthAndProduct(year, month,
				salesTransaction.getProduct());
		if (summary == null) {
			return;
		}

		summary.setTotalAmount(summary.getTotalAmount().subtract(salesTransaction.getTransactionAmount()));
		summary.getTransactions().remove(salesTransaction);
		productSummaryRepository.save(summary);
	}

	private SalesTransaction updateSalesTransaction(SalesTransaction salesTransaction) {
		SalesTransaction ret = salesTransactionRepository.save(salesTransaction);
		return ret;
	}

	/**
	 * Get all the salesTransactions.
	 * 
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<SalesTransactionDTO> findAll(Pageable pageable) {
		log.debug("Request to get all SalesTransactions");
		Page<SalesTransaction> result = salesTransactionRepository.findAll(pageable);
		return result.map(
				salesTransaction -> salesTransactionMapper.salesTransactionToSalesTransactionDTO(salesTransaction));
	}

	/**
	 * Get one salesTransaction by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public SalesTransactionDTO findOne(Long id) {
		log.debug("Request to get SalesTransaction : {}", id);
		SalesTransaction salesTransaction = salesTransactionRepository.findOne(id);
		SalesTransactionDTO salesTransactionDTO = salesTransactionMapper
				.salesTransactionToSalesTransactionDTO(salesTransaction);
		return salesTransactionDTO;
	}
}
