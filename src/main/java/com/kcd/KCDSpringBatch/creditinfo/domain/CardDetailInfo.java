package com.kcd.KCDSpringBatch.creditinfo.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
// test를 위해 setter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CARD_DETAIL_INFO")
public class CardDetailInfo {
    @Id @GeneratedValue
    @Column(name = "card_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private Integer target;

//    private String codeGender;

    //data Type
    private String nameContractType;

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
