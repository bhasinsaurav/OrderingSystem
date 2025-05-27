package com.ren.orderingSystem.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "restaurant_address")
public class Restaurant_Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "restaurant_address_id", updatable = false, nullable = false)
    private long restaurantAddressId;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street_name")
    private String street_Name;

    @Column(name = "street_address")
    private String street_Address;

    @Column(name = "pin_code")
    private String pin_Code;

    @Column(name = "province")
    private String province;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
