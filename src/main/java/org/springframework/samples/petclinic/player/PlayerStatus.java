package org.springframework.samples.petclinic.player;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.persistence.JoinColumn;

import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Noelia López Durán
 */

@Entity
@Setter
@Getter
@Table(name = "playerstatus")
public class PlayerStatus extends BaseEntity{
    @Enumerated(value=EnumType.ORDINAL)
    @Column(name="status")
    @NotNull
    private StatusType status;
    @NotNull
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    public void setPlayer(Player player){
        this.player = player;
    }
}