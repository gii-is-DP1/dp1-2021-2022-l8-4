package org.springframework.samples.petclinic.modules.statistics.metrics;

import org.springframework.samples.petclinic.user.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Metric {
    private User user;
    private Long score;

    public Metric(User user, Long score){
        this.user = user;
        this.score = score;
    }
}
