package org.springframework.samples.petclinic.player;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * Simple JavaBean domain object representing a player Status.
 *
 * @author Noelia López Durán
 */

@Entity
public class PlayerStatus extends BaseEntity{
    @Getter
    @Setter
    @Enumerated(value=EnumType.ORDINAL)
    private StatusType status;

    @Setter
    @Getter
    private Integer amount;

    @ManyToOne
    private Player player;
}