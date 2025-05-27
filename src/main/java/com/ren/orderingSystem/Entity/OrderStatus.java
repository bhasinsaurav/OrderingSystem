package com.ren.orderingSystem.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "order_status")
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int status_id;

    @Column(name = "status_name" , nullable = false)
    private String status_Name;

    @OneToMany(mappedBy = "orderStatus")
    private Set<Order> orders = new HashSet<>();
}
