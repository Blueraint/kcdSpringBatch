package com.kcd.KCDSpringBatch.quartz.domain;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JobKeys implements Serializable {
    private String jobName;

    private String jobGroup;
}
