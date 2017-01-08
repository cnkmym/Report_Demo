package com.bayer.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the SalesTransaction entity.
 */
public class SalesTransactionDTO implements Serializable {

    private Long id;

    private ZonedDateTime transactionDate;

    private BigDecimal transactionAmount;


    private Long productSalesSummaryId;
    
    private Long employeeSalesSummaryId;
    
    private Long generalSalesSummaryId;
    
    private Long empolyeeId;
    
    private Long productId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public ZonedDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(ZonedDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Long getProductSalesSummaryId() {
        return productSalesSummaryId;
    }

    public void setProductSalesSummaryId(Long productSalesSummaryId) {
        this.productSalesSummaryId = productSalesSummaryId;
    }

    public Long getEmployeeSalesSummaryId() {
        return employeeSalesSummaryId;
    }

    public void setEmployeeSalesSummaryId(Long employeeSalesSummaryId) {
        this.employeeSalesSummaryId = employeeSalesSummaryId;
    }

    public Long getGeneralSalesSummaryId() {
        return generalSalesSummaryId;
    }

    public void setGeneralSalesSummaryId(Long generalSalesSummaryId) {
        this.generalSalesSummaryId = generalSalesSummaryId;
    }

    public Long getEmpolyeeId() {
        return empolyeeId;
    }

    public void setEmpolyeeId(Long employeeId) {
        this.empolyeeId = employeeId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SalesTransactionDTO salesTransactionDTO = (SalesTransactionDTO) o;

        if ( ! Objects.equals(id, salesTransactionDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SalesTransactionDTO{" +
            "id=" + id +
            ", transactionDate='" + transactionDate + "'" +
            ", transactionAmount='" + transactionAmount + "'" +
            '}';
    }
}
