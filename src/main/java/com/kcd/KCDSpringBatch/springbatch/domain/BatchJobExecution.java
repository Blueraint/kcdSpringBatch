package com.kcd.KCDSpringBatch.springbatch.domain;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.kcd.KCDSpringBatch.dto.BatchParam;
import com.kcd.KCDSpringBatch.util.DateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "BATCH_JOB_EXECUTION")
public class BatchJobExecution {
    @Id
    @Column(name = "JOB_EXECUTION_ID")
    private Long id;
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "JOB_INSTANCE_ID")
    private BatchJobInstance batchJobInstance;

    private LocalDateTime createTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String exitCode;
    private String exitMessage;
    private String lastUpdated;
    private String jobConfigurationLocation;

    public BatchJobExecution(Long id, Long version, BatchJobInstance batchJobInstance, LocalDateTime createTime, LocalDateTime startTime, LocalDateTime endTime, String status, String exitCode, String exitMessage, String lastUpdated, String jobConfigurationLocation) {
        this.id = id;
        this.version = version;
        this.batchJobInstance = batchJobInstance;
        this.createTime = createTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.exitCode = exitCode;
        this.exitMessage = exitMessage;
        this.lastUpdated = lastUpdated;
        this.jobConfigurationLocation = jobConfigurationLocation;
    }

    @Override
    public String toString() {
        return "BatchJobExecution{" +
                "id=" + id +
                ", version=" + version +
                ", batchJobInstance=" + batchJobInstance +
                ", createTime=" + createTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status='" + status + '\'' +
                ", exitCode='" + exitCode + '\'' +
                ", exitMessage='" + exitMessage + '\'' +
                ", lastUpdated='" + lastUpdated + '\'' +
                ", jobConfigurationLocation='" + jobConfigurationLocation + '\'' +
                '}';
    }

    /*
     Batch Parameter List
     Lazy connection 때문에 조회 시에는 Transactional 필수
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "batchJobExecutionParams",
            joinColumns = @JoinColumn(name = "JOB_EXECUTION_ID")
    )
    private final Set<BatchJobExecutionParams> batchJobExecutionParamsList = new HashSet<>();

    /*
    return BatchParamDto
     batchJobExecutionParamsList(Set) switch to BatchParamDto Object
     */
    public BatchParam getBatchParam() {

        Map<String, Object> batchParamDtoMap = getBatchJobExecutionParamsList().stream().collect(Collectors.toMap(BatchJobExecutionParams::getKeyName, p -> {
            switch (p.getTypeCd().toUpperCase()) {
                case "STRING":
                    return p.getStringVal();
                case "LONG":
                    return Long.valueOf(p.getLongVal());
                case "DATE":
                    return DateUtil.getLocalDateTime(p.getDateVal());
                case "DOUBLE":
                    return Double.valueOf(p.getDoubleVal());
                default:
                    throw new IllegalArgumentException("BATCH_JOB_EXECUTION_PARAMS are not list of string,long,date,double type.");
            }
            })
        );

        // use jackson.convertValue() or Gson library(serialize / deserialize)
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
        mapper.registerModule(module);
        mapper.setDateFormat(DateUtil.getDateFormat());

        return mapper.convertValue(batchParamDtoMap, BatchParam.class);
    }
}
