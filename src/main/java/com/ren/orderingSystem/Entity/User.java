    package com.ren.orderingSystem.Entity;

    import jakarta.persistence.*;
    import lombok.Data;
    import lombok.EqualsAndHashCode;

    import java.time.LocalDateTime;
    import java.util.Set;
    import java.util.UUID;

    @Entity
    @Table(name = "users")
    @Data
    @EqualsAndHashCode(exclude = {"restaurant" ,"customer"})
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        @Column(name = "user_id", updatable = false, nullable = false)
        private UUID userId;

        @Column(name = "first_name")
        private String firstName;

        @Column(name = "last_name")
        private String lastName;

        @Column(name = "user_timestamp")
        private LocalDateTime userTimestamp;

        @Column(name = "username", unique = true, nullable = false)
        private String userName;

        @Column(name = "email", unique = true, nullable = false)
        private String email;

        @Column(name = "password", nullable = false)
        private String password;

        @Column(name = "phone_number")
        private String phoneNumber;

        @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private Customer customer;

        @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private Restaurant restaurant;

    }
