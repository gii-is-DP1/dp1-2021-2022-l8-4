package org.springframework.samples.petclinic.board;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))

/**
 * @author Carlos Varela Soult
 */
public class BoardServiceTests {
    /*
    @Autowired
    private BoardService boardService;

    @Autowired
    private UserService userService;
    
    @Test
    public void shouldFindAllBoards() {
        Iterable<Board> all = boardService.findAll();
        for (Board board:all) {
            LocationStatus tokyoCityStatus = board.getTokyoCityStatus();
            LocationStatus tokyoBayStatus = board.getTokyoBayStatus();
            assertThat(tokyoCityStatus).isEqualTo(LocationStatus.OCUPADO);
            assertThat(tokyoBayStatus).isEqualTo(LocationStatus.LIBRE);
        }
    }

    @Test
    public void shouldFindBoardWithCorrectId() {
        Board board = boardService.findBoardById(2);
        assertThat(board.getTokyoCityStatus()).isEqualTo(LocationStatus.OCUPADO);
        assertThat(board.getTokyoBayStatus()).isEqualTo(LocationStatus.LIBRE);
    }

    @Test
    public void shouldInsertBoard() {
        Board board = new Board();
        board.setTokyoCityStatus(LocationStatus.LIBRE);
        board.setTokyoBayStatus(LocationStatus.LIBRE);

        User user = userService.findUserByUsername("fire");
        Game game = new Game();
        game.setId(5);
        game.setCreator(user);
        game.setName("La partida del siglo");
        game.setStartTime(LocalDateTime.now());
        game.setEndTime(LocalDateTime.parse("2040-11-01T10:00:00"));
        game.setMaxNumberOfPlayers(6);
        game.setTurn(1);
        game.setWinner(null);
        
        board.setGame(game);
        boardService.saveBoard(board);
        assertThat(board.getId()).isNotNull();
        assertThat(board.getGame().getId()).isNotNull();
        assertThat(board.getGame().getCreator().getUsername().toUpperCase()).startsWith("FIRE");
        assertThat(board.getGame().getName()).startsWith("La partida");
    }

    @Test
    public void shouldUpdateBoard() {
        Board board = boardService.findBoardById(2);
        LocationStatus oldTokyoCityStatus = board.getTokyoCityStatus();
        LocationStatus oldTokyoBayStatus = board.getTokyoBayStatus();
        board.setTokyoCityStatus(LocationStatus.BLOQUEADO);
        board.setTokyoBayStatus(LocationStatus.LIBRE);
        boardService.saveBoard(board);

        board = boardService.findBoardById(2);
        assertThat(board.getTokyoCityStatus()).isNotEqualTo(oldTokyoCityStatus);
        assertThat(board.getTokyoBayStatus()).isEqualTo(oldTokyoBayStatus);
    }

*/
}
