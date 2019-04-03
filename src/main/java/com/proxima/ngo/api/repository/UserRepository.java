package com.proxima.ngo.api.repository;

import com.proxima.ngo.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByOtp(int otp);
    User findUserById(Long id);

    @Query( value = "SELECT c FROM User c WHERE c.email=?1" )
    User findByEmailOrId(@Param("email") String email);
}
