package com.ubbackend.model;

import com.ubbackend.enumerations.ERol;
import jakarta.persistence.*;

@Entity
@Table(name = "accessCodes")
public class AccessCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long code;

    private Boolean active;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "creator_id")
    private UserEntity creator;

    @Enumerated(EnumType.STRING)
    private ERol roleType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public UserEntity getCreator() {
        return creator;
    }

    public void setCreator(UserEntity creator) {
        this.creator = creator;
    }

    public ERol getRoleType() {
        return roleType;
    }

    public void setRoleType(ERol roleType) {
        this.roleType = roleType;
    }
}
