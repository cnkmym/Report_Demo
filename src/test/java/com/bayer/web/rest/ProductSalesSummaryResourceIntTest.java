package com.bayer.web.rest;

import com.bayer.ReportApp;

import com.bayer.domain.ProductSalesSummary;
import com.bayer.repository.ProductSalesSummaryRepository;
import com.bayer.service.dto.ProductSalesSummaryDTO;
import com.bayer.service.mapper.ProductSalesSummaryMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProductSalesSummaryResource REST controller.
 *
 * @see ProductSalesSummaryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReportApp.class)
public class ProductSalesSummaryResourceIntTest {

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Integer DEFAULT_MONTH = 1;
    private static final Integer UPDATED_MONTH = 2;

    @Inject
    private ProductSalesSummaryRepository productSalesSummaryRepository;

    @Inject
    private ProductSalesSummaryMapper productSalesSummaryMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProductSalesSummaryMockMvc;

    private ProductSalesSummary productSalesSummary;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProductSalesSummaryResource productSalesSummaryResource = new ProductSalesSummaryResource();
        ReflectionTestUtils.setField(productSalesSummaryResource, "productSalesSummaryRepository", productSalesSummaryRepository);
        ReflectionTestUtils.setField(productSalesSummaryResource, "productSalesSummaryMapper", productSalesSummaryMapper);
        this.restProductSalesSummaryMockMvc = MockMvcBuilders.standaloneSetup(productSalesSummaryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductSalesSummary createEntity(EntityManager em) {
        ProductSalesSummary productSalesSummary = new ProductSalesSummary()
                .year(DEFAULT_YEAR)
                .month(DEFAULT_MONTH);
        return productSalesSummary;
    }

    @Before
    public void initTest() {
        productSalesSummary = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductSalesSummary() throws Exception {
        int databaseSizeBeforeCreate = productSalesSummaryRepository.findAll().size();

        // Create the ProductSalesSummary
        ProductSalesSummaryDTO productSalesSummaryDTO = productSalesSummaryMapper.productSalesSummaryToProductSalesSummaryDTO(productSalesSummary);

        restProductSalesSummaryMockMvc.perform(post("/api/product-sales-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productSalesSummaryDTO)))
            .andExpect(status().isCreated());

        // Validate the ProductSalesSummary in the database
        List<ProductSalesSummary> productSalesSummaryList = productSalesSummaryRepository.findAll();
        assertThat(productSalesSummaryList).hasSize(databaseSizeBeforeCreate + 1);
        ProductSalesSummary testProductSalesSummary = productSalesSummaryList.get(productSalesSummaryList.size() - 1);
        assertThat(testProductSalesSummary.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testProductSalesSummary.getMonth()).isEqualTo(DEFAULT_MONTH);
    }

    @Test
    @Transactional
    public void createProductSalesSummaryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productSalesSummaryRepository.findAll().size();

        // Create the ProductSalesSummary with an existing ID
        ProductSalesSummary existingProductSalesSummary = new ProductSalesSummary();
        existingProductSalesSummary.setId(1L);
        ProductSalesSummaryDTO existingProductSalesSummaryDTO = productSalesSummaryMapper.productSalesSummaryToProductSalesSummaryDTO(existingProductSalesSummary);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductSalesSummaryMockMvc.perform(post("/api/product-sales-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingProductSalesSummaryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ProductSalesSummary> productSalesSummaryList = productSalesSummaryRepository.findAll();
        assertThat(productSalesSummaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = productSalesSummaryRepository.findAll().size();
        // set the field null
        productSalesSummary.setYear(null);

        // Create the ProductSalesSummary, which fails.
        ProductSalesSummaryDTO productSalesSummaryDTO = productSalesSummaryMapper.productSalesSummaryToProductSalesSummaryDTO(productSalesSummary);

        restProductSalesSummaryMockMvc.perform(post("/api/product-sales-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productSalesSummaryDTO)))
            .andExpect(status().isBadRequest());

        List<ProductSalesSummary> productSalesSummaryList = productSalesSummaryRepository.findAll();
        assertThat(productSalesSummaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = productSalesSummaryRepository.findAll().size();
        // set the field null
        productSalesSummary.setMonth(null);

        // Create the ProductSalesSummary, which fails.
        ProductSalesSummaryDTO productSalesSummaryDTO = productSalesSummaryMapper.productSalesSummaryToProductSalesSummaryDTO(productSalesSummary);

        restProductSalesSummaryMockMvc.perform(post("/api/product-sales-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productSalesSummaryDTO)))
            .andExpect(status().isBadRequest());

        List<ProductSalesSummary> productSalesSummaryList = productSalesSummaryRepository.findAll();
        assertThat(productSalesSummaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductSalesSummaries() throws Exception {
        // Initialize the database
        productSalesSummaryRepository.saveAndFlush(productSalesSummary);

        // Get all the productSalesSummaryList
        restProductSalesSummaryMockMvc.perform(get("/api/product-sales-summaries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productSalesSummary.getId().intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)));
    }

    @Test
    @Transactional
    public void getProductSalesSummary() throws Exception {
        // Initialize the database
        productSalesSummaryRepository.saveAndFlush(productSalesSummary);

        // Get the productSalesSummary
        restProductSalesSummaryMockMvc.perform(get("/api/product-sales-summaries/{id}", productSalesSummary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productSalesSummary.getId().intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH));
    }

    @Test
    @Transactional
    public void getNonExistingProductSalesSummary() throws Exception {
        // Get the productSalesSummary
        restProductSalesSummaryMockMvc.perform(get("/api/product-sales-summaries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductSalesSummary() throws Exception {
        // Initialize the database
        productSalesSummaryRepository.saveAndFlush(productSalesSummary);
        int databaseSizeBeforeUpdate = productSalesSummaryRepository.findAll().size();

        // Update the productSalesSummary
        ProductSalesSummary updatedProductSalesSummary = productSalesSummaryRepository.findOne(productSalesSummary.getId());
        updatedProductSalesSummary
                .year(UPDATED_YEAR)
                .month(UPDATED_MONTH);
        ProductSalesSummaryDTO productSalesSummaryDTO = productSalesSummaryMapper.productSalesSummaryToProductSalesSummaryDTO(updatedProductSalesSummary);

        restProductSalesSummaryMockMvc.perform(put("/api/product-sales-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productSalesSummaryDTO)))
            .andExpect(status().isOk());

        // Validate the ProductSalesSummary in the database
        List<ProductSalesSummary> productSalesSummaryList = productSalesSummaryRepository.findAll();
        assertThat(productSalesSummaryList).hasSize(databaseSizeBeforeUpdate);
        ProductSalesSummary testProductSalesSummary = productSalesSummaryList.get(productSalesSummaryList.size() - 1);
        assertThat(testProductSalesSummary.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testProductSalesSummary.getMonth()).isEqualTo(UPDATED_MONTH);
    }

    @Test
    @Transactional
    public void updateNonExistingProductSalesSummary() throws Exception {
        int databaseSizeBeforeUpdate = productSalesSummaryRepository.findAll().size();

        // Create the ProductSalesSummary
        ProductSalesSummaryDTO productSalesSummaryDTO = productSalesSummaryMapper.productSalesSummaryToProductSalesSummaryDTO(productSalesSummary);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProductSalesSummaryMockMvc.perform(put("/api/product-sales-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productSalesSummaryDTO)))
            .andExpect(status().isCreated());

        // Validate the ProductSalesSummary in the database
        List<ProductSalesSummary> productSalesSummaryList = productSalesSummaryRepository.findAll();
        assertThat(productSalesSummaryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProductSalesSummary() throws Exception {
        // Initialize the database
        productSalesSummaryRepository.saveAndFlush(productSalesSummary);
        int databaseSizeBeforeDelete = productSalesSummaryRepository.findAll().size();

        // Get the productSalesSummary
        restProductSalesSummaryMockMvc.perform(delete("/api/product-sales-summaries/{id}", productSalesSummary.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProductSalesSummary> productSalesSummaryList = productSalesSummaryRepository.findAll();
        assertThat(productSalesSummaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
