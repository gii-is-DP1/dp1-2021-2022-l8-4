package org.springframework.samples.petclinic.player;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.samples.petclinic.dice.Roll;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;


/**
 * @author Ricardo Nadal Garcia
 */
 
@Entity
@Getter
@Setter
@Table(name = "players")
public class Player extends BaseEntity {

    
    @Enumerated(value=EnumType.ORDINAL)
    @Column(name="monster_name")
    private MonsterName monsterName;


    
    @NotNull
    @Column(name="life_points")
    private Integer lifePoints;

    
    @NotNull
    @Min(0)
    @Column(name="victory_points")
    private Integer victoryPoints;

    
    @NotNull
    @Min(0)
    @Column(name="energy_points")
    private Integer energyPoints;

    
    @Enumerated(value=EnumType.ORDINAL)
    private LocationType location;
    
   
    @Transient
    private Roll roll;
   
    public String monsterStatus() {
        String status="ALIVE";
        if(this.lifePoints <= 0) {
            status="DEAD";
        }
        return status;
    }
}
