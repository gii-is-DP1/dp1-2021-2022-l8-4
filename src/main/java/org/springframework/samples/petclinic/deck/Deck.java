package org.springframework.samples.petclinic.deck;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author José Maria Delgado Sanchez
 */
@Entity
@Table(name = "decks")
public class Deck extends BaseEntity{
    
    @NotNull
    @Getter
    @Setter
    @Min(0)
    @Max(66)
    @Column(name="number_of_cards_left")
    private Integer numberOfCardsLeft;

    @NotEmpty
    @Getter
    @Setter
    @Column(name="card_list")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "deck")
    private List<Card> cardList;

    @OneToOne(mappedBy = "deck")
    private Game game;

    /**
	 * Return the next card in the deck.
	 * @return Card object if there are card left, null if there are not card left
	 */
    public Card nextCard(){
        if(this.numberOfCardsLeft != 0){
            this.numberOfCardsLeft--;
            Card card = cardList.get(this.numberOfCardsLeft);
            return card;
        }else{
            return null;
        }
    }

}
