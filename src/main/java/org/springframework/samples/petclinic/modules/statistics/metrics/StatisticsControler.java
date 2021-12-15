package org.springframework.samples.petclinic.modules.statistics.metrics;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatisticsControler {
        @GetMapping("/statistics")
    public String rules(Map<String, Object> model) {	    
      return "modules/metrics/statistics";
    }
}