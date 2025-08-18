package com.ubbackend.DTO;

public class UserEntityDTO {
    private String name;
    private String email;
    private String password;
    private Long dni;
    private Long accessCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getDni() {
        return dni;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }

    public Long getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(Long accessCode) {
        this.accessCode = accessCode;
    }
}
