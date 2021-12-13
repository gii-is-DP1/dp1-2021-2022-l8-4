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
 * @author Noelia López Durán
 */

public enum CardEnum{ //Primero estan todas las de descarte al usarlo
    apartmentBuilding("Bloque de apartamentos","Otorga 3 puntos de victoria") {

        @Override
        public void effect(Player player,PlayerService playerService){
            player.setVictoryPoints(player.getVictoryPoints() + 3);
            playerService.savePlayer(player);
        }

        
    },
    commuterTrain("Tren de cercanias","Otorga 2 puntos de victoria") {


        @Override
        public void effect(Player player,PlayerService playerService){
            player.setVictoryPoints(player.getVictoryPoints() + 2);
            playerService.savePlayer(player);
        }

        
    },
    energize("Energizado","Otorga 9 puntos de energía") {

        @Override
        public void effect(Player player,PlayerService playerService){
            player.setVictoryPoints(player.getVictoryPoints() + 9);
            playerService.savePlayer(player);
        }        
    },
    fireBlast("Bola de fuego","Todos los monstruos enemigos reciben 2 puntos de daño") {

        

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
    evacuationOrders("Ordenes de evacuacion","Todos los monstruos enemigos pierden 5 puntos de victoria") {

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
    heal("Curacion","Tu monstruo se cura 2 puntos de vida") {
        @Override
        public void effect(Player player,PlayerService playerService){
            playerService.healDamage(player, 2);
            playerService.savePlayer(player);
        }        
    },
    gasRefinery("Refineria de gas","Obtienes 2 puntos de victoria y todos los monstruos enemigos reciben 3 puntos de daño") {

        @Override
        public void effect(Player player,PlayerService playerService){
             player.setVictoryPoints(player.getVictoryPoints() + 2);
            playerService.savePlayer(player);

            for(Player play:player.getGame().getPlayers()) {
                if(!player.equals(play)){
                    playerService.damagePlayer(play,3);
                    playerService.savePlayer(play);
                }
            }
        }        
    },

    highAltitudeBombing("Bombardeo de Gran Altura","Todos los monstruos(incluyéndote a ti) reciben 3 puntos de daño") {

        @Override
        public void effect(Player player,PlayerService playerService){
            for(Player play:player.getGame().getPlayers()) {
                playerService.damagePlayer(play, 3);
                playerService.savePlayer(play);
            }
        }        
    },

    jetFighters("Caza de combate","Obtienes 5 puntos de victoria pero pierdes 4 puntos de vida") {

        @Override
        public void effect(Player player,PlayerService playerService){
            playerService.damagePlayer(player, 5);
            player.setVictoryPoints(player.getVictoryPoints() + 5);
            playerService.savePlayer(player);
            
        }        
    },

    nationalGuard("Guarda Nacional","Obtienes 2 puntos de victoria pero pierdes 2 puntos de vida") {

        @Override
        public void effect(Player player,PlayerService playerService){
            playerService.damagePlayer(player, 2);
            player.setVictoryPoints(player.getVictoryPoints() + 2);
            playerService.savePlayer(player);
            
        }        
    },
    

    cornerStore("Bazar de la esquina","Otorga 1 punto de victoria") {

        @Override
        public void effect(Player player,PlayerService playerService){
            player.setVictoryPoints(player.getVictoryPoints() + 1);
            playerService.savePlayer(player);
            
        }
    },
    acidAttack("Ataque ácido","Obtienes en cada turno un dado de daño extra") {

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
    alphaMonster("Monstruo alfa","Obtienes un punto de victoria cuando consigues al menos 1 dado de daño") {

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
    fireBreathing("Aliento de fuego","Dañas a tus monstruos vecinos cuando consigues almenos 1 dado de daño") {

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
    friendOfChildren("Amigo de los niños","Si en tu turno obtienes al menos 1 punto de energía, ganas 1 punto de energía extra") {

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
    gourmet("Gourmet","Cuando consigas 3 o más dados 'ONE', recibirás 2 puntos de victoria extra"){
        @Override
        public void effect(Player player, PlayerService playerService){
            Roll roll=MapGameRepository.getInstance().getRoll(player.getGame().getId());
            if(roll.isFinished()) {
                Map<String,Integer> rollValues=playerService.countRollValues(roll.getValues());
                if(rollValues.get("ones") > 2) {
                    player.setVictoryPoints(player.getVictoryPoints()+2);
                    playerService.savePlayer(player);
                }
            }
        }
    };
    
    public abstract void effect(Player player,PlayerService playerService);

    private final String name;
    private final String description;
    
    private CardEnum(String name,String description) {
        this.name=name;
        this.description=description;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    
    
}
