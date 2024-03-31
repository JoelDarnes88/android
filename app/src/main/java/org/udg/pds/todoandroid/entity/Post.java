package org.udg.pds.todoandroid.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Created by imartin on 12/02/16.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id", scope = Post.class)
public class Post {
    public Long id;
    @JsonProperty("titol")
    public String titol;
    @JsonProperty("preu")
    public Double preu;
    @JsonProperty("descripcio")
    public String descripcio;
    public User creador;


    public Post() {}


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
}
