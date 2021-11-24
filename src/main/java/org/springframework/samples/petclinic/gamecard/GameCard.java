package org.springframework.samples.petclinic.gamecard;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author José Maria Delgado Sanchez
 */
 @Entity
 @Table(name="games_cards",
 uniqueConstraints = @UniqueConstraint(name = "uniqueGameCard", columnNames = {"game_id", "card_id"}))
 public class GameCard extends BaseEntity{

    @Getter
    @Setter
    @ManyToOne(optional=false, cascade = CascadeType.ALL)
    @JoinColumn(name="game_id")
    private Game game;

    @Getter
    @Setter
    @ManyToOne(optional=false, cascade = CascadeType.ALL)
    @JoinColumn(name="card_id")
    private Card card;

    @Setter
    @Getter
    @Column(name="sold")
    private Boolean sold;
 }