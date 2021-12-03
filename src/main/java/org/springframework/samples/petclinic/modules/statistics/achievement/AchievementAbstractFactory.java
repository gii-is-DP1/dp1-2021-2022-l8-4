package org.springframework.samples.petclinic.modules.statistics.achievement;

import org.springframework.samples.petclinic.modules.statistics.metrics.MetricType;

public interface AchievementAbstractFactory<T> {
    T create(MetricType metric);
}
