package org.springframework.samples.petclinic.userKoT;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "authoritieskot")
public class AuthoritiesKoT extends BaseEntity{
	
	@ManyToOne
	@JoinColumn(name = "userid")
	UserKoT userkot;
	
	@Size(min = 3, max = 50)
	String authority;
	
	
}
