package org.springframework.samples.petclinic.boardcard;

import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.board.BoardService;
import org.springframework.samples.petclinic.card.Card;
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
    public Set<Card> findCardsOnSaleByBoardId(int boardId){
        return boardService.findById(boardId).getBoardCard()
                                            .stream()
                                            .filter(x -> x.getSold() == false)
                                            .map(x -> x.getCard())
                                            .collect(Collectors.toSet());
    }
}
