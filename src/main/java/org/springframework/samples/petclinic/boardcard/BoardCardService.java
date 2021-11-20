package org.springframework.samples.petclinic.boardcard;

import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.board.Board;
import org.springframework.samples.petclinic.board.BoardService;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.stereotype.Service;

/**
 * @author Jose Maria Delgado Sanchez
 */
@Service
public class BoardCardService {
    @Autowired
    private BoardCardRepository boardCardRepository;

    @Autowired
    private BoardService boardService;

    @Transactional
    public Set<Card> findAvailableCardsByBoard(Board board){
        return boardService.findBoardById(board.getId()).getBoardCard()
                                            .stream()
                                            .filter(x -> x.getSold() == false)
                                            .map(x -> x.getCard())
                                            .collect(Collectors.toSet());
    }

    @Transactional
    public void saveBoardCard(BoardCard boardCard) throws DataAccessException{
        boardCardRepository.save(boardCard);
    }

    @Transactional
    public BoardCard findByBoardCard(Board board, Card card) throws DataAccessException {
        return boardCardRepository.findByBoardCards(board, card);
    }


}
