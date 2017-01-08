package com.bayer.service;

import com.bayer.domain.SalesTransaction;
import com.bayer.repository.SalesTransactionRepository;
import com.bayer.service.dto.SalesTransactionDTO;
import com.bayer.service.mapper.SalesTransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
    private SalesTransactionMapper salesTransactionMapper;

    /**
     * Save a salesTransaction.
     *
     * @param salesTransactionDTO the entity to save
     * @return the persisted entity
     */
    public SalesTransactionDTO save(SalesTransactionDTO salesTransactionDTO) {
        log.debug("Request to save SalesTransaction : {}", salesTransactionDTO);
        SalesTransaction salesTransaction = salesTransactionMapper.salesTransactionDTOToSalesTransaction(salesTransactionDTO);
        salesTransaction = salesTransactionRepository.save(salesTransaction);
        SalesTransactionDTO result = salesTransactionMapper.salesTransactionToSalesTransactionDTO(salesTransaction);
        return result;
    }

    /**
     *  Get all the salesTransactions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<SalesTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SalesTransactions");
        Page<SalesTransaction> result = salesTransactionRepository.findAll(pageable);
        return result.map(salesTransaction -> salesTransactionMapper.salesTransactionToSalesTransactionDTO(salesTransaction));
    }

    /**
     *  Get one salesTransaction by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public SalesTransactionDTO findOne(Long id) {
        log.debug("Request to get SalesTransaction : {}", id);
        SalesTransaction salesTransaction = salesTransactionRepository.findOne(id);
        SalesTransactionDTO salesTransactionDTO = salesTransactionMapper.salesTransactionToSalesTransactionDTO(salesTransaction);
        return salesTransactionDTO;
    }

    /**
     *  Delete the  salesTransaction by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SalesTransaction : {}", id);
        salesTransactionRepository.delete(id);
    }
}
