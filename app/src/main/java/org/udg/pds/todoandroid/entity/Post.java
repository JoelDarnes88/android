package org.udg.pds.todoandroid.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.List;

/**
 * Created by imartin on 12/02/16.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id", scope = Post.class)
public class Post {
    public Long id;

    @JsonProperty("titol")
    public String titol;

    @JsonProperty("descripcio")
    public String descripcio;

    @JsonProperty("preu")
    public Double preu;

    @JsonProperty("user")
    public User user;

    private List<String> images;

    public Post() {}

    public Post(String title, String description, double price, User user) {
        this.titol = title;
        this.descripcio = description;
        this.preu = price;
        this.user = user;
    }

    public Post(String title, String description, double price) {
        this.titol = title;
        this.descripcio = description;
        this.preu = price;
    }


    // Getters y Setters
    public Long getId() {
        return id;
    }

    public String getTitol() {
        return titol;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public Double getPreu() {
        return preu;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
