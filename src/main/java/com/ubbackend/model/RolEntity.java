package com.ubbackend.model;

import com.ubbackend.enumeration.ERol;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class RolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ERol role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ERol getRole() {
        return role;
    }

    public void setRole(ERol role) {
        this.role = role;
    }
}
