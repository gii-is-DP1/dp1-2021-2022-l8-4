package org.springframework.samples.petclinic.game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA GameRepository interface
 * 
 * @author Jose Maria Delgado Sanchez
 */
@Repository
public interface GameRepository extends CrudRepository<Game, Integer>{
    
}
