package com.bayer.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bayer.domain.Employee;
import com.bayer.domain.EmployeeSalesSummary;
import com.bayer.domain.SalesTransaction;
import com.bayer.repository.EmployeeRepository;
import com.bayer.repository.EmployeeSalesSummaryRepository;
import com.bayer.repository.SalesTransactionRepository;
import com.bayer.service.dto.EmployeeDTO;
import com.bayer.service.dto.EmployeeSalesSummaryDTO;
import com.bayer.service.dto.SalesTransactionDTO;
import com.bayer.service.mapper.EmployeeMapper;
import com.bayer.service.mapper.EmployeeSalesSummaryMapper;
import com.bayer.service.mapper.SalesTransactionMapper;

/**
 * Service Implementation for managing SalesTransaction.
 */
@Service
@Transactional
public class EmployeeSalesSummaryService {

	private final Logger log = LoggerFactory.getLogger(EmployeeSalesSummaryService.class);

	@Inject
	private SalesTransactionRepository salesTransactionRepository;
	@Inject
	private EmployeeSalesSummaryRepository employeeSummaryRepository;
	@Inject
	private EmployeeRepository employeeRepository;
	@Inject
	private EmployeeSalesSummaryMapper employeeSummaryMapper;
	@Inject
	private EmployeeMapper employeeMapper;
	@Inject
	private SalesTransactionMapper salesTransactionMapper;

	/**
	 * Get all the GeneralSalesSummary.
	 * 
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<EmployeeSalesSummaryDTO> findAll(Pageable pageable) {
		log.debug("Request to get all EmployeeSalesSummary");
		Page<EmployeeSalesSummary> result = employeeSummaryRepository.findAll(pageable);
		return result
				.map(salesSummary -> employeeSummaryMapper.employeeSalesSummaryToEmployeeSalesSummaryDTO(salesSummary));
	}

	/**
	 * Get one GeneralSalesSummary by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public EmployeeSalesSummaryDTO findOne(Long id) {
		log.debug("Request to get EmployeeSalesSummary : {}", id);
		EmployeeSalesSummary salesSummary = employeeSummaryRepository.findOne(id);
		EmployeeSalesSummaryDTO salesSummaryDTO = employeeSummaryMapper
				.employeeSalesSummaryToEmployeeSalesSummaryDTO(salesSummary);
		return salesSummaryDTO;
	}

	private EmployeeSalesSummaryDTO composeDummyRecord(Integer year, Integer month, Employee employee) {
		EmployeeSalesSummaryDTO dto = new EmployeeSalesSummaryDTO();
		dto.setYear(year);
		dto.setMonth(month);
		dto.setTotalAmount(new BigDecimal("0"));
		dto.setEmployeeId(employee.getId());
		return dto;
	}

	@Transactional(readOnly = true)
	public Map<String, List<EmployeeSalesSummaryDTO>> findByTimePeriod(Integer fromYear, Integer fromMonth,
			Integer toYear, Integer toMonth) {
		Map<String, List<EmployeeSalesSummaryDTO>> ret = new HashMap<>();

		employeeRepository.findAll().forEach(employee -> {
			ret.put("{\"id\":"+employee.getId()+",\"employeeName\":\""+employee.getEmployeeName()+"\"}",
					findByTimePeriod(fromYear, fromMonth, toYear, toMonth, employee));
		});
		
		return ret;
	}

	private List<EmployeeSalesSummaryDTO> findByTimePeriod(Integer fromYear, Integer fromMonth, Integer toYear,
			Integer toMonth, Employee employee) {
		List<EmployeeSalesSummaryDTO> ret = new ArrayList<>();
		int startY = fromYear;
		int startM = fromMonth;
		while (true) {
			if (startM > 12) {
				startY++;
				startM = 1;
				continue;
			}
			if (startY > toYear || (startY == toYear && startM > toMonth)) {
				break;
			}
			EmployeeSalesSummary entity = employeeSummaryRepository.findOneByYearAndMonthAndEmployee(startY, startM, employee);
			if (entity == null) {
				ret.add(composeDummyRecord(startY, startM, employee));
			} else {
				ret.add(employeeSummaryMapper.employeeSalesSummaryToEmployeeSalesSummaryDTO(entity));
			}
			startM++;
		}
		return ret;
	}

	@Transactional(readOnly = true)
	public List<SalesTransactionDTO> findDetailTransactionsBySummaryId(Long id) {
		List<SalesTransactionDTO> ret = new ArrayList<>();
		EmployeeSalesSummary entity = employeeSummaryRepository.findOne(id);
		if (entity != null) {
			List<SalesTransaction> list = new ArrayList<>();
			list.addAll(entity.getTransactions());
			ret.addAll(salesTransactionMapper.salesTransactionsToSalesTransactionDTOs(list));
		}
		return ret;
	}
}
