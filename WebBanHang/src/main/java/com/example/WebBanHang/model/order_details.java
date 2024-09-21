package com.example.WebBanHang.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_details")
public class order_details {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private orders order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private products product;

    // Getters and Setters
}
