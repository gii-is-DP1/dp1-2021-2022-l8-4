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

    @Column(name = "finished")
    private Boolean finished;

    

    public List<Integer> initialTurnList(){
       List<Integer> listaTurnos=new ArrayList<Integer>();
       for(Player player:this.players) {
          listaTurnos.add(player.getId());
       }
       Collections.shuffle(listaTurnos);
       return listaTurnos;
    }

    public Player actualTurn(List<Integer> turnList){
      
      List<Player> jugadores=getPlayers();
      Player jugadorActual= actualTurnPosicionLista(turnList, getTurn(), jugadores);
      
      return jugadorActual;
      
  }

  private Player actualTurnPosicionLista(List<Integer> turnList,Integer posicionLista,List<Player> jugadores) {
      Integer numeroTurno = posicionLista % (jugadores.size());
      
      for(Player player:players) {
         if(player.getId()==turnList.get(numeroTurno) && player.isDead()) {
            numeroTurno++;
            return actualTurnPosicionLista(turnList, numeroTurno, jugadores);
         } else if(player.getId()==turnList.get(numeroTurno)){
            return player;
         }
      }
      return null;
  }

  public List<Player> playersAlive(){
     List<Player> vivos=new ArrayList<Player>();
     for(Player player:this.players) {
         if(!player.isDead()){
            vivos.add(player);
         }
     }
     return vivos;
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