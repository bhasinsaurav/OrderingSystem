package com.ren.orderingSystem.repository;

import com.ren.orderingSystem.Entity.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @EntityGraph(attributePaths = {"menuItems", "restaurantAddress"})
    Optional<Restaurant> findByUser_UserId(UUID userId);
}
