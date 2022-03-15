package com.kcd.KCDSpringBatch.quartz.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@ToString
public class TriggerKeys implements Serializable {
    private String triggerName;

    private String triggerGroup;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TriggerKeys that = (TriggerKeys) o;
        return Objects.equals(triggerName, that.triggerName) && Objects.equals(triggerGroup, that.triggerGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(triggerName, triggerGroup);
    }
}
