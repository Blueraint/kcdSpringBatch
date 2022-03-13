package com.kcd.KCDSpringBatch.springbatch.domain;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.Table;

/**
 * Embedded Type
 * Collection type -> re-definition Hashcode() and Equal() method(value check because these are not Entity)
 */

@Embeddable
@EqualsAndHashCode
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BATCH_JOB_EXECUTION_PARAMS")
public class BatchJobExecutionParams {

    // STRING, INTEGER, ....
    private String typeCd;

    private String keyName;

    private String stringVal;

    private String dateVal;

    private String longVal;

    private String doubleVal;

    private Character identifying;

}
