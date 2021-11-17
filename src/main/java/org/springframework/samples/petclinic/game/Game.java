package org.springframework.samples.petclinic.game;

import java.time.LocalDateTime;
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
 */

 @Entity
 @Table(name = "games")
 public class Game extends NamedEntity{

    @NotEmpty
    @Getter
    @Setter
    @Column(name="creator")
    private String creator;

    @NotNull
    @Getter
    @Setter
    @Min(0)
    @Column(name="turn")
    private Integer turn;

    @Getter
    @Setter
    @Column(name="winner")
    private String winner;

    @NotNull
    @Getter
    @Setter
    @Column(name="start_time")
    private LocalDateTime startTime;

    @NotNull
    @Getter
    @Setter
    @Column(name="end_time")
    private LocalDateTime endTime;

    @Getter
    @Setter
    @OneToMany(mappedBy = "game")
    private List<Player> players;

    
    @OneToOne
    private Board board;
 }