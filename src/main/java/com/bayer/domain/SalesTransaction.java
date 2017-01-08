package com.bayer.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A SalesTransaction.
 */
@Entity
@Table(name = "sales_transaction")
public class SalesTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "transaction_date")
    private ZonedDateTime transactionDate;

    @Column(name = "transaction_amount", precision=10, scale=2)
    private BigDecimal transactionAmount;

    @ManyToOne
    private ProductSalesSummary productSalesSummary;

    @ManyToOne
    private EmployeeSalesSummary employeeSalesSummary;

    @ManyToOne
    private GeneralSalesSummary generalSalesSummary;

    @ManyToOne
    private Employee empolyee;

    @ManyToOne
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getTransactionDate() {
        return transactionDate;
    }

    public SalesTransaction transactionDate(ZonedDateTime transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public void setTransactionDate(ZonedDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public SalesTransaction transactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
        return this;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public ProductSalesSummary getProductSalesSummary() {
        return productSalesSummary;
    }

    public SalesTransaction productSalesSummary(ProductSalesSummary productSalesSummary) {
        this.productSalesSummary = productSalesSummary;
        return this;
    }

    public void setProductSalesSummary(ProductSalesSummary productSalesSummary) {
        this.productSalesSummary = productSalesSummary;
    }

    public EmployeeSalesSummary getEmployeeSalesSummary() {
        return employeeSalesSummary;
    }

    public SalesTransaction employeeSalesSummary(EmployeeSalesSummary employeeSalesSummary) {
        this.employeeSalesSummary = employeeSalesSummary;
        return this;
    }

    public void setEmployeeSalesSummary(EmployeeSalesSummary employeeSalesSummary) {
        this.employeeSalesSummary = employeeSalesSummary;
    }

    public GeneralSalesSummary getGeneralSalesSummary() {
        return generalSalesSummary;
    }

    public SalesTransaction generalSalesSummary(GeneralSalesSummary generalSalesSummary) {
        this.generalSalesSummary = generalSalesSummary;
        return this;
    }

    public void setGeneralSalesSummary(GeneralSalesSummary generalSalesSummary) {
        this.generalSalesSummary = generalSalesSummary;
    }

    public Employee getEmpolyee() {
        return empolyee;
    }

    public SalesTransaction empolyee(Employee employee) {
        this.empolyee = employee;
        return this;
    }

    public void setEmpolyee(Employee employee) {
        this.empolyee = employee;
    }

    public Product getProduct() {
        return product;
    }

    public SalesTransaction product(Product product) {
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
        SalesTransaction salesTransaction = (SalesTransaction) o;
        if (salesTransaction.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, salesTransaction.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SalesTransaction{" +
            "id=" + id +
            ", transactionDate='" + transactionDate + "'" +
            ", transactionAmount='" + transactionAmount + "'" +
            '}';
    }
}
