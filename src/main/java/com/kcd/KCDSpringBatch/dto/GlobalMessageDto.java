package com.kcd.KCDSpringBatch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GlobalMessageDto {
    boolean status = true;
    Object message = "Api Called";
}
