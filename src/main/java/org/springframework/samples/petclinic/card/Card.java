package org.springframework.samples.petclinic.card;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Noelia López Durán
 * @author José Maria Delgado Sanchez
 */
@Entity
@Table(name = "cards")
public class Card extends NamedEntity{
    @NotEmpty
    @Getter
    @Setter
    private String name;

    @NotEmpty
    @Setter
    @Getter
    private Integer cost;

    @Getter
    @NotEmpty
    private CardType type;


}
