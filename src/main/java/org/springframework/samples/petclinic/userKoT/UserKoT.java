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
@Getter
@Setter
@Entity
@Table(name = "userskot")
public class UserKoT extends NamedEntity{

    private String username;

    private String email;

    private String password;

    private String firstname;

    private String lastname;
    
}
