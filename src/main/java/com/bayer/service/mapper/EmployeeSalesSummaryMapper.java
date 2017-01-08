package com.bayer.service.mapper;

import com.bayer.domain.*;
import com.bayer.service.dto.EmployeeSalesSummaryDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity EmployeeSalesSummary and its DTO EmployeeSalesSummaryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EmployeeSalesSummaryMapper {

    @Mapping(source = "employee.id", target = "employeeId")
    EmployeeSalesSummaryDTO employeeSalesSummaryToEmployeeSalesSummaryDTO(EmployeeSalesSummary employeeSalesSummary);

    List<EmployeeSalesSummaryDTO> employeeSalesSummariesToEmployeeSalesSummaryDTOs(List<EmployeeSalesSummary> employeeSalesSummaries);

    @Mapping(target = "transactions", ignore = true)
    @Mapping(source = "employeeId", target = "employee")
    EmployeeSalesSummary employeeSalesSummaryDTOToEmployeeSalesSummary(EmployeeSalesSummaryDTO employeeSalesSummaryDTO);

    List<EmployeeSalesSummary> employeeSalesSummaryDTOsToEmployeeSalesSummaries(List<EmployeeSalesSummaryDTO> employeeSalesSummaryDTOs);

    default Employee employeeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
