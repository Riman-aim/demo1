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

    @Query("select u.password from User u where u.username =:username")
    Optional<String> getPasswordByUsername(@Param("username") String username);

    boolean existsByUsername(@Param("username") String username);

    @Query("select case when u.password=:password then true else false end from User u where u.username=:username")
    boolean isCorrectPassword(@Param("username") String username, @Param("password") String password);

    @Query("select case when u.isAccepted=true then true else false end from User u where u.id=:id")
    boolean isAcceptedByUserId(@Param("id") Long userId);

    @Modifying
    @Transactional
    @Query("update User u set u.isAccepted=true where u.id=:id")
    void acceptUserById(@Param("id") Long userId);

    @Query("select u from User u where u.isAccepted=false ")
    List<User> getNotAcceptedUsers();

    @Query("select u from User u where u.isAddressDeleted=true ")
    List<User> getAddressDeletedUsers();

//    @Query("select case when u.username=:username then true else false end from User u")
//    boolean isDuplicateUsername(@Param("username") String username);

    @Query("SELECT case when exists(select 1 from User u where u.username=:username) then true else false end ")
    boolean isDuplicateUsername(@Param("username") String username);

    @Query("select case when exists (select 1 from User u where u.phoneNumber=:phoneNumber) then true  else false end ")
    boolean isDuplicatePhoneNumber(@Param("phoneNumber") String phoneNumber);

}
