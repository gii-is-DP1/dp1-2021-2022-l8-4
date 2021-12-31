package org.springframework.samples.petclinic.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.geo.Metrics;
import org.springframework.samples.petclinic.modules.statistics.metrics.MetricData;
import org.springframework.samples.petclinic.modules.statistics.metrics.MetricService;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.stereotype.Service;

import static org.assertj.core.api.Assertions.assertThat;



import javassist.bytecode.Opcode;

/**
/* @author Rosa Molina
/* @author Carlos Varela Soult
*/

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class MetricsTest {
    @Autowired
    private MetricService metricService;
    @Autowired
    private UserService userService;

    
    @Test
    public void testMetricCreate() {
        User user1 = userService.findUserById(1).get();        
        MetricData metric = new MetricData(user1, 10l);
        assertEquals(user1, metric.getUser());
        assertEquals(10l, metric.getScore());
    }

    @Test
    public void testMetricUpdate() {
        User user1 = userService.findUserById(1).get();
        User user2 = userService.findUserById(2).get();
        MetricData metric = new MetricData(user1, 10l);
        metric.setScore(20l);
        metric.setUser(user2);
        assertEquals(user2, metric.getUser());
        assertEquals(20l, metric.getScore());
    }
 

}