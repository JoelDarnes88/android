package org.udg.pds.todoandroid.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Created by imartin on 12/02/16.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id", scope = Post.class)
public class Post {
    public Long id;
    public String titol;
    public String descripcio;
    public Double preu;
    public Long userId;

    public Post(String title, String description, double price) {
        this.titol = title;
        this.descripcio = description;
        this.preu = price;
    }
}
