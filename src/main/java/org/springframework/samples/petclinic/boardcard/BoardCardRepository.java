package org.springframework.samples.petclinic.boardcard;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardCardRepository extends CrudRepository<BoardCard,Integer>{
    
    /**
	 * Retrieve all <code>PetType</code>s from the data store.
	 * @return a <code>Collection</code> of <code>Game</code>s
	 */
	@Query("SELECT bc FROM BoardCard bc WHERE bc.board.id = ?1 AND bc.card.id = ?2")
	BoardCard findBoardCardsByIds(int boardId, int cardId) throws DataAccessException;
}
