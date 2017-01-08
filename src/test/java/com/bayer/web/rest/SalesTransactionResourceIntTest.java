package com.bayer.web.rest;

import com.bayer.ReportApp;

import com.bayer.domain.SalesTransaction;
import com.bayer.repository.SalesTransactionRepository;
import com.bayer.service.SalesTransactionService;
import com.bayer.service.dto.SalesTransactionDTO;
import com.bayer.service.mapper.SalesTransactionMapper;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.util.List;

import static com.bayer.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SalesTransactionResource REST controller.
 *
 * @see SalesTransactionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReportApp.class)
public class SalesTransactionResourceIntTest {

    private static final ZonedDateTime DEFAULT_TRANSACTION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TRANSACTION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final BigDecimal DEFAULT_TRANSACTION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TRANSACTION_AMOUNT = new BigDecimal(2);

    @Inject
    private SalesTransactionRepository salesTransactionRepository;

    @Inject
    private SalesTransactionMapper salesTransactionMapper;

    @Inject
    private SalesTransactionService salesTransactionService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSalesTransactionMockMvc;

    private SalesTransaction salesTransaction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SalesTransactionResource salesTransactionResource = new SalesTransactionResource();
        ReflectionTestUtils.setField(salesTransactionResource, "salesTransactionService", salesTransactionService);
        this.restSalesTransactionMockMvc = MockMvcBuilders.standaloneSetup(salesTransactionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalesTransaction createEntity(EntityManager em) {
        SalesTransaction salesTransaction = new SalesTransaction()
                .transactionDate(DEFAULT_TRANSACTION_DATE)
                .transactionAmount(DEFAULT_TRANSACTION_AMOUNT);
        return salesTransaction;
    }

    @Before
    public void initTest() {
        salesTransaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createSalesTransaction() throws Exception {
        int databaseSizeBeforeCreate = salesTransactionRepository.findAll().size();

        // Create the SalesTransaction
        SalesTransactionDTO salesTransactionDTO = salesTransactionMapper.salesTransactionToSalesTransactionDTO(salesTransaction);

        restSalesTransactionMockMvc.perform(post("/api/sales-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesTransactionDTO)))
            .andExpect(status().isCreated());

        // Validate the SalesTransaction in the database
        List<SalesTransaction> salesTransactionList = salesTransactionRepository.findAll();
        assertThat(salesTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        SalesTransaction testSalesTransaction = salesTransactionList.get(salesTransactionList.size() - 1);
        assertThat(testSalesTransaction.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testSalesTransaction.getTransactionAmount()).isEqualTo(DEFAULT_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void createSalesTransactionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = salesTransactionRepository.findAll().size();

        // Create the SalesTransaction with an existing ID
        SalesTransaction existingSalesTransaction = new SalesTransaction();
        existingSalesTransaction.setId(1L);
        SalesTransactionDTO existingSalesTransactionDTO = salesTransactionMapper.salesTransactionToSalesTransactionDTO(existingSalesTransaction);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalesTransactionMockMvc.perform(post("/api/sales-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingSalesTransactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SalesTransaction> salesTransactionList = salesTransactionRepository.findAll();
        assertThat(salesTransactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSalesTransactions() throws Exception {
        // Initialize the database
        salesTransactionRepository.saveAndFlush(salesTransaction);

        // Get all the salesTransactionList
        restSalesTransactionMockMvc.perform(get("/api/sales-transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salesTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(sameInstant(DEFAULT_TRANSACTION_DATE))))
            .andExpect(jsonPath("$.[*].transactionAmount").value(hasItem(DEFAULT_TRANSACTION_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getSalesTransaction() throws Exception {
        // Initialize the database
        salesTransactionRepository.saveAndFlush(salesTransaction);

        // Get the salesTransaction
        restSalesTransactionMockMvc.perform(get("/api/sales-transactions/{id}", salesTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(salesTransaction.getId().intValue()))
            .andExpect(jsonPath("$.transactionDate").value(sameInstant(DEFAULT_TRANSACTION_DATE)))
            .andExpect(jsonPath("$.transactionAmount").value(DEFAULT_TRANSACTION_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSalesTransaction() throws Exception {
        // Get the salesTransaction
        restSalesTransactionMockMvc.perform(get("/api/sales-transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSalesTransaction() throws Exception {
        // Initialize the database
        salesTransactionRepository.saveAndFlush(salesTransaction);
        int databaseSizeBeforeUpdate = salesTransactionRepository.findAll().size();

        // Update the salesTransaction
        SalesTransaction updatedSalesTransaction = salesTransactionRepository.findOne(salesTransaction.getId());
        updatedSalesTransaction
                .transactionDate(UPDATED_TRANSACTION_DATE)
                .transactionAmount(UPDATED_TRANSACTION_AMOUNT);
        SalesTransactionDTO salesTransactionDTO = salesTransactionMapper.salesTransactionToSalesTransactionDTO(updatedSalesTransaction);

        restSalesTransactionMockMvc.perform(put("/api/sales-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesTransactionDTO)))
            .andExpect(status().isOk());

        // Validate the SalesTransaction in the database
        List<SalesTransaction> salesTransactionList = salesTransactionRepository.findAll();
        assertThat(salesTransactionList).hasSize(databaseSizeBeforeUpdate);
        SalesTransaction testSalesTransaction = salesTransactionList.get(salesTransactionList.size() - 1);
        assertThat(testSalesTransaction.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testSalesTransaction.getTransactionAmount()).isEqualTo(UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingSalesTransaction() throws Exception {
        int databaseSizeBeforeUpdate = salesTransactionRepository.findAll().size();

        // Create the SalesTransaction
        SalesTransactionDTO salesTransactionDTO = salesTransactionMapper.salesTransactionToSalesTransactionDTO(salesTransaction);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSalesTransactionMockMvc.perform(put("/api/sales-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesTransactionDTO)))
            .andExpect(status().isCreated());

        // Validate the SalesTransaction in the database
        List<SalesTransaction> salesTransactionList = salesTransactionRepository.findAll();
        assertThat(salesTransactionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSalesTransaction() throws Exception {
        // Initialize the database
        salesTransactionRepository.saveAndFlush(salesTransaction);
        int databaseSizeBeforeDelete = salesTransactionRepository.findAll().size();

        // Get the salesTransaction
        restSalesTransactionMockMvc.perform(delete("/api/sales-transactions/{id}", salesTransaction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SalesTransaction> salesTransactionList = salesTransactionRepository.findAll();
        assertThat(salesTransactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
