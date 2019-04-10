package com.proxima.ngo.api.payload;

import com.proxima.ngo.api.model.Photos;

import java.time.Instant;
import java.util.List;

public class CauseFeedResponse {

    private Long id;
    private String title;
    private String description;
    private List<Photos> photos;
    private String amount;
    private Instant createdAt;
    private String organizationName = "Wild";
    private String Feedtype = "Cause";

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

    public List<Photos> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photos> photos) {
        this.photos = photos;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getFeedtype() {
        return Feedtype;
    }

    public void setFeedtype(String feedtype) {
        Feedtype = feedtype;
    }
}
