package org.springframework.samples.petclinic.game;


import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.deck.Deck;
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


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "deck_id", referencedColumnName = "id")
    private Deck deck;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Card> availableCards;

 }