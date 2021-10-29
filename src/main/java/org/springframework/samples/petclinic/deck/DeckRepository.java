package org.springframework.samples.petclinic.deck;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA CardRepository interface
 * 
 * @author Jose Maria Delgado Sanchez
 */
@Repository
public interface DeckRepository extends CrudRepository<Deck, Integer> {
    
}
