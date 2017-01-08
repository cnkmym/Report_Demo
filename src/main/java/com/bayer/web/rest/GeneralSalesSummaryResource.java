package com.bayer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bayer.domain.GeneralSalesSummary;

import com.bayer.repository.GeneralSalesSummaryRepository;
import com.bayer.web.rest.util.HeaderUtil;
import com.bayer.web.rest.util.PaginationUtil;
import com.bayer.service.dto.GeneralSalesSummaryDTO;
import com.bayer.service.mapper.GeneralSalesSummaryMapper;

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
 * REST controller for managing GeneralSalesSummary.
 */
@RestController
@RequestMapping("/api")
public class GeneralSalesSummaryResource {

    private final Logger log = LoggerFactory.getLogger(GeneralSalesSummaryResource.class);
        
    @Inject
    private GeneralSalesSummaryRepository generalSalesSummaryRepository;

    @Inject
    private GeneralSalesSummaryMapper generalSalesSummaryMapper;

    /**
     * GET  /general-sales-summaries : get all the generalSalesSummaries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of generalSalesSummaries in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/general-sales-summaries")
    @Timed
    public ResponseEntity<List<GeneralSalesSummaryDTO>> getAllGeneralSalesSummaries(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of GeneralSalesSummaries");
        Page<GeneralSalesSummary> page = generalSalesSummaryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/general-sales-summaries");
        return new ResponseEntity<>(generalSalesSummaryMapper.generalSalesSummariesToGeneralSalesSummaryDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /general-sales-summaries/:id : get the "id" generalSalesSummary.
     *
     * @param id the id of the generalSalesSummaryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the generalSalesSummaryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/general-sales-summaries/{id}")
    @Timed
    public ResponseEntity<GeneralSalesSummaryDTO> getGeneralSalesSummary(@PathVariable Long id) {
        log.debug("REST request to get GeneralSalesSummary : {}", id);
        GeneralSalesSummary generalSalesSummary = generalSalesSummaryRepository.findOne(id);
        GeneralSalesSummaryDTO generalSalesSummaryDTO = generalSalesSummaryMapper.generalSalesSummaryToGeneralSalesSummaryDTO(generalSalesSummary);
        return Optional.ofNullable(generalSalesSummaryDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
