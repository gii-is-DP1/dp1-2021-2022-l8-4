package org.springframework.samples.kingoftokyo.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.kingoftokyo.modules.statistics.metrics.MetricData;
import org.springframework.samples.kingoftokyo.user.User;
import org.springframework.samples.kingoftokyo.user.UserService;
import org.springframework.stereotype.Service;

/**
/* @author Rosa Molina
/* @author Carlos Varela Soult
*/

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class MetricsTest {

    @Autowired
    private UserService userService;

    
    @Test
    void testMetricCreate() {
        User user1 = userService.findUserById(1);        
        MetricData metric = new MetricData(user1, 10l);
        assertEquals(user1, metric.getUser());
        assertEquals(10l, metric.getScore());
    }

    @Test
    void testMetricUpdate() {
        User user1 = userService.findUserById(1);
        User user2 = userService.findUserById(2);
        MetricData metric = new MetricData(user1, 10l);
        metric.setScore(20l);
        metric.setUser(user2);
        assertEquals(user2, metric.getUser());
        assertEquals(20l, metric.getScore());
    }
 

}
