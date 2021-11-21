package org.springframework.samples.petclinic.boardcard;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.board.Board;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardCardRepository extends CrudRepository<BoardCard,Integer>{
    
    /**
	 * Retrieve all <code>PetType</code>s from the data store.
	 * @return a <code>Collection</code> of <code>Game</code>s
	 */
	@Query("SELECT bc FROM BoardCard bc WHERE bc.board = ?1 AND bc.card = ?2")
	BoardCard findByBoardCards(Board board, Card card) throws DataAccessException;
}
