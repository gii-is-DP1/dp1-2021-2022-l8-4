package org.springframework.samples.kingoftokyo.game;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import org.springframework.samples.kingoftokyo.gamecard.GameCard;
import org.springframework.samples.kingoftokyo.model.NamedEntity;
import org.springframework.samples.kingoftokyo.player.Monster;
import org.springframework.samples.kingoftokyo.player.Player;
import org.springframework.samples.kingoftokyo.user.User;

import lombok.Getter;
import lombok.Setter;

/**
 * @author José Maria Delgado Sanchez
 * @author Ricardo Nadal Garcia
 * @author Rosa Molina
 */

 @Entity
 @Getter
 @Setter
 @Table(name = "games")
 public class Game extends NamedEntity{

    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

    @Column(name="turn")
    private Integer turn;

    @Column(name="winner")
    private String winner;

    @Column(name="start_time")
    private LocalDateTime startTime;

    @Column(name="end_time")
    private LocalDateTime endTime;

    @Column(name = "max_number_of_players")
    private Integer maxNumberOfPlayers;

    
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Player> players;

    @OneToMany(mappedBy = "game")
    private List<GameCard> gameCards;

    @PreRemove
    private void setGameInGameCardsNull() {
        gameCards.forEach(gc -> gc.setGame(null));
    }

    
    /**
     * @return true if the game has started
     */
    public Boolean isStarted(){
       return this.turn !=0;
    }

   /**
   * @return true if the game still on going
   */
    public Boolean isOnGoing(){
      return isStarted() && !isFinished();
   }

    /**
     * @return true if the game has enough players to start
     */
    public Boolean hasEnoughPlayers(){
       Integer nPlayers = this.players.size();
      return  nPlayers >= 2 && nPlayers <= this.maxNumberOfPlayers;
   }


    /**
     * @return true if the game has room for more players
     */
    public Boolean hasRoom(){
       return this.players.size() < this.maxNumberOfPlayers;
    }

    /**
     * @return true if the monster is still available in the game
     */
    public Boolean monsterAvailable(Monster monsterName){
       return !this.players.stream()
                        .map(p -> p.getMonster())
                        .filter(pm -> pm == monsterName)
                        .findAny()
                        .isPresent();
    }

    /**
     * @return a Set containing the available monster to pick
     */
    public Set<Monster> availableMonsters(){
       Monster[] monsters = Monster.values();
       Set<Monster> allMonsters = new HashSet<>(monsters.length);
       Set<Monster> availableMonsters = new HashSet<>(monsters.length);
       Set<Monster> actualPlayersMonster = this.players.stream()
                                                         .map(p -> p.getMonster())
                                                         .collect(Collectors.toSet());
      
       for(int i=0;i<monsters.length;i++){
         allMonsters.add(monsters[i]);
       }

       availableMonsters = allMonsters.stream()
                                    .filter(x -> !actualPlayersMonster.contains(x))
                                    .collect(Collectors.toSet());

       return availableMonsters;
    }


  public List<Player> playersAlive(){
     List<Player> vivos=new ArrayList<>();
     for(Player player:this.players) {
         if(!player.isDead()){
            vivos.add(player);
         }
     }
     return vivos;
  }
  
  public Boolean isFinished(){ 
     return this.winner!=null && !this.winner.isEmpty() ;
  }



  public Integer playersAmount(){
     return this.players.size();
  }
  
  public List<Player> playersWithMaxVictoryPoints() {
     List<Player> playerList=new ArrayList<>();
     Integer maxPoints=20;
     for(Player player:this.players) {
        if(player.getVictoryPoints()>=maxPoints) {
            playerList.add(player);
        }
     }
     return playerList;
  }

  public List<Player> playersOutOfTokyo(){
     List<Player> playerList=new ArrayList<>();
     for(Player player:this.players){
        if(player.isOutOfTokyo() && !player.isDead()) {
           playerList.add(player);
        }
     }
     return playerList;
  }

   public List<Player> playersInTokyo(){
      List<Player> playerList=new ArrayList<>();
      for(Player player:this.players){
         if(player.isInTokyo() && !player.isDead()) {
            playerList.add(player);
         }
      }
      return playerList;
   }

   public Integer getDuration(){
      Integer hour = this.endTime.getHour() - this.startTime.getHour();
      Integer minute = this.endTime.getMinute() - this.startTime.getMinute();
      Integer second = this.endTime.getSecond() - this.startTime.getSecond();
      return (hour*60) + (minute) + (second/60);
   }

 }