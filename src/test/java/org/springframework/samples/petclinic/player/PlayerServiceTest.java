package org.springframework.samples.petclinic.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))

public class PlayerServiceTest {
    
    @Autowired
    private PlayerService playerService;

    @Test
    public void testCountWithInitialData(){
        int count = playerService.playerCount();
       assertEquals(count,4);
    }
    @Test
    public void testFindPlayerWithCorrectId(){
        Player player4 = playerService.findPlayerById(4);
        assertThat(player4.getMonsterName().toString()).startsWith("CyberBunny");
		assertThat(player4.getLifePoints()).isEqualTo(4);
        assertThat(player4.getVictoryPoints()).isEqualTo(8);
        assertThat(player4.getEnergyPoints()).isEqualTo(2);
        assertThat(player4.getLocation().toString()).startsWith("bahiaTokyo");
        assertThat(player4.getGame().getId()).isEqualTo(1);
    }


}
