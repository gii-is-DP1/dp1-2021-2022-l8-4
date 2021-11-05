package org.springframework.samples.petclinic.userKoT;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
/**
* @author Sara Cruz
* @author Rosa Molina
*/
@Getter
@Setter
@Entity
@Table(name = "usersKoT")
public class User {
    @Id
    String username;

    String email;

    String password;

    String firstName;

    String lastName;
    
    
}
