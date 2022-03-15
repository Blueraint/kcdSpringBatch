package com.kcd.KCDSpringBatch.quartz.domain;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JobKeys implements Serializable {
    private String jobName;

    private String jobGroup;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobKeys jobKeys = (JobKeys) o;
        return Objects.equals(jobName, jobKeys.jobName) && Objects.equals(jobGroup, jobKeys.jobGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobName, jobGroup);
    }
}
