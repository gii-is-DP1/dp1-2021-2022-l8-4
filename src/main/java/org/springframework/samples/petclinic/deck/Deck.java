package org.springframework.samples.petclinic.deck;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author José Maria Delgado Sanchez
 */
@Entity
@Table(name = "decks")
public class Deck extends NamedEntity{
    
    @NotNull
    @Getter
    @Setter
    private Integer cardsLeft;

    @NotEmpty
    @Getter
    @Setter
    private ArrayList<Card> cardList;
}
