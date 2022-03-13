package com.kcd.KCDSpringBatch.springbatch.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
public class DateValue implements Serializable {
    @Column(name = "V_YEAR")
    private String year;

    @Column(name = "V_MONTH")
    private String month;

    @Column(name = "V_DATE")
    private String date;

    @Override
    public String toString() {
        return "DateValue{" +
                "year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
