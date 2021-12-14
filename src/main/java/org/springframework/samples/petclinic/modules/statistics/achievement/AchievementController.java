package org.springframework.samples.petclinic.modules.statistics.achievement;

import javax.enterprise.inject.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.modules.statistics.metrics.MetricType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
/* @author Jose Maria Delgado Sanchez
*/

 @Controller
 @RequestMapping("/achievements")
 public class AchievementController {

    @Autowired
    private AchievementService achievementService;

    @GetMapping()
    public String getAchievementsList(ModelMap modelMap){
        String view = "modules/statistics/achievements/achievementsList";
        Iterable<Achievement> achievements = achievementService.findAll();
        modelMap.addAttribute("achievements", achievements);
        return view;
    }

    @GetMapping("/new")
    public String newAchievement(ModelMap modelMap){
        String view = "modules/statistics/achievements/newAchievement";
        modelMap.addAttribute("achievement", new Achievement());
        modelMap.addAttribute("metrics", MetricType.values());
        return view;
    }

    @PostMapping("/new")
    public String createNewAchievement(@ModelAttribute("newAchievement") Achievement newAchievement,ModelMap modelMap){
        achievementService.saveAchievement(newAchievement);
        return "redirect:/achievements/";
    }

}