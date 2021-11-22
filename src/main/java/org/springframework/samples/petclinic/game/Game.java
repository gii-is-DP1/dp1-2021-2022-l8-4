package org.springframework.samples.petclinic.game;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.samples.petclinic.board.Board;
import org.springframework.samples.petclinic.model.NamedEntity;
import org.springframework.samples.petclinic.player.Player;

import lombok.Getter;
import lombok.Setter;

/**
 * @author José Maria Delgado Sanchez
 * @author Ricardo Nadal Garcia
 */

 @Entity
 @Getter
 @Setter
 @Table(name = "games")
 public class Game extends NamedEntity{

    @NotEmpty
  
    @Column(name="creator")
    private String creator;

    @NotNull
   
    @Min(0)
    @Column(name="turn")
    private Integer turn;

    
    @Column(name="winner")
    private String winner;

    @NotNull
    
    @Column(name="start_time")
    private LocalDateTime startTime;

    
    
    @Column(name="end_time")
    private LocalDateTime endTime;

    
    @OneToMany(mappedBy = "game")
    private List<Player> players;

    @Getter
    @Setter
    @OneToOne
    private Board board;

    
 

  public List<Player> playersAlive(){
     List<Player> vivos=new ArrayList<Player>();
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
     List<Player> playerList=new ArrayList<Player>();
     Integer maxPoints=20;
     for(Player player:this.players) {
        if(player.getVictoryPoints()>=maxPoints) {
            playerList.add(player);
        }
     }
     return playerList;
  }


 }