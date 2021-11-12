package org.springframework.samples.petclinic.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;


/**
 * @author Ricardo Nadal Garcia
 * @author Noelia López Durán
 */
 
@Entity
@Table(name = "players")
public class Player extends BaseEntity {

    @Getter
    @Setter
    @Enumerated(value=EnumType.ORDINAL)
    @Column(name="monster_name")
    private MonsterName monsterName;

    @Getter
    @Setter
    @NotNull
    @Column(name="life_points")
    private Integer lifePoints;

    @Getter
    @Setter
    @NotNull
    @Min(0)
    @Column(name="victory_points")
    private Integer victoryPoints;

    @Getter
    @Setter
    @NotNull
    @Min(0)
    @Column(name="energy_points")
    private Integer energyPoints;

    @Getter
    @Setter
    @Enumerated(value=EnumType.ORDINAL)
    private LocationType location;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player" , fetch = FetchType.EAGER)
    private Set<PlayerStatus> playerStatus; 
    
    protected Set<PlayerStatus> getPlayerStatusInternal() {
		if (this.playerStatus == null) {
			this.playerStatus = new HashSet<>();
		}
		return this.playerStatus;
	}
   
    public String monsterStatus() {
        String status="ALIVE";
        if(this.lifePoints <= 0) {
            status="DEAD";
        }
        return status;
    }
    public List<PlayerStatus> getPlayerStatus(){
        List<PlayerStatus> listPlayerStatus = new ArrayList<>(getPlayerStatusInternal());
		return Collections.unmodifiableList(listPlayerStatus);
    }

    public void addPlayerStatus(PlayerStatus playerStatus){
        getPlayerStatusInternal().add(playerStatus);
        playerStatus.setStatus(this);
        
    }
}
