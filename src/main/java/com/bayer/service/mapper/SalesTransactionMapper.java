package com.bayer.service.mapper;

import com.bayer.domain.*;
import com.bayer.service.dto.SalesTransactionDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity SalesTransaction and its DTO SalesTransactionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SalesTransactionMapper {

    @Mapping(source = "productSalesSummary.id", target = "productSalesSummaryId")
    @Mapping(source = "employeeSalesSummary.id", target = "employeeSalesSummaryId")
    @Mapping(source = "generalSalesSummary.id", target = "generalSalesSummaryId")
    @Mapping(source = "empolyee.id", target = "empolyeeId")
    @Mapping(source = "product.id", target = "productId")
    SalesTransactionDTO salesTransactionToSalesTransactionDTO(SalesTransaction salesTransaction);

    List<SalesTransactionDTO> salesTransactionsToSalesTransactionDTOs(List<SalesTransaction> salesTransactions);

    @Mapping(source = "productSalesSummaryId", target = "productSalesSummary")
    @Mapping(source = "employeeSalesSummaryId", target = "employeeSalesSummary")
    @Mapping(source = "generalSalesSummaryId", target = "generalSalesSummary")
    @Mapping(source = "empolyeeId", target = "empolyee")
    @Mapping(source = "productId", target = "product")
    SalesTransaction salesTransactionDTOToSalesTransaction(SalesTransactionDTO salesTransactionDTO);

    List<SalesTransaction> salesTransactionDTOsToSalesTransactions(List<SalesTransactionDTO> salesTransactionDTOs);

    default ProductSalesSummary productSalesSummaryFromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductSalesSummary productSalesSummary = new ProductSalesSummary();
        productSalesSummary.setId(id);
        return productSalesSummary;
    }

    default EmployeeSalesSummary employeeSalesSummaryFromId(Long id) {
        if (id == null) {
            return null;
        }
        EmployeeSalesSummary employeeSalesSummary = new EmployeeSalesSummary();
        employeeSalesSummary.setId(id);
        return employeeSalesSummary;
    }

    default GeneralSalesSummary generalSalesSummaryFromId(Long id) {
        if (id == null) {
            return null;
        }
        GeneralSalesSummary generalSalesSummary = new GeneralSalesSummary();
        generalSalesSummary.setId(id);
        return generalSalesSummary;
    }

    default Employee employeeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }

    default Product productFromId(Long id) {
        if (id == null) {
            return null;
        }
        Product product = new Product();
        product.setId(id);
        return product;
    }
}
