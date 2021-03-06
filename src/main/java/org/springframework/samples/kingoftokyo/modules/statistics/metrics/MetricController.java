package org.springframework.samples.kingoftokyo.modules.statistics.metrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.samples.kingoftokyo.modules.statistics.achievement.AchievementService;
import org.springframework.samples.kingoftokyo.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


/**
/* @author Jose Maria Delgado Sanchez
*  @author Rosa Molina
*  @author Sara Cruz
*/
@Controller
@RequestMapping("/statistics")
public class MetricController {
  
    @Autowired
    private MetricService metricService;
    @Autowired
    private UserService userService;
    @Autowired
    private AchievementService achievementService;

    @GetMapping(path = "/ranking")
    public String getRanking(@RequestParam(value = "metric", defaultValue = "gamesPlayed") MetricType metric,@RequestParam(value = "page", defaultValue = "1") int page, ModelMap modelMap){
        String view = "modules/statistics/metrics/ranking";
        Page<MetricData> pages = metricService.rankingByMetricType(metric,page-1,10);

        modelMap.addAttribute("metrics", MetricType.values());
        modelMap.addAttribute("actualMetric", metric);

        modelMap.addAttribute("totalPages", pages.getTotalPages());
        modelMap.addAttribute("totalElements", pages.getTotalElements());
        modelMap.addAttribute("number", pages.getNumber());
        modelMap.addAttribute("rows", pages.getContent());
        modelMap.addAttribute("size", pages.getContent().size());
        
        return view;
    }

    
    @GetMapping()
    public String getStatistics(Map<String, Object> model, ModelMap modelMap) {
      achievementService.checkAchievements(userService.authenticatedUser());
      modelMap.addAttribute("totalGames", metricService.findTotalGamesApp());
      modelMap.addAttribute("mediumGameTime", metricService.findTimeGames());
      modelMap.addAttribute("modaMonstername", metricService.findMonsterModa().getName());
      modelMap.addAttribute("modaMonstericon", metricService.findMonsterModa().getIcon());
      modelMap.addAttribute("nomodaMonstername", metricService.findMonsterNoModa().getName());
      modelMap.addAttribute("nomodaMonstericon", metricService.findMonsterNoModa().getIcon());
      modelMap.addAttribute("listWinsRanking", metricService.winsRankingStatistic());
      modelMap.addAttribute("listScoresRanking", metricService.scoresRankingStatistic());
      modelMap.addAttribute("winsCurrent", metricService.findTotalWinsGamesCurrentUser(userService.authenticatedUser().getUsername()));
      modelMap.addAttribute("gamesCurrent", metricService.findTotalGamesCurrentUser(userService.authenticatedUser()));
      modelMap.addAttribute("gamesTimeCurrent", metricService.findTimeGamesforUser(userService.authenticatedUser()));
      modelMap.addAttribute("listUsersTurns", metricService.findTurnsTokyo());

      return "modules/statistics/metrics/statistics";
    }
}
