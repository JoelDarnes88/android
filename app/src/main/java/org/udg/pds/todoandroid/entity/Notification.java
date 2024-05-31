package org.udg.pds.todoandroid.entity;

import java.time.ZonedDateTime;

public class Notification {
    private Long id;
    private String information;
    private ZonedDateTime dateCreation;
    private boolean showed;
    private String reference;
    private boolean referenceBool;

    public Notification(Long id, String information, ZonedDateTime dateCreation, boolean showed, String reference, boolean referenceBool) {
        this.id = id;
        this.information = information;
        this.dateCreation = dateCreation;
        this.showed = showed;
        this.reference = reference;
        this.referenceBool = referenceBool;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public ZonedDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public boolean isShowed() {
        return showed;
    }

    public void setShowed(boolean showed) {
        this.showed = showed;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public boolean isReferenceBool() {
        return referenceBool;
    }

    public void setReferenceBool(boolean referenceBool) {
        this.referenceBool = referenceBool;
    }
}
