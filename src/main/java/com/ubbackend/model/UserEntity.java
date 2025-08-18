package com.ubbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "users ")
public class UserEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String password;
    private Long dni;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = RolEntity.class, cascade = CascadeType.PERSIST)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonManagedReference
    private Set<RolEntity> roles;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "creator")
    @JsonBackReference
    private Set<AccessCodeEntity> accessCodeEntityList;

    public int getId() {
        return id;
    }

    public Long getDni() {
        return dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }

    public Set<RolEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RolEntity> roles) {
        this.roles = roles;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String username) {
        this.email = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<AccessCodeEntity> getAccessCodeEntityList() {
        return accessCodeEntityList;
    }

    public void setAccessCodeEntityList(Set<AccessCodeEntity> accessCodeEntityList) {
        this.accessCodeEntityList = accessCodeEntityList;
    }
}
