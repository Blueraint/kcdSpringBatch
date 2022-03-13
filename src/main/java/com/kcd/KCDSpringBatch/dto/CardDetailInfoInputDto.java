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
public class CardDetailInfoInputDto {
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
    private String nameTypeSuite;
    private String nameIncomeType;
    private String nameEducationType;
    private String nameFamilyStatus;
    private String nameHousingType;
    private String regionPopulationRelative;
    private String daysBirth;
    private String daysEmployed;
    private String daysRegistration;
    private String daysIdPublish;
    private String ownCarAge;
    private String flagMobil;
    private String flagEmpPhone;
    private String flagWorkPhone;
    private String flagContMobile;
    private String flagPhone;
    private String flagEmail;
    private String occupationType;
    private String cntFamMembers;
    private String regionRatingClient;
    private String regionRatingClientWCity;
    private String weekdayApprProcessStart;
    private String hourApprProcessStart;
    private String regRegionNotLiveRegion;
    private String regRegionNotWorkRegion;
    private String liveRegionNotWorkRegion;
    private String regCityNotLiveCity;
    private String regCityNotWorkCity;
    private String liveCityNotWorkCity;
    private String organizationType;
}
