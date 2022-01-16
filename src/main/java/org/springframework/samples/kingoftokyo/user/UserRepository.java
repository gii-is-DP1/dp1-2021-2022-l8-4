package org.springframework.samples.kingoftokyo.user;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

/**
* @author Sara Cruz
* @author Rosa Molina
* @author Carlos Varela Soult
* @author Ricardo Nadal Garcia
*/
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer>{

    /**
     * @param pageable
     * @return Obtains a page of users
     */
    @Query(value="SELECT u FROM User u")
    Page<User> findAllUsersByPage(Pageable pageable);

    /**
     * @param username
     * @return Obtains currentUser from username
     */
    @Query(value="SELECT u FROM User u WHERE u.username=?1")
    User findCurrentUser(String username);
    
}
