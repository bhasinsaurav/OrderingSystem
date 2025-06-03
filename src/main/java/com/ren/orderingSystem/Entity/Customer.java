package com.ren.orderingSystem.Entity;

import jakarta.persistence.*;
import lombok.Data;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name="customer")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name= "customer_id", updatable = false, nullable = false)
    private long customerId;

    @Column(name = "contact_number", nullable = false)
    private String contactNumber;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CustomerAddress> customerAddresses = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    private Set<Order> orders = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    private List<Cart> carts = new ArrayList<>();



}
