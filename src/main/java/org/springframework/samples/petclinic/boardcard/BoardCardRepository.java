package org.springframework.samples.petclinic.boardcard;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardCardRepository extends CrudRepository<BoardCard,Integer>{
    
}
