package org.springframework.samples.petclinic.userKoT;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

/**
* @author Sara Cruz
* @author Rosa Molina
* @author Carlos Varela Soult
*/

public interface UserRepositoryKoT extends CrudRepository<UserKoT, Integer>{

    // H9 - Listado de usuarios registrados por paginación
    @Query(value="SELECT * FROM UsersKoT", nativeQuery=true)
    Page<UserKoT> findUserByPage(Pageable pageable);

    // Obtener currentUser a partir de username
    @Query(value="SELECT * FROM USERSKOT WHERE username=?1", nativeQuery=true)
    UserKoT findCurrentUser(String username);
    
}
