package org.springframework.samples.petclinic.modules.statistics.metrics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.modules.statistics.achievement.Achievement;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String getStatistics(ModelMap modelMap){
        String view = "modules/statistics/metrics/metrics";
        MetricType metric = MetricType.gamesPlayed;
        List<MetricData> rows = metricService.statisticsByMetricType(metric);

        modelMap.addAttribute("metrics", MetricType.values());
        modelMap.addAttribute("metric", metric);
        modelMap.addAttribute("metricToDisplay", new Achievement());
        modelMap.addAttribute("rows", rows);
        return view;
    }

    @PostMapping
    public String requestStatistics(@ModelAttribute("metricToDisplay") Achievement metricToDisplay,ModelMap modelMap){
        String view = "modules/statistics/metrics/metrics";
        MetricType metric = metricToDisplay.getMetric();
        List<MetricData> rows = metricService.statisticsByMetricType(metric);

        modelMap.addAttribute("metrics", MetricType.values());
        modelMap.addAttribute("actualMetric", metric);
        modelMap.addAttribute("metricToDisplay", new Achievement());
        modelMap.addAttribute("rows", rows);
        return view;
    @GetMapping("/statistics")
    public String rules(Map<String, Object> model) {	    
      return "modules/metrics/statistics";
    }
}
