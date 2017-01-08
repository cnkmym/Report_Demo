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
     * POST  /general-sales-summaries : Create a new generalSalesSummary.
     *
     * @param generalSalesSummaryDTO the generalSalesSummaryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new generalSalesSummaryDTO, or with status 400 (Bad Request) if the generalSalesSummary has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/general-sales-summaries")
    @Timed
    public ResponseEntity<GeneralSalesSummaryDTO> createGeneralSalesSummary(@Valid @RequestBody GeneralSalesSummaryDTO generalSalesSummaryDTO) throws URISyntaxException {
        log.debug("REST request to save GeneralSalesSummary : {}", generalSalesSummaryDTO);
        if (generalSalesSummaryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("generalSalesSummary", "idexists", "A new generalSalesSummary cannot already have an ID")).body(null);
        }
        GeneralSalesSummary generalSalesSummary = generalSalesSummaryMapper.generalSalesSummaryDTOToGeneralSalesSummary(generalSalesSummaryDTO);
        generalSalesSummary = generalSalesSummaryRepository.save(generalSalesSummary);
        GeneralSalesSummaryDTO result = generalSalesSummaryMapper.generalSalesSummaryToGeneralSalesSummaryDTO(generalSalesSummary);
        return ResponseEntity.created(new URI("/api/general-sales-summaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("generalSalesSummary", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /general-sales-summaries : Updates an existing generalSalesSummary.
     *
     * @param generalSalesSummaryDTO the generalSalesSummaryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated generalSalesSummaryDTO,
     * or with status 400 (Bad Request) if the generalSalesSummaryDTO is not valid,
     * or with status 500 (Internal Server Error) if the generalSalesSummaryDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/general-sales-summaries")
    @Timed
    public ResponseEntity<GeneralSalesSummaryDTO> updateGeneralSalesSummary(@Valid @RequestBody GeneralSalesSummaryDTO generalSalesSummaryDTO) throws URISyntaxException {
        log.debug("REST request to update GeneralSalesSummary : {}", generalSalesSummaryDTO);
        if (generalSalesSummaryDTO.getId() == null) {
            return createGeneralSalesSummary(generalSalesSummaryDTO);
        }
        GeneralSalesSummary generalSalesSummary = generalSalesSummaryMapper.generalSalesSummaryDTOToGeneralSalesSummary(generalSalesSummaryDTO);
        generalSalesSummary = generalSalesSummaryRepository.save(generalSalesSummary);
        GeneralSalesSummaryDTO result = generalSalesSummaryMapper.generalSalesSummaryToGeneralSalesSummaryDTO(generalSalesSummary);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("generalSalesSummary", generalSalesSummaryDTO.getId().toString()))
            .body(result);
    }

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

    /**
     * DELETE  /general-sales-summaries/:id : delete the "id" generalSalesSummary.
     *
     * @param id the id of the generalSalesSummaryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/general-sales-summaries/{id}")
    @Timed
    public ResponseEntity<Void> deleteGeneralSalesSummary(@PathVariable Long id) {
        log.debug("REST request to delete GeneralSalesSummary : {}", id);
        generalSalesSummaryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("generalSalesSummary", id.toString())).build();
    }

}
