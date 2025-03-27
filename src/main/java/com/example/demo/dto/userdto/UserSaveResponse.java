package com.example.demo.dto.userdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserSaveResponse{


//    @NotNull(message = "first name must not be null ")
//    @NotBlank(message = "first name must not be blank ")
    String firstname;
//    @NotNull(message = " last name must not be null ")
//    @NotBlank(message = "last name  must not be blank ")
    String lastname;
//    @NotNull(message = "user name must not be null ")
//    @NotBlank(message = "user name must not be blank ")
    String username;
//    @NotNull(message = "phoneNumber must not be null ")
//    @NotBlank(message = "phoneNumber must not be blank ")
    String phoneNumber;
//    @NotNull(message = "password must not be null ")
//    @NotBlank(message = "password  must not be blank ")
    String password;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
