package com.kcd.KCDSpringBatch;

import com.kcd.KCDSpringBatch.springbatch.repository.CalendarRepository;
import org.junit.jupiter.api.Test;
import org.quartz.impl.calendar.HolidayCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class calendarTest {
    @Autowired
    private CalendarRepository calendarRepository;
    @Autowired
    private HolidayCalendar holidayCalendar;

    @Test
    public void findCalendarTest() {
        calendarRepository.findByIsHoliday().forEach(calendar -> System.out.println("## Calendar List : " + calendar.toString()));

//        System.out.println(holidayCalendar.getBaseCalendar().getDescription());
    }

}
