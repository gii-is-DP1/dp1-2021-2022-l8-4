package org.springframework.samples.petclinic.userKoT;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;

/**
* @author Sara Cruz
* @author Rosa Molina
*/

public interface UserRepository extends CrudRepository<User, String>{

    
}
