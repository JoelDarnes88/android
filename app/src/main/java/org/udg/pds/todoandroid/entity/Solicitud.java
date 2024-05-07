package org.udg.pds.todoandroid.entity;
import java.util.List;

public class Solicitud {
    private Long id;
    private String estat;
    private User usuari;
    private Pagament pagament;
    private List<Missatge> missatges;

    public Solicitud() {}

    // Getters i setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstat() {
        return estat;
    }

    public void setEstat(String estat) {
        this.estat = estat;
    }

    public User getUsuari() {
        return usuari;
    }

    public void setUsuari(User usuari) {
        this.usuari = usuari;
    }

    public Pagament getPagament() {
        return pagament;
    }

    public void setPagament(Pagament pagament) {
        this.pagament = pagament;
    }

    public List<Missatge> getMissatges() {
        return missatges;
    }

    public void setMissatges(List<Missatge> missatges) {
        this.missatges = missatges;
    }
}
