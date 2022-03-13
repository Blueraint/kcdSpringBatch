package com.kcd.KCDSpringBatch.springbatch.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BATCH_JOB_INSTANCE")
public class BatchJobInstance {
    @Id
    @Column(name = "JOB_INSTANCE_ID")
    private Long id;

    private Long version;

    private String jobName;

    private String jobKey;

    @OneToMany(mappedBy = "batchJobInstance", fetch = FetchType.LAZY)
    private List<BatchJobExecution> batchJobExecutions = new ArrayList<>();

    @Override
    public String toString() {
        return "BatchJobInstance{" +
                "id=" + id +
                ", version=" + version +
                ", jobName='" + jobName + '\'' +
                ", jobKey='" + jobKey + '\'' +
                '}';
    }
}
