package com.ren.orderingSystem.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Data
@Table(name= "order_items")
@EqualsAndHashCode(exclude = { "order"})
@ToString(exclude = {"order"})
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "item_id", nullable = false, updatable = false)
    private long itemId;

    @Column(name = "quantity", nullable = false)
    private long quantity;

    @Column(name = "item_price", nullable = false)
    private BigDecimal itemPrice;

    @Column(name = "item_total_price", nullable = false)
    private BigDecimal itemTotalPrice;

    @Column(name = "menu_item_id", nullable = false)
    private long menuItemId;

    @Column(name = "menuItemName", nullable = false)
    private String menuItemName;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
