package org.springframework.samples.petclinic.player;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Ricardo Nadal Garcia
 */

@Repository
public interface PlayerRepository extends CrudRepository<Player, Integer> {

}
