package com.kcd.KCDSpringBatch.mapper;

import com.kcd.KCDSpringBatch.creditinfo.domain.Customer;
import com.kcd.KCDSpringBatch.dto.CustomerOutputDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer outputDtoToEntity(CustomerOutputDto customerOutputDto);
    CustomerOutputDto entityToOutputDto(Customer customer);

}
