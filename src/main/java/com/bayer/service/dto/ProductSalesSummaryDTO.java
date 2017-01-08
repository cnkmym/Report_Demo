package com.bayer.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the ProductSalesSummary entity.
 */
public class ProductSalesSummaryDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer year;

    @NotNull
    private Integer month;

    private BigDecimal totalAmount;


    private Long productId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
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

        ProductSalesSummaryDTO productSalesSummaryDTO = (ProductSalesSummaryDTO) o;

        if ( ! Objects.equals(id, productSalesSummaryDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProductSalesSummaryDTO{" +
            "id=" + id +
            ", year='" + year + "'" +
            ", month='" + month + "'" +
            ", totalAmount='" + totalAmount + "'" +
            '}';
    }
}
