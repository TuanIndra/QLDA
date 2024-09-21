package com.example.WebBanHang.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;
@Data
@Entity
@Table(name = "role")
public class role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<user> users;

    // Getters and Setters
}
