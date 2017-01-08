package com.bayer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bayer.domain.ProductSalesSummary;

import com.bayer.repository.ProductSalesSummaryRepository;
import com.bayer.web.rest.util.HeaderUtil;
import com.bayer.web.rest.util.PaginationUtil;
import com.bayer.service.dto.ProductSalesSummaryDTO;
import com.bayer.service.mapper.ProductSalesSummaryMapper;

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
 * REST controller for managing ProductSalesSummary.
 */
@RestController
@RequestMapping("/api")
public class ProductSalesSummaryResource {

    private final Logger log = LoggerFactory.getLogger(ProductSalesSummaryResource.class);
        
    @Inject
    private ProductSalesSummaryRepository productSalesSummaryRepository;

    @Inject
    private ProductSalesSummaryMapper productSalesSummaryMapper;

    /**
     * GET  /product-sales-summaries : get all the productSalesSummaries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of productSalesSummaries in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/product-sales-summaries")
    @Timed
    public ResponseEntity<List<ProductSalesSummaryDTO>> getAllProductSalesSummaries(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ProductSalesSummaries");
        Page<ProductSalesSummary> page = productSalesSummaryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/product-sales-summaries");
        return new ResponseEntity<>(productSalesSummaryMapper.productSalesSummariesToProductSalesSummaryDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /product-sales-summaries/:id : get the "id" productSalesSummary.
     *
     * @param id the id of the productSalesSummaryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productSalesSummaryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/product-sales-summaries/{id}")
    @Timed
    public ResponseEntity<ProductSalesSummaryDTO> getProductSalesSummary(@PathVariable Long id) {
        log.debug("REST request to get ProductSalesSummary : {}", id);
        ProductSalesSummary productSalesSummary = productSalesSummaryRepository.findOne(id);
        ProductSalesSummaryDTO productSalesSummaryDTO = productSalesSummaryMapper.productSalesSummaryToProductSalesSummaryDTO(productSalesSummary);
        return Optional.ofNullable(productSalesSummaryDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
