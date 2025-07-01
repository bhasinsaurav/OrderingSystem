package com.ren.orderingSystem.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@Table(name = "restaurant_address")
@EqualsAndHashCode(exclude = {"restaurant"})
@ToString(exclude = {"restaurant"})
public class RestaurantAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "restaurant_address_id", updatable = false, nullable = false)
    private long restaurantAddressId;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "pin_code")
    private String pinCode;

    @Column(name = "province")
    private String province;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
