package com.proxima.ngo.api.payload;

import java.time.Instant;

public class UserProfile {

    private Long id;
    private String email;
    private String name;
    private String username;
    private String nationality;
    private Long mobile;

    public UserProfile() {
    }

    public UserProfile(String email, String name, String username, String nationality, Long mobile) {
        this.email = email;
        this.name = name;
        this.username = username;
        this.nationality = nationality;
        this.mobile = mobile;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
