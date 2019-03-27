package com.proxima.ngo.api.model;

import com.proxima.ngo.api.model.audit.DateAudit;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Entity
public class Causes extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cover;
    private String title;
    private String email;
    @Lob
    @Column(length=1000000)
    private String description;
    private String location;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Photos> photos;

//    @OneToMany(mappedBy = "causes", cascade = CascadeType.ALL)
//    private Set<Posts> posts;

//    @OneToMany(mappedBy = "causes", cascade = CascadeType.ALL)
//    private Set<CauseType> types;



    public Causes() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Photos> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photos> photos) {
        this.photos = photos;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public Set<CauseType> getTypes() {
//        return types;
//    }
//
//    public void setTypes(Set<CauseType> types) {
//        this.types = types;
//    }
}
