package org.springframework.samples.petclinic.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Carlos Varela Soult
 */

public interface UserRepository extends  CrudRepository<User, String>{
	
    // H9 - Listado de usuarios registrados por paginación
    @Query(value="SELECT * FROM Users", nativeQuery=true)
    Page<User> findUserByPage(Pageable pageable);
}
