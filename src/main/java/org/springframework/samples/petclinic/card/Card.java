package org.springframework.samples.petclinic.card;

import javax.persistence.Column;
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


}
