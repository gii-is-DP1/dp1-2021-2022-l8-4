package org.springframework.samples.petclinic.card;

import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.samples.petclinic.playercard.PlayerCardService;



public enum CardEnum{
    apartmentBuilding("Bloque de apartamentos") {

        @Override
        public void effect(Player player,PlayerService playerService){
            player.setVictoryPoints(player.getVictoryPoints() + 3);
            playerService.savePlayer(player);
        }

        
    },
    commuterTrain("Tren de cercanias") {


        @Override
        public void effect(Player player,PlayerService playerService){
            player.setVictoryPoints(player.getVictoryPoints() + 2);
            playerService.savePlayer(player);
        }

        
    },
    energize("Energizado") {

        @Override
        public void effect(Player player,PlayerService playerService){
            player.setVictoryPoints(player.getVictoryPoints() + 9);
            playerService.savePlayer(player);
        }        
    },
    fireBlast("Bola de fuego") {

        

        @Override
        public void effect(Player player,PlayerService playerService){
            for(Player play:player.getGame().getPlayers()) {
                if(!player.equals(play)) {
                    playerService.damagePlayer(play, 2);
                    playerService.savePlayer(play);
                }
            }
        }        
    },
    evacuationOrders("Ordenes de evacuacion") {

        @Override
        public void effect(Player player,PlayerService playerService){
            for(Player play:player.getGame().getPlayers()) {
                if(!player.equals(play)) {
                    playerService.substractVictoryPointsPlayer(play, 5);
                    playerService.savePlayer(play);
                }
            }
        }        
    },
    heal("Curacion") {
        @Override
        public void effect(Player player,PlayerService playerService){
            playerService.healDamage(player, 2);
            playerService.savePlayer(player);
        }        
    },
    gasRefinery("Refineria de gas") {

        @Override
        public void effect(Player player,PlayerService playerService){
             player.setVictoryPoints(player.getVictoryPoints() + 2);
            playerService.savePlayer(player);

            for(Player play:player.getGame().getPlayers()) {
                playerService.damagePlayer(play,3);
                playerService.savePlayer(play);
            }
        }        
    },

    highAltitudeBombing("Bombardeo de Gran Altura") {

        @Override
        public void effect(Player player,PlayerService playerService){
            for(Player play:player.getGame().getPlayers()) {
                playerService.damagePlayer(play, 3);
                playerService.savePlayer(play);
            }
        }        
    },

    jetFighters("Caza de combate") {

        @Override
        public void effect(Player player,PlayerService playerService){
            playerService.damagePlayer(player, 5);
            player.setVictoryPoints(player.getVictoryPoints() + 5);
            playerService.savePlayer(player);
            
        }        
    },
    

    cornerStore("Bazar de la esquina") {

        @Override
        public void effect(Player player,PlayerService playerService){
            player.setVictoryPoints(player.getVictoryPoints() + 1);
            playerService.savePlayer(player);
            
        }
    };
    
    public abstract void effect(Player player,PlayerService playerService);

    private final String name;
    
    private CardEnum(String name) {
        this.name=name;
    }

    public String getName(){
        return this.name;
    }

    
    
}
