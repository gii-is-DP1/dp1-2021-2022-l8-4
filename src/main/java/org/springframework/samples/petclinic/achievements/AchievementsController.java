package org.springframework.samples.petclinic.achievements;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
/* @author Carlos Varela Soult
*/

 @Controller
 @RequestMapping("/achievements")
 public class AchievementsController {
     @Autowired
     private AchievementsService achievementsService;

     private static final String ACHIEVEMENTS_UPDATE_FORM = "achievements/updateAchievement";

     @GetMapping()
     public String getAchievementsList(ModelMap modelMap) {
         String view = "achievements/achievementsList";
         Iterable<Achievements> achievements = achievementsService.findAll(); 
         modelMap.addAttribute("achievements", achievements);
         return view;
     }

     @GetMapping(path="/new")
     public String createAchievement(ModelMap modelMap) {
         String view = ACHIEVEMENTS_UPDATE_FORM;
         modelMap.addAttribute("achievement", new Achievements());
         return view;
     }

     @PostMapping(path="/save")
     public String postAchievement(@Valid Achievements achievement, BindingResult result, ModelMap modelMap) {
        String view = "achievements/achievementsList";
        if (result.hasErrors()) {
            modelMap.addAttribute("achievement", achievement);
            return ACHIEVEMENTS_UPDATE_FORM;
        } else {
            achievementsService.saveAchievement(achievement);
            modelMap.addAttribute("message", "Achievement succesfully saved");
            view = getAchievementsList(modelMap);
        }
        return view;
     }

     @GetMapping(path="/delete/{achievementId}")
     public String deleteAchievement(@PathVariable("achievementId") int achievementId, ModelMap modelMap) {
         String view = "achievements/achievementsList";
         Optional<Achievements> achievement = achievementsService.findAchievementById(achievementId);
         if (achievement.isPresent()) {
            achievementsService.deleteAchievement(achievement.get());
            modelMap.addAttribute("message", "Achievement succesfully deleted");
         }
         else {
            modelMap.addAttribute("message", "Achievement not found");
            view = getAchievementsList(modelMap);
         }
         return view;
     }

 }