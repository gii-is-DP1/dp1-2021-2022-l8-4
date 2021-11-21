package org.springframework.samples.petclinic.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

/**
* @author Sara Cruz
* @author Rosa Molina
* @author Carlos Varela Soult
*/

public interface UserRepository extends CrudRepository<User, Integer>{

    // H9 - Listado de usuarios registrados por paginación
    @Query(value="SELECT * FROM USERS", nativeQuery=true)
    Page<User> findUserByPage(Pageable pageable);

    // Obtener currentUser a partir de username
    @Query(value="SELECT * FROM USERS WHERE username=?", nativeQuery=true)
    User findCurrentUser(String username);
    
}
