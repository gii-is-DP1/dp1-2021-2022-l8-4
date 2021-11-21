package org.springframework.samples.petclinic.achievements;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
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
     public String  processCreationAchievement(@Valid Achievements achievement, BindingResult result, ModelMap modelMap) {
        if (result.hasErrors()) {
            modelMap.addAttribute("achievement", achievement);
            return ACHIEVEMENTS_UPDATE_FORM;
        } else {
            achievementsService.saveAchievement(achievement);
            modelMap.addAttribute("message", "Achievement succesfully saved");
            getAchievementsList(modelMap);
            return "redirect:/achievements";
        }
     }

     @GetMapping(value = "/{achievementId}/edit")
     public String initUpdateForm(@PathVariable("achievementId") int achievementId, ModelMap modelMap) {
         Optional<Achievements> achievement = this.achievementsService.findAchievementById(achievementId);
         modelMap.put("achievements", achievement.get());
         return ACHIEVEMENTS_UPDATE_FORM;
     }
 
     /**
      *
      * @param card
      * @param result
      * @param cardId
      * @param model
      * @return
      */
     @PostMapping(value = "/{achievementId}/edit")
     public String processUpdateForm(@Valid Achievements achievements, BindingResult result, @PathVariable("achievementId") int achievementId, 
     ModelMap modelMap) {
         if (result.hasErrors()) {
             modelMap.put("achievements", achievements);
             return ACHIEVEMENTS_UPDATE_FORM;
         }
         else {
            Optional<Achievements> achievementsToUpdate = this.achievementsService.findAchievementById(achievementId);
            BeanUtils.copyProperties(achievements, achievementsToUpdate.get(), "id");                                                                                                    
            this.achievementsService.saveAchievement(achievements);                    
            return "redirect:/achievements";
         }
     }

     @GetMapping(path="/delete/{achievementId}")
     public String deleteAchievement(@PathVariable("achievementId") int achievementId, ModelMap modelMap) {
         Optional<Achievements> achievement = achievementsService.findAchievementById(achievementId);
         if (achievement.isPresent()) {
            achievementsService.deleteAchievement(achievement.get());
            modelMap.addAttribute("message", "Achievement succesfully deleted");
         }
         else {
            modelMap.addAttribute("message", "Achievement not found");
         }
        return "redirect:/achievements";
     }

 }