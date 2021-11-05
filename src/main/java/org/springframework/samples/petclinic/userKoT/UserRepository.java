package org.springframework.samples.petclinic.userKoT;

import org.springframework.data.repository.CrudRepository;

/**
* @author Sara Cruz
* @author Rosa Molina
*/

public interface UserRepository extends CrudRepository<User, String>{
    
}
