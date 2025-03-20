package com.example.demo.repository;

import com.example.demo.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Long getUserIdByUsername(String username);

    @Query("select u.password from User u where u.username =:username")
    Optional<String> getPasswordByUsername(@Param("username") String username);

    boolean existsByUsername(@Param("username") String username);

    @Query("select case when u.password=:password then true else false end from User u where u.username=:username")
    boolean isCorrectPassword(@Param("username") String username,@Param("password") String password);

    @Query("select u from User u where u.id =:id and u.isAccepted=true   ")
    Optional<User> isAcceptedByUserId(@Param("id") Long userId);

    @Modifying
    @Transactional
    @Query("update User u set u.isAccepted=true where u.id=:id")
    void acceptUserById(@Param("id") Long userId);

    @Query("select u from User u where u.isAccepted=false ")
    List<User> getNotAcceptedUsers();
}
