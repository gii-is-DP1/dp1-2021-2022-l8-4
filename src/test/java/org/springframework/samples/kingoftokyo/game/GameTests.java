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
        
        assertEquals(user, game.getCreator());
        assertEquals(ldt, game.getEndTime());
        assertEquals(ldt, game.getStartTime());
        assertEquals("juego", game.getName());
        assertEquals("user1", game.getWinner());
        assertEquals(0, game.getTurn());
        assertEquals(6, game.getMaxNumberOfPlayers());
    }

    
}
