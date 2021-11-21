package org.springframework.samples.petclinic.game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.samples.petclinic.dice.Roll;

public class MapGameRepository{
    Map<Integer,Roll> rollMap;
    Map<Integer, List<Integer>> turnListMap;
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
}
