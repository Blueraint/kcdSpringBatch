package com.kcd.KCDSpringBatch;

import com.kcd.KCDSpringBatch.creditinfo.domain.CardDetailInfo;
import com.kcd.KCDSpringBatch.creditinfo.domain.Customer;
import com.kcd.KCDSpringBatch.dto.CardDetailInfoInputDto;
import com.kcd.KCDSpringBatch.dto.CardDetailInfoOutputDto;
import com.kcd.KCDSpringBatch.mapper.CardDetailInfoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MapperTest {

    @Test
    public void mapperTest(){

        CardDetailInfoInputDto cardDetailInfoInputDto = new CardDetailInfoInputDto();
        cardDetailInfoInputDto.setCustomerNumber("9999999999999");
        cardDetailInfoInputDto.setAmtCredit("223");
        cardDetailInfoInputDto.setAmtGoodsPrice("111");
        cardDetailInfoInputDto.setCodeGender("M");

        Customer customer = new Customer();
        customer.setCustomerNumber(cardDetailInfoInputDto.getCustomerNumber());
        customer.setCodeGender(cardDetailInfoInputDto.getCodeGender());

        // input -> entity
        CardDetailInfo cardDetailInfo = CardDetailInfoMapper.INSTANCE.cardDetailInfoInputDtoToEntity(cardDetailInfoInputDto, customer);

        // entity -> output
        CardDetailInfoOutputDto cardDetailInfoOutputDto = CardDetailInfoMapper.INSTANCE.cardDetailInfoEntityToOutputDto(cardDetailInfo);

        // output -> entity
        CardDetailInfo cardDetailInfoReverse = CardDetailInfoMapper.INSTANCE.cardDetailInfoOutputDtoToEntity(cardDetailInfoOutputDto, customer);

        //entity -> input
        CardDetailInfoInputDto cardDetailInfoInputDtoReverse = CardDetailInfoMapper.INSTANCE.cardDetailInfoEntityToInputDto(cardDetailInfo);

        System.out.println("(1) InputDto -> Entity : " + cardDetailInfo.toString());
        System.out.println("(1) InputDto -> Entity : " + cardDetailInfo.getCustomer().toString());
        System.out.println("**************************************************************************************************************");
        System.out.println("(2) Entity -> outputDto : " + cardDetailInfoOutputDto.toString());
        System.out.println("**************************************************************************************************************");
        System.out.println("(3) outputDto -> Entity : " + cardDetailInfoReverse.toString());
        System.out.println("**************************************************************************************************************");
        System.out.println("(4) Entity -> InputDto : " + cardDetailInfoInputDtoReverse.toString());
        System.out.println("**************************************************************************************************************");
    }

}
