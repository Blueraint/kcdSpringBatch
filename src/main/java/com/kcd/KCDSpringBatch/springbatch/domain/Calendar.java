package com.kcd.KCDSpringBatch.springbatch.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/*
    create table CALENDAR(
        V_YEAR VARCHAR(4),
        V_MONTH VARCHAR(2),
        V_DATE VARCHAR(2),
        IS_HOLIDAY BOOLEAN
    );

    ALTER TABLE CALENDAR ADD CONSTRAINT CALENDAR_IDX_1 UNIQUE(V_YEAR, V_MONTH, V_DATE);
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CALENDAR")
public class Calendar {
    @EmbeddedId
    private DateValue dateValue;

    @Column(name = "IS_HOLIDAY")
    private boolean isHoliday;

    @Override
    public String toString() {
        return "Calendar{" +
                "dateValue=" + dateValue.toString() +
                ", isHoliday=" + isHoliday +
                '}';
    }
}
