package org.udg.pds.todoandroid.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.io.Serializable;

public class UserModify{
    @JsonProperty("username")
    public String username;

    @JsonProperty("name")
    public String name;

    @JsonProperty("country")
    public String country;

    @JsonProperty("email")
    public String email;

    @JsonProperty("phone_number")
    public String phoneNumber;

    @JsonProperty("password")
    public String password;

    @JsonProperty("about_me")
    public String aboutMe;
    @JsonProperty("payment_method")
    public String paymentMethod;

    public UserModify(String username, String name, String country, String email, String phoneNumber, String password, String aboutMe, String paymentMethod) {
        this.username = username;
        this.name = name;
        this.country = country;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.aboutMe = aboutMe;
        this.paymentMethod = paymentMethod;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}
