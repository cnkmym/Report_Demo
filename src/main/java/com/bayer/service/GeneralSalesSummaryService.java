package com.bayer.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bayer.domain.GeneralSalesSummary;
import com.bayer.domain.SalesTransaction;
import com.bayer.repository.GeneralSalesSummaryRepository;
import com.bayer.repository.SalesTransactionRepository;
import com.bayer.service.dto.GeneralSalesSummaryDTO;
import com.bayer.service.dto.SalesTransactionDTO;
import com.bayer.service.mapper.GeneralSalesSummaryMapper;
import com.bayer.service.mapper.SalesTransactionMapper;

/**
 * Service Implementation for managing SalesTransaction.
 */
@Service
@Transactional
public class GeneralSalesSummaryService {

	private final Logger log = LoggerFactory.getLogger(GeneralSalesSummaryService.class);

	@Inject
	private SalesTransactionRepository salesTransactionRepository;
	@Inject
	private GeneralSalesSummaryRepository generalSummaryRepository;
	@Inject
	private GeneralSalesSummaryMapper generalSummaryMapper;
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
	public Page<GeneralSalesSummaryDTO> findAll(Pageable pageable) {
		log.debug("Request to get all GeneralSalesSummary");
		Page<GeneralSalesSummary> result = generalSummaryRepository.findAll(pageable);
		return result.map(generalSalesSummary -> generalSummaryMapper
				.generalSalesSummaryToGeneralSalesSummaryDTO(generalSalesSummary));
	}

	/**
	 * Get one GeneralSalesSummary by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public GeneralSalesSummaryDTO findOne(Long id) {
		log.debug("Request to get GeneralSalesSummary : {}", id);
		GeneralSalesSummary generalSalesSummary = generalSummaryRepository.findOne(id);
		GeneralSalesSummaryDTO generalSalesSummaryDTO = generalSummaryMapper
				.generalSalesSummaryToGeneralSalesSummaryDTO(generalSalesSummary);
		return generalSalesSummaryDTO;
	}

	private GeneralSalesSummaryDTO composeDummyRecord(Integer year, Integer month) {
		GeneralSalesSummaryDTO dto = new GeneralSalesSummaryDTO();
		dto.setYear(year);
		dto.setMonth(month);
		dto.setTotalAmount(new BigDecimal("0"));
		return dto;
	}

	@Transactional(readOnly = true)
	public List<GeneralSalesSummaryDTO> findByTimePeriod(Integer fromYear, Integer fromMonth, Integer toYear, Integer toMonth){
		List<GeneralSalesSummaryDTO> ret = new ArrayList<>();
		int startY = fromYear;
        int startM = fromMonth;
        while (true){
        	if (startM>12){
        		startY++;
        		startM = 1;
        		continue;
        	}
        	if (startY > toYear || (startY == toYear && startM > toMonth)) {
        		break;
        	}
        	GeneralSalesSummary entity = generalSummaryRepository.findOneByYearAndMonth(startY, startM);
        	if (entity == null){
        		ret.add(composeDummyRecord(startY,startM));
        	}else{
        		ret.add(generalSummaryMapper.generalSalesSummaryToGeneralSalesSummaryDTO(entity));
        	}
        	startM++;
        }
        return ret;
	}

	@Transactional(readOnly = true)
	public List<SalesTransactionDTO> findDetailTransactionsBySummaryId(Long id) {
		List<SalesTransactionDTO> ret = new ArrayList<>();
		GeneralSalesSummary entity = generalSummaryRepository.findOne(id);
		if (entity != null){
			List<SalesTransaction> list = new ArrayList<>();
			list.addAll(entity.getTransactions());
    		ret.addAll(salesTransactionMapper.salesTransactionsToSalesTransactionDTOs(list));
    	}
		return ret;
	}
}
