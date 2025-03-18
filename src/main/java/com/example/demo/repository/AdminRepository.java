package com.example.demo.repository;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository {
    @Modifying
    @Transactional
    @Query("update User u set u.isAccepted=true where u.id=:id")
    void acceptUserById(@Param("id") Long userId);
}
