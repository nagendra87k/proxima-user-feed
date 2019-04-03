package com.proxima.ngo.api.model;

import lombok.*;

import javax.persistence.*;

@Entity
public class CauseType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn()
    private Causes causes;

    public CauseType() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Causes getCauses() {
        return causes;
    }

    public void setCauses(Causes causes) {
        this.causes = causes;
    }
}
