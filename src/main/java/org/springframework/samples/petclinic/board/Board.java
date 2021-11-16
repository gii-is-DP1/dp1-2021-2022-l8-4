package org.springframework.samples.petclinic.board;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.deck.Deck;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;
/**
 * @author Ricardo Nadal Garcia
 * @author José María Delgado Sánchez
 */

@Entity
@Table(name="boards")
public class Board extends BaseEntity{

    @Setter
    @Getter
    @Enumerated(value=EnumType.ORDINAL)
    @Column(name="tokyo_city_status")
    private LocationStatus tokyoCityStatus;

    @Setter
    @Getter
    @Enumerated(value=EnumType.ORDINAL)
    @Column(name="tokyo_bay_status")
    private LocationStatus tokyoBayStatus;

    @Setter
    @Getter
    @OneToOne(mappedBy = "board")
    private Game game;

    @Setter
    @Getter
    @OneToOne
    private Deck deck;

    @Setter
    @Getter
    @OneToMany(mappedBy = "board")
    private List<Card> card;
}
