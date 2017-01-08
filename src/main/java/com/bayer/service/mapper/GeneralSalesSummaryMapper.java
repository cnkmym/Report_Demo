package com.bayer.service.mapper;

import com.bayer.domain.*;
import com.bayer.service.dto.GeneralSalesSummaryDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity GeneralSalesSummary and its DTO GeneralSalesSummaryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GeneralSalesSummaryMapper {

    GeneralSalesSummaryDTO generalSalesSummaryToGeneralSalesSummaryDTO(GeneralSalesSummary generalSalesSummary);

    List<GeneralSalesSummaryDTO> generalSalesSummariesToGeneralSalesSummaryDTOs(List<GeneralSalesSummary> generalSalesSummaries);

    @Mapping(target = "transactions", ignore = true)
    GeneralSalesSummary generalSalesSummaryDTOToGeneralSalesSummary(GeneralSalesSummaryDTO generalSalesSummaryDTO);

    List<GeneralSalesSummary> generalSalesSummaryDTOsToGeneralSalesSummaries(List<GeneralSalesSummaryDTO> generalSalesSummaryDTOs);
}
