package org.springframework.samples.petclinic.game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.samples.petclinic.card.Deck;
import org.springframework.samples.petclinic.dice.Roll;

public class MapGameRepository{
    Map<Integer,Roll> rollMap;
    Map<Integer, List<Integer>> turnListMap;
    Map<Integer, Deck> deckMap;
    private static MapGameRepository instance=null;
    

    public static MapGameRepository getInstance() {
        if(instance==null) {
            instance = new MapGameRepository();
            instance.init();
        }

        return instance;
    }

    public void init(){
       rollMap=new HashMap<Integer,Roll>();
       turnListMap=new HashMap<Integer,List<Integer>>();
       deckMap = new HashMap<Integer, Deck>();
    }

    public void putRoll(Integer gameId,Roll roll) {
        rollMap.put(gameId, roll);
    }

    public Roll getRoll(Integer gameId){
        if(!rollMap.containsKey(gameId)) {
            rollMap.put(gameId, new Roll());
        }
        return rollMap.get(gameId);
    }

    public void putTurnList(Integer gameId,List<Integer> turnList) {
        turnListMap.put(gameId, turnList);
    }

    public List<Integer> getTurnList(Integer gameId){
        
        return turnListMap.get(gameId);
    }

    /**
     * Insert or update a deck linked to a game
     * @param Game game key
     * @param Deck deck linked to the game
     */
    public void putDeck(Game game, Deck deck){
        deckMap.put(game.getId(), deck);
    }

    /**
     * Retrieve the deck linked to a game if it exists
     * @param Game game key
     * @return Deck linked to the game or null if the game has not deck assigned
     */
    public Deck getDeck(Game game){
        if(deckMap.containsKey(game.getId())){
            return deckMap.get(game.getId());
        }else{
            return null;
        }
    }
    
}
