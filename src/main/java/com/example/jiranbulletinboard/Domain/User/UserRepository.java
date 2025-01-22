package com.example.jiranbulletinboard.Domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByEmail(String email);
    @Query("SELECT u.userName FROM UserEntity u WHERE u.userId = :userId")
    String findUserNameById(@Param("userId") Integer userId);
}
