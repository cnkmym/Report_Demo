package com.bayer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bayer.domain.EmployeeSalesSummary;

import com.bayer.repository.EmployeeSalesSummaryRepository;
import com.bayer.web.rest.util.HeaderUtil;
import com.bayer.web.rest.util.PaginationUtil;
import com.bayer.service.dto.EmployeeSalesSummaryDTO;
import com.bayer.service.mapper.EmployeeSalesSummaryMapper;

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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing EmployeeSalesSummary.
 */
@RestController
@RequestMapping("/api")
public class EmployeeSalesSummaryResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeSalesSummaryResource.class);
        
    @Inject
    private EmployeeSalesSummaryRepository employeeSalesSummaryRepository;

    @Inject
    private EmployeeSalesSummaryMapper employeeSalesSummaryMapper;

    /**
     * GET  /employee-sales-summaries : get all the employeeSalesSummaries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of employeeSalesSummaries in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/employee-sales-summaries")
    @Timed
    public ResponseEntity<List<EmployeeSalesSummaryDTO>> getAllEmployeeSalesSummaries(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of EmployeeSalesSummaries");
        Page<EmployeeSalesSummary> page = employeeSalesSummaryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/employee-sales-summaries");
        return new ResponseEntity<>(employeeSalesSummaryMapper.employeeSalesSummariesToEmployeeSalesSummaryDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /employee-sales-summaries/:id : get the "id" employeeSalesSummary.
     *
     * @param id the id of the employeeSalesSummaryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the employeeSalesSummaryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/employee-sales-summaries/{id}")
    @Timed
    public ResponseEntity<EmployeeSalesSummaryDTO> getEmployeeSalesSummary(@PathVariable Long id) {
        log.debug("REST request to get EmployeeSalesSummary : {}", id);
        EmployeeSalesSummary employeeSalesSummary = employeeSalesSummaryRepository.findOne(id);
        EmployeeSalesSummaryDTO employeeSalesSummaryDTO = employeeSalesSummaryMapper.employeeSalesSummaryToEmployeeSalesSummaryDTO(employeeSalesSummary);
        return Optional.ofNullable(employeeSalesSummaryDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
