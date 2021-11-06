package org.springframework.samples.petclinic.achievements;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
/* @author Carlos Varela Soult
*/

 @Repository
 public interface AchievementsRepository extends CrudRepository<Achievements, Integer>{
     
 }