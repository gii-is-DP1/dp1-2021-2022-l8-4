package org.springframework.samples.petclinic.modules.statistics.metrics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
/* @author Jose Maria Delgado Sanchez
*/
@Controller
@RequestMapping("/metrics")
public class MetricControler {
    
    @Autowired
    private MetricService metricService;

    @GetMapping()
    public String getGamesPlayedMetric(ModelMap modelMap){
        String view = "modules/statistics/metrics/gamesPlayedMetric";
        List<Metric> rows = metricService.gamesPlayed();
        modelMap.addAttribute("rows", rows);
        return view;
    }
}
