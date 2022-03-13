package com.kcd.KCDSpringBatch.quartz.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "QRTZ_JOB_DETAILS")
@Getter
@Setter
@NoArgsConstructor
public class JobDetails {
    @EmbeddedId
    private JobKeys jobKeys;

    private String schedName;

    private String description;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "jobName", referencedColumnName = "jobName", updatable=false, insertable=false),
            @JoinColumn(name = "jobGroup", referencedColumnName = "jobGroup", updatable=false, insertable=false)
    })
    private Triggers triggers;

    @Override
    public String toString() {
        return "JobDetails{" +
                "jobKeys= " + jobKeys.toString() +
                ", schedName='" + schedName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
