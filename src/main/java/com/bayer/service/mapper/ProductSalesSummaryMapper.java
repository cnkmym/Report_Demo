package com.bayer.service.mapper;

import com.bayer.domain.*;
import com.bayer.service.dto.ProductSalesSummaryDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ProductSalesSummary and its DTO ProductSalesSummaryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProductSalesSummaryMapper {

    @Mapping(source = "product.id", target = "productId")
    ProductSalesSummaryDTO productSalesSummaryToProductSalesSummaryDTO(ProductSalesSummary productSalesSummary);

    List<ProductSalesSummaryDTO> productSalesSummariesToProductSalesSummaryDTOs(List<ProductSalesSummary> productSalesSummaries);

    @Mapping(target = "transactions", ignore = true)
    @Mapping(source = "productId", target = "product")
    ProductSalesSummary productSalesSummaryDTOToProductSalesSummary(ProductSalesSummaryDTO productSalesSummaryDTO);

    List<ProductSalesSummary> productSalesSummaryDTOsToProductSalesSummaries(List<ProductSalesSummaryDTO> productSalesSummaryDTOs);

    default Product productFromId(Long id) {
        if (id == null) {
            return null;
        }
        Product product = new Product();
        product.setId(id);
        return product;
    }
}
