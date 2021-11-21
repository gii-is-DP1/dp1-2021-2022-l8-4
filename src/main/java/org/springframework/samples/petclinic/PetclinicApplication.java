package org.springframework.samples.petclinic;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.samples.petclinic.game.MapGameRepository;


@SpringBootApplication()
public class PetclinicApplication {

	
	
	public static void main(String[] args) {
		Set<Object> singletons = new HashSet<Object>();
		singletons.add(MapGameRepository.getInstance());
		SpringApplication.run(PetclinicApplication.class, args);
	}

}
