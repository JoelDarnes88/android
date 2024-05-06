package org.udg.pds.todoandroid.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by imartin on 12/02/16.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id", scope = Message.class)
public class Message implements Serializable {

    private Long id;

    private String content;

    private LocalDateTime timestamp;

    private Long senderId;

    public Message(){}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getSender() {
        return senderId;
    }

    public void setSender(Long senderId) {
        this.senderId = senderId;
    }

}
