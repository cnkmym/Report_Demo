package com.bayer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ProductSalesSummary.
 */
@Entity
@Table(name = "product_sales_summary")
public class ProductSalesSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "year", nullable = false)
    private Integer year;

    @NotNull
    @Column(name = "month", nullable = false)
    private Integer month;

    @OneToMany(mappedBy = "productSalesSummary")
    @JsonIgnore
    private Set<SalesTransaction> transactions = new HashSet<>();

    @ManyToOne
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public ProductSalesSummary year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public ProductSalesSummary month(Integer month) {
        this.month = month;
        return this;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Set<SalesTransaction> getTransactions() {
        return transactions;
    }

    public ProductSalesSummary transactions(Set<SalesTransaction> salesTransactions) {
        this.transactions = salesTransactions;
        return this;
    }

    public ProductSalesSummary addTransactions(SalesTransaction salesTransaction) {
        transactions.add(salesTransaction);
        salesTransaction.setProductSalesSummary(this);
        return this;
    }

    public ProductSalesSummary removeTransactions(SalesTransaction salesTransaction) {
        transactions.remove(salesTransaction);
        salesTransaction.setProductSalesSummary(null);
        return this;
    }

    public void setTransactions(Set<SalesTransaction> salesTransactions) {
        this.transactions = salesTransactions;
    }

    public Product getProduct() {
        return product;
    }

    public ProductSalesSummary product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductSalesSummary productSalesSummary = (ProductSalesSummary) o;
        if (productSalesSummary.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, productSalesSummary.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProductSalesSummary{" +
            "id=" + id +
            ", year='" + year + "'" +
            ", month='" + month + "'" +
            '}';
    }
}
