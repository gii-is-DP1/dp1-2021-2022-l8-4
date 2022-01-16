package org.springframework.samples.kingoftokyo.web;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RulesController {
    
    @GetMapping("/rules")
    public String rules(Map<String, Object> model) {	    
      return "rules";
    }
}
