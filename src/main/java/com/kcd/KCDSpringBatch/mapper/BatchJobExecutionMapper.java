package com.kcd.KCDSpringBatch.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.kcd.KCDSpringBatch.dto.BatchJobExecutionDto;
import com.kcd.KCDSpringBatch.dto.BatchParam;
import com.kcd.KCDSpringBatch.springbatch.domain.BatchJobExecution;
import com.kcd.KCDSpringBatch.springbatch.domain.BatchJobExecutionParams;
import com.kcd.KCDSpringBatch.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface BatchJobExecutionMapper {
    BatchJobExecutionMapper INSTANCE = Mappers.getMapper(BatchJobExecutionMapper.class);

    /*Job Dto Mapper*/
    @Mapping(source = "batchJobInstance.jobName", target = "jobName")
    @Mapping(source = "batchJobExecutionParamsList", target = "batchParam", qualifiedByName = "executionParamToBatchParam")
    BatchJobExecutionDto entityToDto(BatchJobExecution batchJobExecution);

    @Named("executionParamToBatchParam")
    static BatchParam executionParamToBatchParam(Set<BatchJobExecutionParams> batchJobExecutionParamsList) {
        Map<String, Object> batchParamDtoMap = batchJobExecutionParamsList.stream().collect(Collectors.toMap(BatchJobExecutionParams::getKeyName, p -> {
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
