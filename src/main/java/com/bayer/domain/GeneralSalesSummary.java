package com.bayer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A GeneralSalesSummary.
 */
@Entity
@Table(name = "general_sales_summary")
public class GeneralSalesSummary implements Serializable {

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

    @OneToMany(mappedBy = "generalSalesSummary")
    @JsonIgnore
    private Set<SalesTransaction> transactions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public GeneralSalesSummary year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public GeneralSalesSummary month(Integer month) {
        this.month = month;
        return this;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Set<SalesTransaction> getTransactions() {
        return transactions;
    }

    public GeneralSalesSummary transactions(Set<SalesTransaction> salesTransactions) {
        this.transactions = salesTransactions;
        return this;
    }

    public GeneralSalesSummary addTransactions(SalesTransaction salesTransaction) {
        transactions.add(salesTransaction);
        salesTransaction.setGeneralSalesSummary(this);
        return this;
    }

    public GeneralSalesSummary removeTransactions(SalesTransaction salesTransaction) {
        transactions.remove(salesTransaction);
        salesTransaction.setGeneralSalesSummary(null);
        return this;
    }

    public void setTransactions(Set<SalesTransaction> salesTransactions) {
        this.transactions = salesTransactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GeneralSalesSummary generalSalesSummary = (GeneralSalesSummary) o;
        if (generalSalesSummary.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, generalSalesSummary.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "GeneralSalesSummary{" +
            "id=" + id +
            ", year='" + year + "'" +
            ", month='" + month + "'" +
            '}';
    }
}
