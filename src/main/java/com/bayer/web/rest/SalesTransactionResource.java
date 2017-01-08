package com.bayer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bayer.service.SalesTransactionService;
import com.bayer.web.rest.util.HeaderUtil;
import com.bayer.web.rest.util.PaginationUtil;
import com.bayer.service.dto.SalesTransactionDTO;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing SalesTransaction.
 */
@RestController
@RequestMapping("/api")
public class SalesTransactionResource {

    private final Logger log = LoggerFactory.getLogger(SalesTransactionResource.class);
        
    @Inject
    private SalesTransactionService salesTransactionService;

    /**
     * POST  /sales-transactions : Create a new salesTransaction.
     *
     * @param salesTransactionDTO the salesTransactionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new salesTransactionDTO, or with status 400 (Bad Request) if the salesTransaction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sales-transactions")
    @Timed
    public ResponseEntity<SalesTransactionDTO> createSalesTransaction(@RequestBody SalesTransactionDTO salesTransactionDTO) throws URISyntaxException {
        log.debug("REST request to save SalesTransaction : {}", salesTransactionDTO);
        if (salesTransactionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("salesTransaction", "idexists", "A new salesTransaction cannot already have an ID")).body(null);
        }
        SalesTransactionDTO result = salesTransactionService.save(salesTransactionDTO);
        return ResponseEntity.created(new URI("/api/sales-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("salesTransaction", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sales-transactions : Updates an existing salesTransaction.
     *
     * @param salesTransactionDTO the salesTransactionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated salesTransactionDTO,
     * or with status 400 (Bad Request) if the salesTransactionDTO is not valid,
     * or with status 500 (Internal Server Error) if the salesTransactionDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sales-transactions")
    @Timed
    public ResponseEntity<SalesTransactionDTO> updateSalesTransaction(@RequestBody SalesTransactionDTO salesTransactionDTO) throws URISyntaxException {
        log.debug("REST request to update SalesTransaction : {}", salesTransactionDTO);
        if (salesTransactionDTO.getId() == null) {
            return createSalesTransaction(salesTransactionDTO);
        }
        SalesTransactionDTO result = salesTransactionService.save(salesTransactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("salesTransaction", salesTransactionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sales-transactions : get all the salesTransactions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of salesTransactions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/sales-transactions")
    @Timed
    public ResponseEntity<List<SalesTransactionDTO>> getAllSalesTransactions(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SalesTransactions");
        Page<SalesTransactionDTO> page = salesTransactionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sales-transactions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sales-transactions/:id : get the "id" salesTransaction.
     *
     * @param id the id of the salesTransactionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the salesTransactionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sales-transactions/{id}")
    @Timed
    public ResponseEntity<SalesTransactionDTO> getSalesTransaction(@PathVariable Long id) {
        log.debug("REST request to get SalesTransaction : {}", id);
        SalesTransactionDTO salesTransactionDTO = salesTransactionService.findOne(id);
        return Optional.ofNullable(salesTransactionDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}
