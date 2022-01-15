package org.springframework.samples.kingoftokyo.playercard;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

/**
 * 
 * @author Jose Maria Delgado Sanchez
 */
@Repository
public interface PlayerCardRepository extends CrudRepository<PlayerCard, Integer> {

}
