package com.bayer.web.rest;

import com.bayer.ReportApp;

import com.bayer.domain.GeneralSalesSummary;
import com.bayer.repository.GeneralSalesSummaryRepository;
import com.bayer.service.dto.GeneralSalesSummaryDTO;
import com.bayer.service.mapper.GeneralSalesSummaryMapper;

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
 * Test class for the GeneralSalesSummaryResource REST controller.
 *
 * @see GeneralSalesSummaryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReportApp.class)
public class GeneralSalesSummaryResourceIntTest {

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Integer DEFAULT_MONTH = 1;
    private static final Integer UPDATED_MONTH = 2;

    @Inject
    private GeneralSalesSummaryRepository generalSalesSummaryRepository;

    @Inject
    private GeneralSalesSummaryMapper generalSalesSummaryMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restGeneralSalesSummaryMockMvc;

    private GeneralSalesSummary generalSalesSummary;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GeneralSalesSummaryResource generalSalesSummaryResource = new GeneralSalesSummaryResource();
        ReflectionTestUtils.setField(generalSalesSummaryResource, "generalSalesSummaryRepository", generalSalesSummaryRepository);
        ReflectionTestUtils.setField(generalSalesSummaryResource, "generalSalesSummaryMapper", generalSalesSummaryMapper);
        this.restGeneralSalesSummaryMockMvc = MockMvcBuilders.standaloneSetup(generalSalesSummaryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GeneralSalesSummary createEntity(EntityManager em) {
        GeneralSalesSummary generalSalesSummary = new GeneralSalesSummary()
                .year(DEFAULT_YEAR)
                .month(DEFAULT_MONTH);
        return generalSalesSummary;
    }

    @Before
    public void initTest() {
        generalSalesSummary = createEntity(em);
    }

    @Test
    @Transactional
    public void createGeneralSalesSummary() throws Exception {
        int databaseSizeBeforeCreate = generalSalesSummaryRepository.findAll().size();

        // Create the GeneralSalesSummary
        GeneralSalesSummaryDTO generalSalesSummaryDTO = generalSalesSummaryMapper.generalSalesSummaryToGeneralSalesSummaryDTO(generalSalesSummary);

        restGeneralSalesSummaryMockMvc.perform(post("/api/general-sales-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(generalSalesSummaryDTO)))
            .andExpect(status().isCreated());

        // Validate the GeneralSalesSummary in the database
        List<GeneralSalesSummary> generalSalesSummaryList = generalSalesSummaryRepository.findAll();
        assertThat(generalSalesSummaryList).hasSize(databaseSizeBeforeCreate + 1);
        GeneralSalesSummary testGeneralSalesSummary = generalSalesSummaryList.get(generalSalesSummaryList.size() - 1);
        assertThat(testGeneralSalesSummary.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testGeneralSalesSummary.getMonth()).isEqualTo(DEFAULT_MONTH);
    }

    @Test
    @Transactional
    public void createGeneralSalesSummaryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = generalSalesSummaryRepository.findAll().size();

        // Create the GeneralSalesSummary with an existing ID
        GeneralSalesSummary existingGeneralSalesSummary = new GeneralSalesSummary();
        existingGeneralSalesSummary.setId(1L);
        GeneralSalesSummaryDTO existingGeneralSalesSummaryDTO = generalSalesSummaryMapper.generalSalesSummaryToGeneralSalesSummaryDTO(existingGeneralSalesSummary);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeneralSalesSummaryMockMvc.perform(post("/api/general-sales-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingGeneralSalesSummaryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<GeneralSalesSummary> generalSalesSummaryList = generalSalesSummaryRepository.findAll();
        assertThat(generalSalesSummaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = generalSalesSummaryRepository.findAll().size();
        // set the field null
        generalSalesSummary.setYear(null);

        // Create the GeneralSalesSummary, which fails.
        GeneralSalesSummaryDTO generalSalesSummaryDTO = generalSalesSummaryMapper.generalSalesSummaryToGeneralSalesSummaryDTO(generalSalesSummary);

        restGeneralSalesSummaryMockMvc.perform(post("/api/general-sales-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(generalSalesSummaryDTO)))
            .andExpect(status().isBadRequest());

        List<GeneralSalesSummary> generalSalesSummaryList = generalSalesSummaryRepository.findAll();
        assertThat(generalSalesSummaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = generalSalesSummaryRepository.findAll().size();
        // set the field null
        generalSalesSummary.setMonth(null);

        // Create the GeneralSalesSummary, which fails.
        GeneralSalesSummaryDTO generalSalesSummaryDTO = generalSalesSummaryMapper.generalSalesSummaryToGeneralSalesSummaryDTO(generalSalesSummary);

        restGeneralSalesSummaryMockMvc.perform(post("/api/general-sales-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(generalSalesSummaryDTO)))
            .andExpect(status().isBadRequest());

        List<GeneralSalesSummary> generalSalesSummaryList = generalSalesSummaryRepository.findAll();
        assertThat(generalSalesSummaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGeneralSalesSummaries() throws Exception {
        // Initialize the database
        generalSalesSummaryRepository.saveAndFlush(generalSalesSummary);

        // Get all the generalSalesSummaryList
        restGeneralSalesSummaryMockMvc.perform(get("/api/general-sales-summaries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(generalSalesSummary.getId().intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)));
    }

    @Test
    @Transactional
    public void getGeneralSalesSummary() throws Exception {
        // Initialize the database
        generalSalesSummaryRepository.saveAndFlush(generalSalesSummary);

        // Get the generalSalesSummary
        restGeneralSalesSummaryMockMvc.perform(get("/api/general-sales-summaries/{id}", generalSalesSummary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(generalSalesSummary.getId().intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH));
    }

    @Test
    @Transactional
    public void getNonExistingGeneralSalesSummary() throws Exception {
        // Get the generalSalesSummary
        restGeneralSalesSummaryMockMvc.perform(get("/api/general-sales-summaries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGeneralSalesSummary() throws Exception {
        // Initialize the database
        generalSalesSummaryRepository.saveAndFlush(generalSalesSummary);
        int databaseSizeBeforeUpdate = generalSalesSummaryRepository.findAll().size();

        // Update the generalSalesSummary
        GeneralSalesSummary updatedGeneralSalesSummary = generalSalesSummaryRepository.findOne(generalSalesSummary.getId());
        updatedGeneralSalesSummary
                .year(UPDATED_YEAR)
                .month(UPDATED_MONTH);
        GeneralSalesSummaryDTO generalSalesSummaryDTO = generalSalesSummaryMapper.generalSalesSummaryToGeneralSalesSummaryDTO(updatedGeneralSalesSummary);

        restGeneralSalesSummaryMockMvc.perform(put("/api/general-sales-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(generalSalesSummaryDTO)))
            .andExpect(status().isOk());

        // Validate the GeneralSalesSummary in the database
        List<GeneralSalesSummary> generalSalesSummaryList = generalSalesSummaryRepository.findAll();
        assertThat(generalSalesSummaryList).hasSize(databaseSizeBeforeUpdate);
        GeneralSalesSummary testGeneralSalesSummary = generalSalesSummaryList.get(generalSalesSummaryList.size() - 1);
        assertThat(testGeneralSalesSummary.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testGeneralSalesSummary.getMonth()).isEqualTo(UPDATED_MONTH);
    }

    @Test
    @Transactional
    public void updateNonExistingGeneralSalesSummary() throws Exception {
        int databaseSizeBeforeUpdate = generalSalesSummaryRepository.findAll().size();

        // Create the GeneralSalesSummary
        GeneralSalesSummaryDTO generalSalesSummaryDTO = generalSalesSummaryMapper.generalSalesSummaryToGeneralSalesSummaryDTO(generalSalesSummary);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGeneralSalesSummaryMockMvc.perform(put("/api/general-sales-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(generalSalesSummaryDTO)))
            .andExpect(status().isCreated());

        // Validate the GeneralSalesSummary in the database
        List<GeneralSalesSummary> generalSalesSummaryList = generalSalesSummaryRepository.findAll();
        assertThat(generalSalesSummaryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGeneralSalesSummary() throws Exception {
        // Initialize the database
        generalSalesSummaryRepository.saveAndFlush(generalSalesSummary);
        int databaseSizeBeforeDelete = generalSalesSummaryRepository.findAll().size();

        // Get the generalSalesSummary
        restGeneralSalesSummaryMockMvc.perform(delete("/api/general-sales-summaries/{id}", generalSalesSummary.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<GeneralSalesSummary> generalSalesSummaryList = generalSalesSummaryRepository.findAll();
        assertThat(generalSalesSummaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
