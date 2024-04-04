package org.udg.pds.todoandroid.entity;

/**
 * Created by imartin on 13/02/17.
 */
public class UserRegister {
    public String username;
    public String password;
    public String email;

    public UserRegister(String username, String password, String email) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
