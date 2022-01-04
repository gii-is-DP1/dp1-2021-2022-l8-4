package org.springframework.samples.petclinic.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardEnum;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.playercard.PlayerCard;
import org.springframework.samples.petclinic.user.User;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Ricardo Nadal Garcia
 * @author Noelia López Durán
 * @author José María Delgado Sánchez
 */



@Entity
@Getter
@Setter
@Table(name = "players")
public class Player extends BaseEntity {

    @NotNull
    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "monster_name")
    private Monster monster;

    @NotNull
    private Integer lifePoints;

    
    @NotNull
    @Min(0)
    private Integer victoryPoints;

    @NotNull
    @Min(0)
    private Integer energyPoints;

    @Enumerated(value = EnumType.ORDINAL)
    private LocationType location;

    @ManyToOne(optional = false)
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "player")
    private List<PlayerCard> playerCard;

    @ManyToOne(optional = true)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "boolean default false")
    private Boolean recentlyHurt;
    

    /**
     * Get a list of cards the has not been used yet by the player
     * @return list of available cards
     */
    public List<Card> getAvailableCards(){
        return this.playerCard.stream()
                    .filter(pc -> pc.getDiscarded().equals(Boolean.FALSE))
                    .map(pc -> pc.getCard())
                    .collect(Collectors.toList());
    }

    /**
     * @return "ALIVE" if the player's health points is >0 or "DEAD" if the player's health points is <=0
     *
     */
    public String monsterStatus() {
        String status = "ALIVE";
        
        if (this.lifePoints <= 0) {
            status = "DEAD";
        }
        return status;
    }


    public Boolean isDead() {
        Integer minHealth=0;
        return this.lifePoints <= minHealth;
    }
    /**
     * @return the maximum health points of a player
     *
     */
    public Integer getMaxHealth() {
        Integer maxHealth=10;
        if(this.playerCard.stream()
                        .filter(x -> x.getPlayer().getId() == this.id) 
                        .map(x -> x.getCard().getCardEnum())
                        .anyMatch(x -> x.equals(CardEnum.evenBigger))){
                            maxHealth=12;
                        }
        return maxHealth;
    }

    public Boolean getRecentlyHurt(){
        return this.recentlyHurt;
    }

    /**
     * @return whether the player is outside of Tokyo or not.
     *
     */
    public Boolean isOutOfTokyo(){
        return this.location == LocationType.fueraTokyo;
    }
    /**
     * @return whether the player is in Tokyo or TokyoBay or not.
     *
     */
    public Boolean isInTokyo(){
        return this.location == LocationType.ciudadTokyo || this.location== LocationType.bahiaTokyo;
    }

}
