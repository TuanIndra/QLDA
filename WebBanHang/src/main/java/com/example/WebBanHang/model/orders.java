package com.example.WebBanHang.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "note")
    private String note;

    @Column(name = "pttt")
    private String pttt;

    @Column(name = "phone")
    private String phone;

    @Column(name = "customer_name")
    private String customerName;

    // Getters and Setters
}

