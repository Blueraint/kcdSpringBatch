package com.kcd.KCDSpringBatch.dto;

import lombok.*;


/**
 * DTO for Input Item Object (Txt, Csv, Json,...)
 * Direct matched by database column
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CardDetailInfoOutputDto {
        private String customerNumber;
        private Integer target;
        private String nameContractType;
        private String codeGender;
        private String flagOwnCar;
        private String flagOwnRealty;
        private String cntChildren;
        private String amtIncomeTotal;
        private String amtCredit;
        private String amtAnnuity;
        private String amtGoodsPrice;
}
