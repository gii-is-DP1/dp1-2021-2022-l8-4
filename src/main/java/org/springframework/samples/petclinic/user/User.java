package org.springframework.samples.petclinic.user;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.player.Player;

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
public class User extends BaseEntity{

    @NotEmpty
    @Column(name = "username")
    private String username;

    @NotEmpty
    @Email
    @Column(name = "email")
    private String email;

    @NotEmpty
    @Column(name = "password")
    private String password;
    
    private boolean enabled;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private Set<Authorities> authorities;

    @OneToMany(mappedBy = "user")
    private Set<Player> players;

    @OneToMany(mappedBy = "creator")
    private Set<Game> games;
}
