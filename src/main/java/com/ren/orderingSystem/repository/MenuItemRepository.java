package com.ren.orderingSystem.repository;

import com.ren.orderingSystem.Entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
}
