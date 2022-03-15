package com.kcd.KCDSpringBatch.quartz.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateValue dateValue = (DateValue) o;
        return Objects.equals(year, dateValue.year) && Objects.equals(month, dateValue.month) && Objects.equals(date, dateValue.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, date);
    }
}
