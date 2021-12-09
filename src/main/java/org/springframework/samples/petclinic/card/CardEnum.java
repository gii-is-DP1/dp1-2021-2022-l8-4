package org.springframework.samples.petclinic.card;

import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.samples.petclinic.playercard.PlayerCardService;



public enum CardEnum{
    apartmentBuilding("Bloque de apartamentos") {

        @Autowired
        private PlayerService playerService;

        @Override
        public void effect(Player player){
            player.setVictoryPoints(player.getVictoryPoints() + 3);
            playerService.savePlayer(player);
        }

        
    },

    cornerStore("Bazar de la esquina") {
        @Autowired
        private PlayerService playerService;

        @Override
        public void effect(Player player){
            player.setVictoryPoints(player.getVictoryPoints() + 3);
            playerService.savePlayer(player);
            
        }
    };
    
    public abstract void effect(Player player);

    private final String name;
    
    private CardEnum(String name) {
        this.name=name;
    }

    public String getName(){
        return this.name;
    }

    
    
}
