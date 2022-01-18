package org.springframework.samples.kingoftokyo.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.samples.kingoftokyo.model.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {
	

	  @GetMapping({"/","/welcome"})
	  public String welcome(Map<String, Object> model) {	    
		List<Person> persons = new ArrayList<>();
		Person rick = new Person();
		rick.setFirstName("Ricardo");
		rick.setLastName(" Nadal");
		Person carlos = new Person();
		carlos.setFirstName("Carlos");
		carlos.setLastName(" Varela");
		Person fire = new Person();
		fire.setFirstName("Jose María");
		fire.setLastName(" Delgado");
		Person noelia = new Person();
		noelia.setFirstName("Noelia");
		noelia.setLastName(" López");
		Person sara = new Person();
		sara.setFirstName("Sara");
		sara.setLastName(" Cruz");
		Person rosa = new Person();
		rosa.setFirstName("Rosa");
		rosa.setLastName(" Molina");
		persons.add(rick);
		persons.add(carlos);
		persons.add(fire);
		persons.add(noelia);
		persons.add(sara);
		persons.add(rosa);
		model.put("persons", persons);
		model.put("title", "DP1 King of Tokyo");
		model.put("group", "3 l8-4");
	    return "welcome";
	  }

}
