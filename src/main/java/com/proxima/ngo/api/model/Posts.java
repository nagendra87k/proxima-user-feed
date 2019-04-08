package com.proxima.ngo.api.model;

import com.proxima.ngo.api.model.audit.DateAudit;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity

public class Posts extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String primaryPhoto;

    @ManyToOne()
    @JoinColumn
    private Causes causes;

    @OneToMany( mappedBy = "posts",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Images> postImaes = new ArrayList<>();

    @Column(length = 500)
    private String description;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrimaryPhoto() {
        return primaryPhoto;
    }

    public void setPrimaryPhoto(String primaryPhoto) {
        this.primaryPhoto = primaryPhoto;
    }

    public Causes getCauses() {
        return causes;
    }

    public void setCauses(Causes causes) {
        this.causes = causes;
    }

    public List<Images> getPostImaes() {
        return postImaes;
    }

    public void setPostImaes(List<Images> postImaes) {
        this.postImaes = postImaes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
