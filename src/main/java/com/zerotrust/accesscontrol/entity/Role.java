package com.zerotrust.accesscontrol.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String roleName;

    // Default constructor
    public Role() {
    }

    // Constructor
    public Role(String roleName) {
        this.roleName = roleName;
    }

    // Getter
    public Long getId() {
        return id;
    }

    // Setter
    public void setId(Long id) {
        this.id = id;
    }

    // Getter
    public String getRoleName() {
        return roleName;
    }

    // Setter
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}