package com.kcd.KCDSpringBatch.mapper;

import com.kcd.KCDSpringBatch.creditinfo.domain.Customer;
import com.kcd.KCDSpringBatch.dto.CustomerOutputDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
//        (componentModel = "spring")
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer outputDtoToEntity(CustomerOutputDto customerOutputDto);
    CustomerOutputDto entityToOutputDto(Customer customer);

}
