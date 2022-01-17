package org.springframework.samples.kingoftokyo.user;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.samples.kingoftokyo.game.Game;
import org.springframework.samples.kingoftokyo.model.BaseEntity;
import org.springframework.samples.kingoftokyo.modules.statistics.achievement.Achievement;
import org.springframework.samples.kingoftokyo.player.Player;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sara Cruz
 * @author Rosa Molina
 */

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @NotEmpty
    @Column(name = "username", unique = true, updatable = false)
    private String username;

    @NotEmpty
    @Email
    @Column(name = "email", unique = true)
    private String email;

    @NotEmpty
    private String password;

    private boolean enabled;

    @Column(columnDefinition = "long default 0l")
    private Long maxTurnsTokyo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Authorities> authorities;

    @OneToMany(mappedBy = "user")
    private Set<Player> players;

    @OneToMany(mappedBy = "creator")
    private Set<Game> games;

    @PreRemove
    private void setCreatorInGameNull() {
        games.forEach(game -> game.setCreator(null));
        players.forEach(player -> player.setUser(null));
    }

    @ManyToMany
    @JoinTable(name = "users_achievements", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "achievement_id"))
    private Set<Achievement> achievements;

    /**
     * @return true if the user has an active player in a game/lobby
     */
    public Boolean hasActivePlayer() {
        if (this.players == null || this.players.isEmpty()) {
            return false;
        } else {
            return this.players.stream()
                    .filter(p -> !p.isDead())
                    .map(p -> p.getGame())
                    .filter(g -> !g.isFinished())
                    .findFirst()
                    .isPresent();
        }
    }

    /**
     * @return true if the user is the creator of the selected game
     */
    public Boolean isCreator(Game game) {
        return this.games.stream()
                .filter(g -> g.equals(game))
                .findFirst()
                .isPresent();
    }

    /**
     * @return true if the user has an active game previously created by the user
     */
    public Boolean hasActiveGameAsCreator() {
        if (this.games == null || this.games.isEmpty()) {
            return false;
        } else {
            return this.games.stream()
                    .filter(g -> !g.isStarted() && g.getCreator().getId().equals(this.id))
                    .findFirst()
                    .isPresent();

        }
    }
}
