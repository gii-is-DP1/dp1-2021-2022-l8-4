package org.springframework.samples.petclinic.card;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dice.DiceValues;
import org.springframework.samples.petclinic.dice.Roll;
import org.springframework.samples.petclinic.game.MapGameRepository;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.samples.petclinic.playercard.PlayerCardService;


/**
 * @author Ricardo Nadal Garcia
 */

public enum CardEnum{ //Primero estan todas las de descarte al usarlo
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

    nationalGuard("Guarda Nacional") {

        @Override
        public void effect(Player player,PlayerService playerService){
            playerService.damagePlayer(player, 2);
            player.setVictoryPoints(player.getVictoryPoints() + 2);
            playerService.savePlayer(player);
            
        }        
    },
    

    cornerStore("Bazar de la esquina") {

        @Override
        public void effect(Player player,PlayerService playerService){
            player.setVictoryPoints(player.getVictoryPoints() + 1);
            playerService.savePlayer(player);
            
        }
    },
    acidAttack("Ataque ácido") {

        @Override 
        public void effect(Player player, PlayerService playerService) {
            Roll roll=MapGameRepository.getInstance().getRoll(player.getGame().getId());
            if(roll.isFinished()) {
                List<DiceValues> dices= roll.getCardExtraValues();
                dices.add(DiceValues.ATTACK);
                roll.setCardExtraValues(dices);
                MapGameRepository.getInstance().putRoll(player.getGame().getId(), roll);
            }
        }  
    },
    alphaMonster("Monstruo alfa") {

        @Override 
        public void effect(Player player, PlayerService playerService) {
            Roll roll=MapGameRepository.getInstance().getRoll(player.getGame().getId());
            if(roll.isFinished()) {
                Map<String,Integer> rollValues=playerService.countRollValues(roll.getValues());
                if(rollValues.get("damage") > 0) {
                    player.setVictoryPoints(player.getVictoryPoints()+1);
                    playerService.savePlayer(player);
                }
            }
        }  
    },
    fireBreathing("Aliento de fuego") {

        @Override 
        public void effect(Player player, PlayerService playerService) {
            Roll roll=MapGameRepository.getInstance().getRoll(player.getGame().getId());
            if(roll.isFinished()) {
                Map<String,Integer> rollValues=playerService.countRollValues(roll.getValues());
                if(rollValues.get("damage") > 0) {
                    if(player.isInTokyo()){
                         for(Player playTokyo:player.getGame().playersInTokyo()){
                             if(player.getId()!=playTokyo.getId()){
                                 playerService.damagePlayer(playTokyo, 1);
                                 playerService.savePlayer(player);
                             }
                         }
                    } else{
                        for(Player playOutTokyo:player.getGame().playersOutOfTokyo()) {
                            if(player.getId() != playOutTokyo.getId()){
                                playerService.damagePlayer(playOutTokyo, 1);
                                playerService.savePlayer(player);
                            }
                        }
                    }
                }
            }
        }  
    },
    friendOfChildren("Amigo de los niños") {

        @Override 
        public void effect(Player player, PlayerService playerService) {
            Roll roll=MapGameRepository.getInstance().getRoll(player.getGame().getId());
            if(roll.isFinished()) {
                Map<String,Integer> rollValues=playerService.countRollValues(roll.getValues());
                if(rollValues.get("energy") > 0) {
                    List<DiceValues> dices= roll.getCardExtraValues();
                    dices.add(DiceValues.ENERGY);
                    roll.setCardExtraValues(dices);
                    MapGameRepository.getInstance().putRoll(player.getGame().getId(), roll);
                }
            }
        }  
    },
    extraHead("Segunda cabeza") {

        @Override 
        public void effect(Player player, PlayerService playerService) {
            Roll roll=MapGameRepository.getInstance().getRoll(player.getGame().getId());
            if(roll.getRollAmount() == 0 ) {
                List<DiceValues> dices=roll.getValues();
                dices.add(DiceValues.HEAL);
                roll.setValues(dices);
                MapGameRepository.getInstance().putRoll(player.getGame().getId(), roll);
            }
        }  
    },
    giantBrain("Cerebro galaxia") {

        @Override 
        public void effect(Player player, PlayerService playerService) {
            Roll roll=MapGameRepository.getInstance().getRoll(player.getGame().getId());
            if(roll.getRollAmount() == 0 )  {                
                roll.setMaxThrows(roll.getMaxThrows()+1);;
                MapGameRepository.getInstance().putRoll(player.getGame().getId(), roll);
            }
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
