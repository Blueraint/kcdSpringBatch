package com.kcd.KCDSpringBatch.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CustomerOutputDto {
    private String customerNumber;
    private String codeGender;
    private Integer rating;
}
