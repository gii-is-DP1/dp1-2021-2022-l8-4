package org.springframework.samples.petclinic.modules.statistics.metrics;

import org.springframework.samples.petclinic.user.User;

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
