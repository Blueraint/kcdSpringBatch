package com.kcd.KCDSpringBatch.quartz.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@ToString
public class TriggerKeys implements Serializable {
    private String triggerName;

    private String triggerGroup;
}
