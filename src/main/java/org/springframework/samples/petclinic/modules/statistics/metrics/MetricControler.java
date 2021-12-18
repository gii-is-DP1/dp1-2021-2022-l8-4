package org.springframework.samples.petclinic.modules.statistics.metrics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


/**
/* @author Jose Maria Delgado Sanchez
*/
@Controller
@RequestMapping("/metrics")
public class MetricControler {
    
    @Autowired
    private MetricService metricService;

    @GetMapping()
    public String getRanking(@RequestParam(value = "metric", defaultValue = "gamesPlayed") MetricType metric, ModelMap modelMap){
        String view = "modules/statistics/metrics/metrics";
        List<MetricData> rows = metricService.statisticsByMetricType(metric);

        modelMap.addAttribute("metrics", MetricType.values());
        modelMap.addAttribute("actualMetric", metric);
        modelMap.addAttribute("rows", rows);
        return view;
    }

    
    @GetMapping("/statistics")
    public String rules(Map<String, Object> model) {	    
      return "modules/metrics/statistics";
    }
}
