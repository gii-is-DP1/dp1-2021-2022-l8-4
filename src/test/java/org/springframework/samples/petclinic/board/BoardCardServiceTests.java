package org.springframework.samples.petclinic.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.samples.petclinic.card.CardType;
import org.springframework.samples.petclinic.game.GameService;
import org.springframework.samples.petclinic.gamecard.GameCardService;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
/**
 * @author Carlos Varela Soult
 */
public class BoardCardServiceTests {
    
    @Autowired
    private GameCardService boardCardService;

    @Autowired
    private GameService gameService;

    @Autowired
    private CardService cardService;

    /*@Test
    public void shouldFindBoardCardWithCorrectId() {
        Board board = boardService.findBoardById(1);
        Card card = cardService.findCardById(1);
        BoardCard boardCard = boardCardService.findByBoardCard(board, card);
        assertThat(boardCard.getBoard().getId()).isEqualTo(board.getId());
        assertThat(boardCard.getCard().getId()).isEqualTo(card.getId());
    }

    @Test
    public void shouldInsertBoardCard() {
        BoardCard boardCard = new BoardCard();
        boardCard.setSold(true);

        Card card = new Card();
        card.setName("Fábrica de huesos");
        card.setCost(7);
        card.setType(CardType.PERMANENTE);
        Board board = new Board();
        board.setTokyoCityStatus(LocationStatus.LIBRE);
        board.setTokyoBayStatus(LocationStatus.LIBRE);
        board.setGame(gameService.findGameById(1));

        boardCard.setCard(card);
        boardCard.setBoard(board);
        boardCardService.saveBoardCard(boardCard);
        assertThat(boardCard.getId()).isNotNull();
        assertThat(boardCard.getCard().getId()).isNotNull();
        assertThat(boardCard.getBoard().getId()).isNotNull();
        assertThat(boardCard.getCard().getName()).startsWith("Fábrica");
    }

    @Test
    public void shouldUpdateBoardCard() {
        Board oldBoard = boardService.findBoardById(1);
        Card oldCard = cardService.findCardById(1);
        BoardCard boardCard = boardCardService.findByBoardCard(oldBoard, oldCard);
        boardCard.setBoard(boardService.findBoardById(2));
        boardCard.setCard(cardService.findCardById(1));
        boardCard.setSold(true);
        assertThat(boardCard.getBoard()).isNotEqualTo(oldBoard);
        assertThat(boardCard.getCard()).isEqualTo(oldCard);
        assertTrue(boardCard.getSold());
    }

    @Test
    public void shouldFindAvailableCardsByBoard() throws Exception {
        Board board = boardService.findBoardById(1);
        Set<Card> availableCardsSet = boardCardService.findAvailableCardsByBoard(board);
        List<Card> availableCardsList = availableCardsSet.stream().collect(Collectors.toList());
        assertThat(availableCardsList.get(0).getId()).isNotNull();
        assertThat(availableCardsList.get(0).getName()).startsWith("Monstruo");
    }
*/
}
