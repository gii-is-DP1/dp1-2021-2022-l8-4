package org.springframework.samples.petclinic.player;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;


/**
 * @author Ricardo Nadal Garcia
 */
 
@Entity
@Table(name = "players")
public class Player extends NamedEntity {

    @Getter
    @Setter
    @Enumerated(value=EnumType.ORDINAL)
    private MonsterName monsterName;

    @Getter
    @Setter
    @NotNull
    private Integer lifePoints;

    @Getter
    @Setter
    @NotNull
    @Min(0)
    private Integer victoryPoints;

    @Getter
    @Setter
    @NotNull
    @Min(0)
    private Integer energyPoints;

    @Getter
    @Setter
    @Enumerated(value=EnumType.ORDINAL)
    private LocationType location;
    
   
    public String monsterStatus() {
        String status="ALIVE";
        if(this.lifePoints <= 0) {
            status="DEAD";
        }
        return status;
    }
}
