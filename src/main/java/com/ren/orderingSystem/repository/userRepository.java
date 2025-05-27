package com.ren.orderingSystem.repository;

import com.ren.orderingSystem.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface userRepository extends JpaRepository<User, UUID> {
}
