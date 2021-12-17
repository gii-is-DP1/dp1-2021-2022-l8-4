package org.springframework.samples.petclinic.card;

import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;

public interface UseCardsInterface {
    
    default void effect(Player player,PlayerService playerService) {
        int doNothing=0;
    }
    default void effectStartTurn(Player player,PlayerService playerService) {
        int doNothing=0;
    }
    default void effectInRoll(Player player,PlayerService playerService) {
        int doNothing=0;
    }
    default Integer effectDamage(Player player,PlayerService playerService,Integer damage) {
        return damage;
    }
}
