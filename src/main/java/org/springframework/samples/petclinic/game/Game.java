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

import org.springframework.data.annotation.Transient;
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

    @NotNull
    
    @Column(name="end_time")
    private LocalDateTime endTime;

    
    @OneToMany(mappedBy = "game")
    private List<Player> players;

    
    @OneToOne
    private Board board;

    

    public List<Integer> initialTurnList(){
       List<Integer> listaTurnos=new ArrayList<Integer>();
       for(Player player:this.players) {
          listaTurnos.add(player.getId());
       }
       Collections.shuffle(listaTurnos);
       return listaTurnos;
    }
 }