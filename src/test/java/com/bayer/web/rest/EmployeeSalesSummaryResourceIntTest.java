package com.bayer.web.rest;

import com.bayer.ReportApp;

import com.bayer.domain.EmployeeSalesSummary;
import com.bayer.repository.EmployeeSalesSummaryRepository;
import com.bayer.service.dto.EmployeeSalesSummaryDTO;
import com.bayer.service.mapper.EmployeeSalesSummaryMapper;

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
 * Test class for the EmployeeSalesSummaryResource REST controller.
 *
 * @see EmployeeSalesSummaryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReportApp.class)
public class EmployeeSalesSummaryResourceIntTest {

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Integer DEFAULT_MONTH = 1;
    private static final Integer UPDATED_MONTH = 2;

    @Inject
    private EmployeeSalesSummaryRepository employeeSalesSummaryRepository;

    @Inject
    private EmployeeSalesSummaryMapper employeeSalesSummaryMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEmployeeSalesSummaryMockMvc;

    private EmployeeSalesSummary employeeSalesSummary;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmployeeSalesSummaryResource employeeSalesSummaryResource = new EmployeeSalesSummaryResource();
        ReflectionTestUtils.setField(employeeSalesSummaryResource, "employeeSalesSummaryRepository", employeeSalesSummaryRepository);
        ReflectionTestUtils.setField(employeeSalesSummaryResource, "employeeSalesSummaryMapper", employeeSalesSummaryMapper);
        this.restEmployeeSalesSummaryMockMvc = MockMvcBuilders.standaloneSetup(employeeSalesSummaryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeSalesSummary createEntity(EntityManager em) {
        EmployeeSalesSummary employeeSalesSummary = new EmployeeSalesSummary()
                .year(DEFAULT_YEAR)
                .month(DEFAULT_MONTH);
        return employeeSalesSummary;
    }

    @Before
    public void initTest() {
        employeeSalesSummary = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmployeeSalesSummary() throws Exception {
        int databaseSizeBeforeCreate = employeeSalesSummaryRepository.findAll().size();

        // Create the EmployeeSalesSummary
        EmployeeSalesSummaryDTO employeeSalesSummaryDTO = employeeSalesSummaryMapper.employeeSalesSummaryToEmployeeSalesSummaryDTO(employeeSalesSummary);

        restEmployeeSalesSummaryMockMvc.perform(post("/api/employee-sales-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeSalesSummaryDTO)))
            .andExpect(status().isCreated());

        // Validate the EmployeeSalesSummary in the database
        List<EmployeeSalesSummary> employeeSalesSummaryList = employeeSalesSummaryRepository.findAll();
        assertThat(employeeSalesSummaryList).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeSalesSummary testEmployeeSalesSummary = employeeSalesSummaryList.get(employeeSalesSummaryList.size() - 1);
        assertThat(testEmployeeSalesSummary.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testEmployeeSalesSummary.getMonth()).isEqualTo(DEFAULT_MONTH);
    }

    @Test
    @Transactional
    public void createEmployeeSalesSummaryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = employeeSalesSummaryRepository.findAll().size();

        // Create the EmployeeSalesSummary with an existing ID
        EmployeeSalesSummary existingEmployeeSalesSummary = new EmployeeSalesSummary();
        existingEmployeeSalesSummary.setId(1L);
        EmployeeSalesSummaryDTO existingEmployeeSalesSummaryDTO = employeeSalesSummaryMapper.employeeSalesSummaryToEmployeeSalesSummaryDTO(existingEmployeeSalesSummary);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeSalesSummaryMockMvc.perform(post("/api/employee-sales-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingEmployeeSalesSummaryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EmployeeSalesSummary> employeeSalesSummaryList = employeeSalesSummaryRepository.findAll();
        assertThat(employeeSalesSummaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeSalesSummaryRepository.findAll().size();
        // set the field null
        employeeSalesSummary.setYear(null);

        // Create the EmployeeSalesSummary, which fails.
        EmployeeSalesSummaryDTO employeeSalesSummaryDTO = employeeSalesSummaryMapper.employeeSalesSummaryToEmployeeSalesSummaryDTO(employeeSalesSummary);

        restEmployeeSalesSummaryMockMvc.perform(post("/api/employee-sales-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeSalesSummaryDTO)))
            .andExpect(status().isBadRequest());

        List<EmployeeSalesSummary> employeeSalesSummaryList = employeeSalesSummaryRepository.findAll();
        assertThat(employeeSalesSummaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeSalesSummaryRepository.findAll().size();
        // set the field null
        employeeSalesSummary.setMonth(null);

        // Create the EmployeeSalesSummary, which fails.
        EmployeeSalesSummaryDTO employeeSalesSummaryDTO = employeeSalesSummaryMapper.employeeSalesSummaryToEmployeeSalesSummaryDTO(employeeSalesSummary);

        restEmployeeSalesSummaryMockMvc.perform(post("/api/employee-sales-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeSalesSummaryDTO)))
            .andExpect(status().isBadRequest());

        List<EmployeeSalesSummary> employeeSalesSummaryList = employeeSalesSummaryRepository.findAll();
        assertThat(employeeSalesSummaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEmployeeSalesSummaries() throws Exception {
        // Initialize the database
        employeeSalesSummaryRepository.saveAndFlush(employeeSalesSummary);

        // Get all the employeeSalesSummaryList
        restEmployeeSalesSummaryMockMvc.perform(get("/api/employee-sales-summaries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeSalesSummary.getId().intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)));
    }

    @Test
    @Transactional
    public void getEmployeeSalesSummary() throws Exception {
        // Initialize the database
        employeeSalesSummaryRepository.saveAndFlush(employeeSalesSummary);

        // Get the employeeSalesSummary
        restEmployeeSalesSummaryMockMvc.perform(get("/api/employee-sales-summaries/{id}", employeeSalesSummary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(employeeSalesSummary.getId().intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH));
    }

    @Test
    @Transactional
    public void getNonExistingEmployeeSalesSummary() throws Exception {
        // Get the employeeSalesSummary
        restEmployeeSalesSummaryMockMvc.perform(get("/api/employee-sales-summaries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployeeSalesSummary() throws Exception {
        // Initialize the database
        employeeSalesSummaryRepository.saveAndFlush(employeeSalesSummary);
        int databaseSizeBeforeUpdate = employeeSalesSummaryRepository.findAll().size();

        // Update the employeeSalesSummary
        EmployeeSalesSummary updatedEmployeeSalesSummary = employeeSalesSummaryRepository.findOne(employeeSalesSummary.getId());
        updatedEmployeeSalesSummary
                .year(UPDATED_YEAR)
                .month(UPDATED_MONTH);
        EmployeeSalesSummaryDTO employeeSalesSummaryDTO = employeeSalesSummaryMapper.employeeSalesSummaryToEmployeeSalesSummaryDTO(updatedEmployeeSalesSummary);

        restEmployeeSalesSummaryMockMvc.perform(put("/api/employee-sales-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeSalesSummaryDTO)))
            .andExpect(status().isOk());

        // Validate the EmployeeSalesSummary in the database
        List<EmployeeSalesSummary> employeeSalesSummaryList = employeeSalesSummaryRepository.findAll();
        assertThat(employeeSalesSummaryList).hasSize(databaseSizeBeforeUpdate);
        EmployeeSalesSummary testEmployeeSalesSummary = employeeSalesSummaryList.get(employeeSalesSummaryList.size() - 1);
        assertThat(testEmployeeSalesSummary.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testEmployeeSalesSummary.getMonth()).isEqualTo(UPDATED_MONTH);
    }

    @Test
    @Transactional
    public void updateNonExistingEmployeeSalesSummary() throws Exception {
        int databaseSizeBeforeUpdate = employeeSalesSummaryRepository.findAll().size();

        // Create the EmployeeSalesSummary
        EmployeeSalesSummaryDTO employeeSalesSummaryDTO = employeeSalesSummaryMapper.employeeSalesSummaryToEmployeeSalesSummaryDTO(employeeSalesSummary);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEmployeeSalesSummaryMockMvc.perform(put("/api/employee-sales-summaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeSalesSummaryDTO)))
            .andExpect(status().isCreated());

        // Validate the EmployeeSalesSummary in the database
        List<EmployeeSalesSummary> employeeSalesSummaryList = employeeSalesSummaryRepository.findAll();
        assertThat(employeeSalesSummaryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEmployeeSalesSummary() throws Exception {
        // Initialize the database
        employeeSalesSummaryRepository.saveAndFlush(employeeSalesSummary);
        int databaseSizeBeforeDelete = employeeSalesSummaryRepository.findAll().size();

        // Get the employeeSalesSummary
        restEmployeeSalesSummaryMockMvc.perform(delete("/api/employee-sales-summaries/{id}", employeeSalesSummary.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EmployeeSalesSummary> employeeSalesSummaryList = employeeSalesSummaryRepository.findAll();
        assertThat(employeeSalesSummaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
