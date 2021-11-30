package org.springframework.samples.petclinic.user;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

/**
* @author Sara Cruz
* @author Rosa Molina
* @author Carlos Varela Soult
*/

public interface UserRepository extends PagingAndSortingRepository<User, Integer>{

    /**
     * @param pageable
     * @return Obtains a page of users
     */
    @Query(value="SELECT * FROM USERS", nativeQuery=true)
    Page<User> findAllUsersByPage(Pageable pageable);

    /**
     * @param username
     * @return Obtains currentUser from username
     */
    @Query(value="SELECT * FROM USERS WHERE username=?", nativeQuery=true)
    User findCurrentUser(String username);
    
}
