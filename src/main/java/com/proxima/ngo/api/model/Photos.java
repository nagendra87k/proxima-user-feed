package com.proxima.ngo.api.model;

import javax.persistence.*;

@Entity
public class Photos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn()
    private Causes causes;

    public Photos() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Causes getCauses() {
        return causes;
    }

    public void setCauses(Causes causes) {
        this.causes = causes;
    }
}
