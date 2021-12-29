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
    @Column(name = "life_points")
    private Integer lifePoints;

    
    @NotNull
    @Min(0)
    @Column(name = "victory_points")
    private Integer victoryPoints;

    @NotNull
    @Min(0)
    @Column(name = "energy_points")
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player", fetch = FetchType.EAGER)
    private Set<PlayerStatus> playerStatus;

    @NotNull
    @Column(name = "recently_hurt" ,columnDefinition = "boolean default false")
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

    protected Set<PlayerStatus> getPlayerStatusInternal() {
        if (this.playerStatus == null) {
            this.playerStatus = new HashSet<>();
        }
        return this.playerStatus;
    }

    protected void setPlayerStatusInternal(Set<PlayerStatus> playerStatus) {
        this.playerStatus = playerStatus;
    }

    public String monsterStatus() {
        String status = "ALIVE";
        if (this.lifePoints <= 0) {
            status = "DEAD";
        }
        return status;
    }

    public List<PlayerStatus> getPlayerStatusList() {
        List<PlayerStatus> listPlayerStatus = new ArrayList<>(getPlayerStatusInternal());
        return Collections.unmodifiableList(listPlayerStatus);
    }

    public void addPlayerStatus(PlayerStatus playerStatus) {
        getPlayerStatusInternal().add(playerStatus);
        playerStatus.setPlayer(this);

    }

    public Boolean isDead() {
        return this.lifePoints <= 0;
    }

    public Integer getMaxHealth() {
        Integer maxHealth=10;
        if(this.playerCard.stream()
                        .filter(x -> x.getPlayer().getId() == this.id) 
                        .map(x -> x.getCard().getCardEnum())
                        .anyMatch(x -> x.equals(CardEnum.evenBigger))){
                            maxHealth=12;
                        }
        return maxHealth; //Cuando se haga lo de la carta de vida maxima esto se cambia 
    }

    public Boolean getRecentlyHurt(){
        return this.recentlyHurt;
    }

    public Boolean isOutOfTokyo(){
        return this.location == LocationType.fueraTokyo;
    }

    public Boolean isInTokyo(){
        return this.location == LocationType.ciudadTokyo || this.location== LocationType.bahiaTokyo;
    }

}
