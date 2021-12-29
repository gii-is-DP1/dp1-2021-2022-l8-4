package org.springframework.samples.petclinic.card;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.samples.petclinic.gamecard.GameCard;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.playercard.PlayerCard;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Noelia López Durán
 * @author José Maria Delgado Sanchez
 * @author Ricardo Nadal García
 */
@Entity
@Getter
@Setter
@Table(name = "cards")
public class Card extends BaseEntity {

    @NotNull
    @Column(name = "cost")
    private Integer cost;

    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "type")
    private CardType type;

    @Enumerated(value = EnumType.STRING)
    @Column(name="card_enum")
    private CardEnum cardEnum;

    @OneToMany(mappedBy = "card")
    private List<GameCard> gameCard;

    @OneToMany(mappedBy = "card")
    private List<PlayerCard> playerCard;

}
