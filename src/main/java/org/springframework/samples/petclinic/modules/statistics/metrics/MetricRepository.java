package org.springframework.samples.petclinic.modules.statistics.metrics;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.game.Game;

public interface MetricRepository extends CrudRepository<Game, Integer> {
    

}
