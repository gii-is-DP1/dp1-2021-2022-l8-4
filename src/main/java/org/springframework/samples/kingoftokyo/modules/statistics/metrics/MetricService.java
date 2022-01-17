package org.springframework.samples.kingoftokyo.modules.statistics.metrics;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.samples.kingoftokyo.game.Game;
import org.springframework.samples.kingoftokyo.game.GameRepository;
import org.springframework.samples.kingoftokyo.player.Monster;
import org.springframework.samples.kingoftokyo.player.PlayerRepository;
import org.springframework.samples.kingoftokyo.user.User;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;

/**
*  @author Rosa Molina
*  @author Sara Cruz
*/

@Service
public class MetricService {

    @Autowired
    private MetricRepository metricRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;


    /**
     * 
     * @return List of Users with their associated score of games played ordered
     */
    @Transactional
    public Page<MetricData> gamesPlayedRanking(int pageNumber, int numberOfElements){
        PageRequest pageable = PageRequest.of(pageNumber, numberOfElements);    
        return metricRepository.gamesPlayedRanking(pageable);
    }

    /**
     * 
     * @return List of Users with their associated score of wins ordered
     */
    @Transactional
    public Page<MetricData> winsRanking(int pageNumber, int numberOfElements){    
        PageRequest pageable = PageRequest.of(pageNumber, numberOfElements);    
        return metricRepository.winsRanking(pageable);
    }    
    
    /**
    * 
    * @return List of Users with their associated score of wins ordered
    */
   @Transactional
   public List<MetricData> findTurnsTokyo(){    
       PageRequest pageable = PageRequest.of(0, 5);    
       Page<MetricData> pages = metricRepository.maxTurnUsers(pageable);
       return pages.toList();
   }

    /**
     * 
     * @return List of Users with their associated score of achievements ordered
     */
    @Transactional
    public Page<MetricData> scoresRanking(int pageNumber, int numberOfElements){    
        PageRequest pageable = PageRequest.of(pageNumber, numberOfElements);    
        return metricRepository.scoresRanking(pageable);
    }

    @Transactional
    public Page<MetricData> rankingByMetricType(MetricType metric, int pageNumber, int numberOfElements){
        switch(metric){
            case gamesPlayed:
                return gamesPlayedRanking(pageNumber, numberOfElements);
            case wins:
                return winsRanking(pageNumber, numberOfElements);
            default:
                break;
        }
        return null;
    }

    /**
     * 
     * @return List of 10 Users with their associated score of wins ordered
     */
    @Transactional
    public List<MetricData> winsRankingStatistic(){
        Page<MetricData> pages = winsRanking(0, 10);
        return pages.toList();
    }

    /**
     * 
     * @return List of 10 Users with their associated score of achievements ordered
     */
    @Transactional
    public List<MetricData> scoresRankingStatistic(){
        Page<MetricData> pages = scoresRanking(0, 10);
        return pages.toList();
    }

    @Transactional
    public Integer findTotalGamesApp() {
        return metricRepository.totalGamesOfApp();
    }

    @Transactional
    public Integer findTotalWinsGamesCurrentUser(String username) {
        return metricRepository.totalGamesWinnerCurrentUser(username);
    }

    @Transactional
    public Integer findTotalGamesCurrentUser(User user) {
        return metricRepository.totalGamesCurrentUser(user);
    }


    @Transactional
    public Integer findTimeGames() {
        Integer duration = 0;
        Integer gamesCounter = 0;
        Iterable<Game> games = this.gameRepository.findAll();
        for(Game game: games){
            if(game.isFinished()){
                gamesCounter+=1;
                duration += game.getDuration();
            }
        }
        return duration/gamesCounter;
    }

    @Transactional
    public Integer findTimeGamesforUser(User user) {
        Integer duration = 0;
        Integer gamesCounter = 0;
        Iterable<Game> games = this.metricRepository.findGamesCurrentUser(user);
        for(Game game: games){
            if(game.isFinished()){
                gamesCounter+=1;
                duration += game.getDuration();
            }
        }
        if(gamesCounter.equals(0)){
            return null;
        }else{
            return duration/gamesCounter;
        }
    }

    @Transactional
    public Monster findMonsterModa(){
        HashMap<Integer, Integer> m = new HashMap<Integer, Integer>();				
        List<Integer> monsters = this.playerRepository.listMonster();
        for (Integer elemento: monsters){			
        if (m.containsKey(elemento))					
            m.put(elemento,m.get(elemento)+1);
        else
            m.put(elemento,1);			
        }
        int moda = 0, mayor = 0;
        for (HashMap.Entry<Integer, Integer> entry : m.entrySet()) {
            if (entry.getValue() > mayor) {
                mayor = entry.getValue();
                moda = entry.getKey();
            }
        }
        Monster monstermoda = Monster.values()[moda];
        return monstermoda;
    }

    @Transactional
    public Monster findMonsterNoModa(){
        HashMap<Integer, Integer> m = new HashMap<Integer, Integer>();				
        List<Integer> monsters = this.playerRepository.listMonster();
        for (Integer elemento: monsters){			
        if (m.containsKey(elemento))					
            m.put(elemento,m.get(elemento)+1);
        else
            m.put(elemento,1);			
        }
        int nomoda = 0, menor = 0;
        for (HashMap.Entry<Integer, Integer> entry : m.entrySet()) {
            if (entry.getValue() < menor) {
                menor = entry.getValue();
                nomoda = entry.getKey();
            }
        }
        Monster monstermenor = Monster.values()[nomoda];
        return monstermenor;
    }
}
