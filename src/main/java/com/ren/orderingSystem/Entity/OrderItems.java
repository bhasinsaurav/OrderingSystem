package com.ren.orderingSystem.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@Table(name= "order_items")
@EqualsAndHashCode(exclude = {"menuItem", "order"})
@ToString(exclude = {"menuItem", "order"})
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "item_id", nullable = false, updatable = false)
    private long itemId;

    @Column(name = "quantity", nullable = false)
    private long quantity;

    @ManyToOne
    @JoinColumn(name = "menuitem_id")
    private MenuItem menuItem;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
