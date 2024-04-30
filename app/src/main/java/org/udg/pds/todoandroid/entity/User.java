package org.udg.pds.todoandroid.entity;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Created by imartin on 12/02/16.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id", scope = User.class)
public class User implements Serializable {
    public Long id;
    public String username;
    public String name;
    public String country;
    public String email;
    public String phoneNumber;
    public String aboutMe;
    public Double wallet;

    public User() {}

    public User(String username, String name, Long id){
        this.name = name;
        this.username = username;
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public String getAboutMe() {
        return aboutMe;
    }

    public Double getWallet() {
        return wallet;
    }
}
