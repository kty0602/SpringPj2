package com.sparta.second.repository;

import com.sparta.second.entity.Reply;
import com.sparta.second.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.userId = :userId AND u.deleteStatus = false")
    Optional<User> getUserByUserId(@Param("userId") Long userId);

    @Query("SELECT u FROM User u WHERE u.deleteStatus = false")
    List<User> findAllActiveUsers();

}
