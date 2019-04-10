package com.proxima.ngo.api.payload;

import com.proxima.ngo.api.model.Images;

import java.time.Instant;
import java.util.List;

public class PostFeedResponse {

    private Long id;
    private String description;
    private Instant createdAt;
    private List<Images> images;
    private String organizationName = "Wild";
    private String Feedtype = "Post";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Images> getImages() {
        return images;
    }

    public void setImages(List<Images> images) {
        this.images = images;
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
