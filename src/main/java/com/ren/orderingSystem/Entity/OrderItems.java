package com.ren.orderingSystem.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name= "order_items")
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "item_id", nullable = false, updatable = false)
    private long item_Id;

    @Column(name = "quantity", nullable = false)
    private long quantity;

    @ManyToOne
    @JoinColumn(name = "menuitem_id")
    private MenuItem menuItem;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
