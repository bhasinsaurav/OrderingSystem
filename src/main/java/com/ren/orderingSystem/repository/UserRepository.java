package com.ren.orderingSystem.repository;

import com.ren.orderingSystem.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUserName(String userName);

    @Query("SELECT u FROM User u JOIN FETCH u.customer WHERE u.userId = :id")
    Optional<User> findByIdWithCustomer(@Param("id") UUID id);
}
