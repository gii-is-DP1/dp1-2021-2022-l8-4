package org.springframework.samples.petclinic.association;

import javax.enterprise.inject.Default;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.player.Player;

import lombok.Getter;
import lombok.Setter;

/**
 * @author José Maria Delgado Sanchez
 */

@Entity
@Table(name="players_cards")
public class PlayerCard extends BaseEntity {

    @Getter
    @Setter
    @ManyToOne(optional=false) 
    @JoinColumn(name="player_id")
    private Player player;

    @Getter
    @Setter
    @ManyToOne(optional=false)
    @JoinColumn(name="card_id")
    private Card card;

    @Setter
    @Getter
    @Column(name="discarded")
    private Boolean discarded;
    
}
