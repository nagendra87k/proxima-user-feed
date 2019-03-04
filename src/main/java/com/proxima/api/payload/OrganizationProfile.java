package com.proxima.api.payload;

import com.proxima.api.model.Role;

import java.util.Set;

public class OrganizationProfile {

    private Long id;
    private String email;
    private String name;
    private String nationality;
    private Long mobile;
    private String about;
    private String tags;
    private Set<Role> role;

    public OrganizationProfile() {
    }

    public OrganizationProfile(Long id, String email, String name, String nationality, Long mobile, String about, String tags, Set<Role> role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.nationality = nationality;
        this.mobile = mobile;
        this.about = about;
        this.tags = tags;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }
}
