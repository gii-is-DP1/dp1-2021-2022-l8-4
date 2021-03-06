package org.springframework.samples.kingoftokyo.card;

import org.springframework.samples.kingoftokyo.game.MapGameRepository;
import org.springframework.samples.kingoftokyo.player.Player;
import org.springframework.samples.kingoftokyo.player.PlayerService;

public interface UseCardsInterface {
    
    static final String damageString="damage";

    default void effect(Player player,PlayerService playerService, MapGameRepository mapGameRepository) {
    }
    default void effectStartTurn(Player player,PlayerService playerService, MapGameRepository mapGameRepository) {
    }
    default void effectEndTurn(Player player,PlayerService playerService, MapGameRepository mapGameRepository) {
    }
    default void effectInRoll(Player player,PlayerService playerService, MapGameRepository mapGameRepository) {
    }
    default void effectAfterRoll(Player player,PlayerService playerService, MapGameRepository mapGameRepository) {
    }
    default Integer effectDamage(Player player,PlayerService playerService,Integer damage, MapGameRepository mapGameRepository) {
        return damage;
    }
    default Integer effectBuy(Player player,PlayerService playerService,Integer energy,Integer cost, MapGameRepository mapGameRepository) {
        return energy;
    }

    
}
