package com.ren.orderingSystem.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "restaurant")
@EqualsAndHashCode(exclude = {"user", "restaurantAddress", "menuItems", "orders"})
@ToString(exclude = {"user", "restaurantAddress", "menuItems", "orders"})
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "restaurant_id", updatable = false, nullable = false)

    private long restaurantId;

    @Column(nullable = false, name = "contact_number")
    private String contactNumber;

    @Column(nullable = false, name = "restaurant_name")
    private String restaurantName;

    @Column(nullable = false, name = "url_slug", unique = true)
    private String slug;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RestaurantAddress> restaurantAddress = new HashSet<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MenuItem> menuItems = new HashSet<>();

    @OneToMany(mappedBy = "restaurant")
    private Set<Order> orders= new HashSet<>();
}
