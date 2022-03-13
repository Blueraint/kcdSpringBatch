package com.kcd.KCDSpringBatch.mapper;

import com.kcd.KCDSpringBatch.creditinfo.domain.CardDetailInfo;
import com.kcd.KCDSpringBatch.creditinfo.domain.Customer;
import com.kcd.KCDSpringBatch.dto.CardDetailInfoInputDto;
import com.kcd.KCDSpringBatch.dto.CardDetailInfoOutputDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
//        (componentModel = "spring")
public interface CardDetailInfoMapper {
    CardDetailInfoMapper INSTANCE = Mappers.getMapper(CardDetailInfoMapper.class);

    /*InputDto Mapper*/
    @Mapping(source = "customerInput", target = "customer")
    CardDetailInfo cardDetailInfoInputDtoToEntity(CardDetailInfoInputDto cardDetailInfoInputDto, Customer customerInput);


    @Mapping(source = "customer.customerNumber", target = "customerNumber")
    @Mapping(source = "customer.codeGender", target = "codeGender")
    CardDetailInfoInputDto cardDetailInfoEntityToInputDto(CardDetailInfo carDetailInfo);

    /*OutputDto Mapper*/
    @Mapping(source = "customerInput", target = "customer")
    CardDetailInfo cardDetailInfoOutputDtoToEntity(CardDetailInfoOutputDto cardDetailInfoOutputDto, Customer customerInput);

    @Mapping(source = "customer.customerNumber", target = "customerNumber")
    @Mapping(source = "customer.codeGender", target = "codeGender")
    CardDetailInfoOutputDto cardDetailInfoEntityToOutputDto(CardDetailInfo carDetailInfo);
}
