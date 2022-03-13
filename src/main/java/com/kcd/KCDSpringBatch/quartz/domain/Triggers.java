package com.kcd.KCDSpringBatch.quartz.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "QRTZ_TRIGGERS")
@Getter
@Setter
@NoArgsConstructor
public class Triggers {
    @EmbeddedId
    private JobKeys jobKeys;

    private String schedName;

    private TriggerKeys triggerKeys;

    private String description;

    private String nextFireTime;

    private String prevFireTime;

    private Integer priority;

    private String triggerState;

    private String triggerType;

    private String startTime;

    private String endTime;

    private String calendarName;

    private String misfireInstr;

    private String jobData;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "triggerName", referencedColumnName = "triggerName", updatable=false, insertable=false),
            @JoinColumn(name = "triggerGroup", referencedColumnName = "triggerGroup", updatable=false, insertable=false)
    })
    private CronTriggers cronTriggers;

    @OneToOne(mappedBy = "triggers")
    private JobDetails quartzJobDetails;
}
