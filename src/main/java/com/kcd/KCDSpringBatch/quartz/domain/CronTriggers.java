package com.kcd.KCDSpringBatch.quartz.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "QRTZ_CRON_TRIGGERS")
@Getter
@Setter
@NoArgsConstructor
public class CronTriggers {
    @EmbeddedId
    private TriggerKeys triggerKeys;

    private String schedName;

    private String cronExpression;

    private String timeZoneId;

    @OneToOne(mappedBy = "cronTriggers")
    private Triggers triggers;
}
