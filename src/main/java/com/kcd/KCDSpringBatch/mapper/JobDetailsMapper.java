package com.kcd.KCDSpringBatch.mapper;

import com.kcd.KCDSpringBatch.dto.JobDetailsDto;
import com.kcd.KCDSpringBatch.quartz.domain.JobDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobDetailsMapper {
    JobDetailsMapper INSTANCE = Mappers.getMapper(JobDetailsMapper.class);

    /*Quartz JobDetails Dto Mapper*/
    @Mapping(source = "triggers.nextFireTime", target = "nextFireTime", qualifiedByName = "transferStringToLocalDate")
    @Mapping(source = "triggers.prevFireTime", target = "prevFireTime", qualifiedByName = "transferStringToLocalDate")
    @Mapping(source = "triggers.triggerState", target = "triggerState")
    @Mapping(source = "triggers.triggerType", target = "triggerType")
    @Mapping(source = "triggers.startTime", target = "startTime", qualifiedByName = "transferStringToLocalDate")
    @Mapping(source = "triggers.endTime", target = "endTime", qualifiedByName = "transferStringToLocalDate")
    @Mapping(source = "triggers.calendarName", target = "setCalendar", qualifiedByName = "setCalendarFunction")
    @Mapping(source = "triggers.cronTriggers.cronExpression", target = "cronExpression")
    JobDetailsDto entityToDto(JobDetails jobDetails);

    /*
    Map transfer time UnixTime(quartz) to LocalDatetime
     */
    @Named("transferStringToLocalDate")
    static LocalDateTime transferStringToLocalDate(String timeStr) {
        long time = Long.parseLong(timeStr);

        return ("0".equals(timeStr))? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(time), TimeZone.getDefault().toZoneId());
    }

    @Named("setCalendarFunction")
    static boolean setCalendarFunction(String calendarName) {
        return calendarName != null;
    }
}
