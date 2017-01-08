package com.bayer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A EmployeeSalesSummary.
 */
@Entity
@Table(name = "employee_sales_summary")
public class EmployeeSalesSummary implements Serializable {

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

    @Column(name = "total_amount", precision=10, scale=2)
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "employeeSalesSummary")
    @JsonIgnore
    private Set<SalesTransaction> transactions = new HashSet<>();

    @ManyToOne
    private Employee employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public EmployeeSalesSummary year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public EmployeeSalesSummary month(Integer month) {
        this.month = month;
        return this;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public EmployeeSalesSummary totalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Set<SalesTransaction> getTransactions() {
        return transactions;
    }

    public EmployeeSalesSummary transactions(Set<SalesTransaction> salesTransactions) {
        this.transactions = salesTransactions;
        return this;
    }

    public EmployeeSalesSummary addTransactions(SalesTransaction salesTransaction) {
        transactions.add(salesTransaction);
        salesTransaction.setEmployeeSalesSummary(this);
        return this;
    }

    public EmployeeSalesSummary removeTransactions(SalesTransaction salesTransaction) {
        transactions.remove(salesTransaction);
        salesTransaction.setEmployeeSalesSummary(null);
        return this;
    }

    public void setTransactions(Set<SalesTransaction> salesTransactions) {
        this.transactions = salesTransactions;
    }

    public Employee getEmployee() {
        return employee;
    }

    public EmployeeSalesSummary employee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmployeeSalesSummary employeeSalesSummary = (EmployeeSalesSummary) o;
        if (employeeSalesSummary.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, employeeSalesSummary.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EmployeeSalesSummary{" +
            "id=" + id +
            ", year='" + year + "'" +
            ", month='" + month + "'" +
            ", totalAmount='" + totalAmount + "'" +
            '}';
    }
}
