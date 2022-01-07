package org.springframework.samples.petclinic.game;

import java.util.ArrayList;
import java.util.Arrays;
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
       //Inicialización del deck de la partida 3
        Iterable<Card> itcard = cardService.findAll();
        Deck deck = new Deck(itcard);
        deckMap.put(3, deck);
        //Inicialización partida 4
        Deck deck4 = new Deck(itcard);
        deckMap.put(4, deck4);
        List<Integer> lsturno = new ArrayList<>();
        lsturno.add(16);lsturno.add(14);lsturno.add(15);lsturno.add(17);lsturno.add(18);
        turnListMap.put(4, lsturno);

        List<Integer> lsturno5 = new ArrayList<>();
        lsturno5.add(19);lsturno5.add(20);
        turnListMap.put(5, lsturno5);
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
