package org.springframework.samples.petclinic.userKoT;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;
/**
* @author Sara Cruz
* @author Rosa Molina
*/

@Entity
@Table(name = "usersKoT")
public class User extends NamedEntity {

    @Getter
    @Setter
    @Id
    String username;

    @Getter
    @Setter
    String email;

    @Getter
    @Setter
    String password;

    @Getter
    @Setter
    String firstName;

    @Getter
    @Setter
    String lastName;
    
}
