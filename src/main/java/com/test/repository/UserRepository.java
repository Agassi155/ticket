package com.test.repository;

import com.test.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for User Entity
 */
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    /**
     * used to get User by username, used also while trying to check if user could read resource from API
     * @param username
     * @return  username found or Optional empty
     */
    @Query(value = "select * from \"user\"  where username = ?1", nativeQuery = true)
    Optional<User> findByUsername(String username);
}
