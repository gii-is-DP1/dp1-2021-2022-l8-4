package org.springframework.samples.petclinic.userKoT;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;
/**
* @author Sara Cruz
* @author Rosa Molina
*/
@Getter
@Setter
@Entity
@NotEmpty
@Table(name = "userskot")
public class UserKoT extends BaseEntity{

    @Column(name = "username")
    private String username;

    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;
    
}
