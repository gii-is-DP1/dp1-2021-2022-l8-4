package org.springframework.samples.kingoftokyo.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.kingoftokyo.user.User;
import org.springframework.stereotype.Service;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
/**
 *@author Rosa Molina
 */
public class GameTests {

    @Test
    public void testSaveCardIntoDatabaseAndGenerateId(){
        Game game = new Game();
        User user = new User();
        game.setCreator(user);
        LocalDateTime ldt = LocalDateTime.now();
        game.setEndTime(ldt);
        game.setStartTime(ldt);
        game.setName("juego");
        game.setWinner("user1");
        game.setTurn(0);
        game.setMaxNumberOfPlayers(6);
        
        assertEquals(game.getCreator(), user);
        assertEquals(game.getEndTime(), ldt);
        assertEquals(game.getStartTime(), ldt);
        assertEquals(game.getName(), "juego");
        assertEquals(game.getWinner(), "user1");
        assertEquals(game.getTurn(), 0);
        assertEquals(game.getMaxNumberOfPlayers(), 6);
    }

    
}
