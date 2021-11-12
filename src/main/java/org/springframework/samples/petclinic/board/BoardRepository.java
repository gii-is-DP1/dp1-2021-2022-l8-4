package org.springframework.samples.petclinic.board;



import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends CrudRepository<Board,Integer>{
    
}
