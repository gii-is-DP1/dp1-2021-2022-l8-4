package org.springframework.samples.petclinic.card;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.samples.petclinic.model.NamedEntity;

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
    @NotEmpty
    @Getter
    @Setter
    private String name;

    @NotNull
    @Setter
    @Getter
    private Integer cost;

    @Getter
    @Enumerated(value=EnumType.ORDINAL)
    @Setter
    private CardType type;


}
