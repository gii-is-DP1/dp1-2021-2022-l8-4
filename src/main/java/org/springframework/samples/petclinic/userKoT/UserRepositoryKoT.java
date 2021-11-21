package org.springframework.samples.petclinic.userKoT;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;

/**
* @author Sara Cruz
* @author Rosa Molina
* @author Carlos Varela Soult
*/

public interface UserRepositoryKoT extends CrudRepository<UserKoT, Integer>{

    // Obtener currentUser a partir de username
    @Query(value="SELECT * FROM USERSKOT WHERE username=?1", nativeQuery=true)
    UserKoT findCurrentUser(String username);
    
}
