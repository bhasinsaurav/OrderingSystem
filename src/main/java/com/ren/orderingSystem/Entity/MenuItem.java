package com.ren.orderingSystem.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "menuitem")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "item_id", nullable = false, updatable = false)
    private long itemId;

    @Column(name = "description")
    private String description;

    @Column(name= "price")
    private BigDecimal price;

    @Lob
    private byte[] itemImage;

    @Column(name="menuitem_category")
    private String category;

    @Column(name = "item_name", nullable = false, unique = true)
    private String itemName;

    @Column(name = "isAvailable")
    private Boolean isAvailable;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "menuItem")
    private List<OrderItems> orderItems = new ArrayList<>();

}
