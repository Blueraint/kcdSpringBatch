package com.kcd.KCDSpringBatch.config;

import com.kcd.KCDSpringBatch.springbatch.domain.Calendar;
import com.kcd.KCDSpringBatch.springbatch.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.quartz.Scheduler;
import org.quartz.impl.calendar.HolidayCalendar;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CalendarConfiguration {
    private final CalendarRepository calendarRepository;

    @Bean
    @DependsOn(value = "springbatchEntityManagerFactory")
    public HolidayCalendar holidayCalendar() {
        HolidayCalendar cal = new HolidayCalendar();

        calendarRepository.findByIsHoliday().forEach(calendar -> {
            cal.addExcludedDate(Date.valueOf(
                LocalDate.of(
                    Integer.valueOf(calendar.getDateValue().getYear()),
                        Month.of(Integer.valueOf(calendar.getDateValue().getMonth())),
                        Integer.valueOf(calendar.getDateValue().getDate())
                )
            ));
        });

        return cal;
    }
}