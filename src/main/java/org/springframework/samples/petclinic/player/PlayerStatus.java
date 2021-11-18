package org.springframework.samples.petclinic.player;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * Simple JavaBean domain object representing a player Status.
 *
 * @author Noelia López Durán
 */

@Entity
@Setter
@Getter
@Table(name = "playerstatus")
public class PlayerStatus extends BaseEntity{
    @Enumerated(value=EnumType.ORDINAL)
    private StatusType status;

    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    public void setStatus(Player player){
        this.player = player;
    }
}