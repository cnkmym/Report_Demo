package com.bayer.web.rest;

import java.net.URISyntaxException;
import java.util.List;
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

import com.bayer.service.GeneralSalesSummaryService;
import com.bayer.service.dto.GeneralSalesSummaryDTO;
import com.bayer.service.dto.SalesTransactionDTO;
import com.bayer.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing GeneralSalesSummary.
 */
@RestController
@RequestMapping("/api/sales-summaries")
public class GeneralSalesSummaryResource {

    private final Logger log = LoggerFactory.getLogger(GeneralSalesSummaryResource.class);
        
    @Inject
    private GeneralSalesSummaryService generalSalesSummaryService;

    /**
     * GET  /general-sales-summaries : get all the generalSalesSummaries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of generalSalesSummaries in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/general")
    @Timed
    public ResponseEntity<List<GeneralSalesSummaryDTO>> getAllGeneralSalesSummaries(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of GeneralSalesSummaries");
        Page<GeneralSalesSummaryDTO> page = generalSalesSummaryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sales-summaries/general");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /general-sales-summaries : get the generalSalesSummaries belong to certain period.
     *
     * @param fromYear, fromMonth, toYear, toMonth
     * @return the ResponseEntity with status 200 (OK) and the list of generalSalesSummaries in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/general/search")
    @Timed
    public ResponseEntity<List<GeneralSalesSummaryDTO>> getGeneralSalesSummaries(@RequestParam Integer fromYear, @RequestParam Integer fromMonth, @RequestParam Integer toYear, @RequestParam Integer toMonth)
        throws URISyntaxException {
        log.debug("REST request to a series of GeneralSalesSummaries belonging to a period");
        if (fromYear > toYear || (fromYear == toYear && fromMonth > toMonth)){
        	new ResponseEntity<>("Invalid Time Period as Request Parameter",HttpStatus.BAD_REQUEST);
        }
        List<GeneralSalesSummaryDTO> generalSalesSummaryDTOs = generalSalesSummaryService.findByTimePeriod(fromYear, fromMonth, toYear, toMonth);
        return new ResponseEntity<>(generalSalesSummaryDTOs, HttpStatus.OK);
    }
    
    @GetMapping("/general/detail/{id}")
    @Timed
    public ResponseEntity<List<SalesTransactionDTO>> getRelatedSalesTransactions(@PathVariable Long id)
        throws URISyntaxException {
        log.debug("REST request to detail SalesTransactionDTO List belonging to a GeneralSalesSummaryDTO");
        if (id == null){
        	new ResponseEntity<>("Invalid Request Parameter",HttpStatus.BAD_REQUEST);
        }
        List<SalesTransactionDTO> relatedSalesTransactionDTOs = generalSalesSummaryService.findDetailTransactionsBySummaryId(id);
        return new ResponseEntity<>(relatedSalesTransactionDTOs, HttpStatus.OK);
    }

    /**
     * GET  /general-sales-summaries/:id : get the "id" generalSalesSummary.
     *
     * @param id the id of the generalSalesSummaryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the generalSalesSummaryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/general/{id}")
    @Timed
    public ResponseEntity<GeneralSalesSummaryDTO> getGeneralSalesSummary(@PathVariable Long id) {
        log.debug("REST request to get GeneralSalesSummary : {}", id);
        GeneralSalesSummaryDTO generalSalesSummaryDTO = generalSalesSummaryService.findOne(id);
        return Optional.ofNullable(generalSalesSummaryDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
