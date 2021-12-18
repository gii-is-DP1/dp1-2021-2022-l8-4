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
@RequestMapping("/statistics")
public class MetricControler {
    
    @Autowired
    private MetricService metricService;

    @GetMapping(path = "/ranking")
    public String getRanking(@RequestParam(value = "metric", defaultValue = "gamesPlayed") MetricType metric, ModelMap modelMap){
        String view = "modules/statistics/metrics/ranking";
        List<MetricData> rows = metricService.statisticsByMetricType(metric);

        modelMap.addAttribute("metrics", MetricType.values());
        modelMap.addAttribute("actualMetric", metric);
        modelMap.addAttribute("rows", rows);
        return view;
    }

    
    @GetMapping()
    public String rules(Map<String, Object> model) {	    
      return "modules/statistics/metrics/statistics";
    }
}
