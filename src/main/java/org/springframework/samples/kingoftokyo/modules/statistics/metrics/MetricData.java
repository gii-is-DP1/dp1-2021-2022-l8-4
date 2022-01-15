package org.springframework.samples.kingoftokyo.modules.statistics.metrics;

import org.springframework.samples.kingoftokyo.user.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetricData {
    private User user;
    private Long score;

    public MetricData(User user, Long score){
        this.user = user;
        this.score = score;
    }
}
