package org.springframework.samples.petclinic.game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.samples.petclinic.card.Deck;
import org.springframework.samples.petclinic.dice.Roll;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author Ricardo Nadal Garcia
 * @author Noelia López Durán
 */

@Service // Dejo esto como @Service cuando debería ser @Component porque si no , no funcionan los test 
public class MapGameRepository{
    private CardService cardService;
    
    Map<Integer,Roll> rollMap;
    Map<Integer, List<Integer>> turnListMap;
    Map<Integer, Deck> deckMap;
    
    @Autowired
    public MapGameRepository(CardService cardService){
        this.cardService = cardService;
        init();
    }

    public void init(){
       rollMap=new HashMap<Integer,Roll>();
       turnListMap=new HashMap<Integer,List<Integer>>();
       deckMap = new HashMap<Integer, Deck>();
        Iterable<Card> itcard = cardService.findAll();
        Deck deck = new Deck(itcard);
        deckMap.put(3, deck);
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
