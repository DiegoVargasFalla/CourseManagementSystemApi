package com.ubbackend.DTOs;

public class CreateAccessCodeDTO {
    private String emailCreator;
    private String rolType;

    public String getEmailCreator() {
        return emailCreator;
    }

    public void setEmailCreator(String emailCreator) {
        this.emailCreator = emailCreator;
    }

    public String getRolType() {
        return rolType;
    }

    public void setRolType(String rolType) {
        this.rolType = rolType;
    }
}
