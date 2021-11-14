package org.springframework.samples.petclinic.card;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.samples.petclinic.board.Board;
import org.springframework.samples.petclinic.deck.Deck;
import org.springframework.samples.petclinic.model.NamedEntity;
import org.springframework.samples.petclinic.player.Player;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Noelia López Durán
 * @author José Maria Delgado Sanchez
 * @author Ricardo Nadal García
 */
@Entity
@Table(name = "cards")
public class Card extends NamedEntity{

    @NotNull
    @Setter
    @Getter
    @Column(name="cost")
    private Integer cost;

    @Getter
    @Setter
    @Enumerated(value=EnumType.ORDINAL)
    @Column(name="type")
    private CardType type;

    @NotNull
    @Getter
    @Setter
    private Boolean discarded;

    @ManyToOne(optional=false)
    @JoinColumn(name="deck_id")
    private Deck deck;

    @ManyToOne(optional=true)
    @JoinColumn(name="board_id")
    private Board board;

    @ManyToOne(optional = true)
    @JoinColumn(name="player_id")
    private Player player;

    
}
