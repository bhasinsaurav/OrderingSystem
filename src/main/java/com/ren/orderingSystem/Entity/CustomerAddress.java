package com.ren.orderingSystem.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "customer_address")
public class CustomerAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "customer_address_id", updatable = false, nullable = false)
    private long customerAddressId;

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
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
