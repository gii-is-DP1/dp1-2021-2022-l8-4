package org.springframework.samples.kingoftokyo.modules.statistics.achievement;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.kingoftokyo.modules.statistics.metrics.MetricType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
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

    private static final String VIEWS_ACHIEVEMENT_CREATE_OR_UPDATE_FORM = "modules/statistics/achievements/newAchievement";

    @GetMapping()
    public String getAchievementsList(ModelMap modelMap){
        String view = "modules/statistics/achievements/achievementsList";
        Iterable<Achievement> achievements = achievementService.findAll();
        modelMap.addAttribute("achievements", achievements);
        return view;
    }

    @GetMapping("/new")
    public String newAchievement(ModelMap modelMap){
        modelMap.addAttribute("achievement", new Achievement());
        modelMap.addAttribute("metrics", MetricType.values());
        return VIEWS_ACHIEVEMENT_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/new")
    public String createNewAchievement(@Valid Achievement newAchievement,BindingResult result, ModelMap modelMap){

        if (result.hasErrors()) {
            modelMap.addAttribute("achievement", newAchievement);
            modelMap.addAttribute("metrics", MetricType.values());
            return VIEWS_ACHIEVEMENT_CREATE_OR_UPDATE_FORM;

        } else {
            // creating card
            achievementService.saveAchievement(newAchievement);
            modelMap.addAttribute("message", "Achievement se ha guardado correctamente!");
        }
        return "redirect:/achievements/";
    }

}