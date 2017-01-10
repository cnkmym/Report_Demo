package com.bayer.web.rest;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bayer.service.EmployeeSalesSummaryService;
import com.bayer.service.dto.EmployeeSalesSummaryDTO;
import com.bayer.service.dto.SalesTransactionDTO;
import com.bayer.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing EmployeeSalesSummary.
 */
@RestController
@RequestMapping("/api/sales-summaries")
public class EmployeeSalesSummaryResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeSalesSummaryResource.class);
        
    @Inject
    private EmployeeSalesSummaryService employeeSalesSummaryService;

    /**
     * GET  /employee-sales-summaries : get all the employeeSalesSummaries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of employeeSalesSummaries in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/employee")
    @Timed
    public ResponseEntity<List<EmployeeSalesSummaryDTO>> getAllEmployeeSalesSummaries(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of EmployeeSalesSummaries");
        Page<EmployeeSalesSummaryDTO> page = employeeSalesSummaryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sales-summaries/employee");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /employee-sales-summaries/:id : get the "id" employeeSalesSummary.
     *
     * @param id the id of the employeeSalesSummaryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the employeeSalesSummaryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sales-summaries/employee/{id}")
    @Timed
    public ResponseEntity<EmployeeSalesSummaryDTO> getEmployeeSalesSummary(@PathVariable Long id) {
        log.debug("REST request to get EmployeeSalesSummary : {}", id);
        EmployeeSalesSummaryDTO employeeSalesSummaryDTO = employeeSalesSummaryService.findOne(id);
        return Optional.ofNullable(employeeSalesSummaryDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    /**
     * GET  /general-sales-summaries : get the generalSalesSummaries belong to certain period.
     *
     * @param fromYear, fromMonth, toYear, toMonth
     * @return the ResponseEntity with status 200 (OK) and the list of generalSalesSummaries in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/employee/search")
    @Timed
    public ResponseEntity<Map<String,List<EmployeeSalesSummaryDTO>>> getEmployeeSalesSummaries(@RequestParam Integer fromYear, @RequestParam Integer fromMonth, @RequestParam Integer toYear, @RequestParam Integer toMonth)
        throws URISyntaxException {
        log.debug("REST request to a series of EmployeeSalesSummaries belonging to a period");
        if (fromYear > toYear || (fromYear == toYear && fromMonth > toMonth)){
        	new ResponseEntity<>("Invalid Time Period as Request Parameter",HttpStatus.BAD_REQUEST);
        }
        Map<String,List<EmployeeSalesSummaryDTO>> employeeSalesSummaryDTOs = employeeSalesSummaryService.findByTimePeriod(fromYear, fromMonth, toYear, toMonth);
        return new ResponseEntity<>(employeeSalesSummaryDTOs, HttpStatus.OK);
    }
    
    @GetMapping("/employee/detail/{id}")
    @Timed
    public ResponseEntity<List<SalesTransactionDTO>> getRelatedSalesTransactions(@PathVariable Long id)
        throws URISyntaxException {
        log.debug("REST request to detail SalesTransactionDTO List belonging to a EmployeeSalesSummaryDTO");
        if (id == null){
        	new ResponseEntity<>("Invalid Request Parameter",HttpStatus.BAD_REQUEST);
        }
        List<SalesTransactionDTO> relatedSalesTransactionDTOs = employeeSalesSummaryService.findDetailTransactionsBySummaryId(id);
        return new ResponseEntity<>(relatedSalesTransactionDTOs, HttpStatus.OK);
    }

}
