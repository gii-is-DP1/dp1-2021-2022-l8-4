package org.springframework.samples.petclinic.achievement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
/* @author Carlos Varela Soult
*/

 @Repository
 public interface AchievementRepository extends CrudRepository<Achievement, Integer>{
     
 }