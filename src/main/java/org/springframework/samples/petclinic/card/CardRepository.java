package org.springframework.samples.petclinic.card;

import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JPA CardRepository interface
 * 
 * @author Jose Maria Delgado Sanchez
 */
public interface CardRepository extends CrudRepository<Card, Integer> {

}
