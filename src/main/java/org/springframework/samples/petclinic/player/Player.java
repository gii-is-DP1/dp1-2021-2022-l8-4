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

    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "monster_name")
    private MonsterName monsterName;

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

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player", fetch = FetchType.EAGER)
    private Set<PlayerStatus> playerStatus;

    /**
     * Reduce player's life points to 0 and remove the player from the on going game
     */
    public void surrender() {
        if (location.equals(LocationType.bahiaTokyo) || location.equals(LocationType.ciudadTokyo)) {
            setLocation(LocationType.fueraTokyo);
        }
        this.lifePoints = 0;
    }

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
        return 10; //Cuando se haga lo de la carta de vida maxima esto se cambia 
    }

}
