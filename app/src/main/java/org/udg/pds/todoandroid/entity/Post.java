package org.udg.pds.todoandroid.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.List;

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

    @JsonProperty("tipusServei")
    public Servei tipusServei;

    private List<String> images;

    public Post() {}

    public Post(String titol, String descripcio, Double preu, User user, Servei tipusServei) {
        this.titol = titol;
        this.descripcio = descripcio;
        this.preu = preu;
        this.user = user;
        this.tipusServei = tipusServei;
    }

    public Long getId() { return id; }

    public String getTitol() { return titol; }

    public String getDescripcio() { return descripcio; }

    public Double getPreu() { return preu; }

    public User getUser() { return user; }

    public Servei getServei() { return tipusServei; }

    public List<String> getImages() { return images; }

    public void setUser(User user) { this.user = user; }

    public void setImages(List<String> images) { this.images = images; }
}
