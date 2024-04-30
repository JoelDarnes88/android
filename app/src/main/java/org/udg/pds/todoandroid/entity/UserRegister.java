package org.udg.pds.todoandroid.entity;

public class UserRegister {
    public String username;
    public String name;
    public String country;

    public String email;
    public String phone_number;
    public String password;


    public UserRegister(String username, String name, String country, String email, String phone_number, String password) {
        this.username = username;
        this.name = name;
        this.country = country;
        this.email = email;
        this.phone_number = phone_number;
        this.password = password;
    }
}
