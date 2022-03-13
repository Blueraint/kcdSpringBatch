package com.kcd.KCDSpringBatch.springbatch.repository;

import com.kcd.KCDSpringBatch.springbatch.domain.BatchJobExecution;
import com.kcd.KCDSpringBatch.springbatch.domain.Calendar;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CalendarRepository extends ReadOnlyRepository<BatchJobExecution, Long> {
    @Query("select c from Calendar c where c.isHoliday = true")
    List<Calendar> findByIsHoliday();
}
