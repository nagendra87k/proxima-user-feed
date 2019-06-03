package com.proxima.ngo.api.payload;

import com.proxima.ngo.api.model.Causes;
import com.proxima.ngo.api.model.Images;

import java.time.Instant;
import java.util.List;

public class PostFeedResponse implements Comparable<PostFeedResponse>{

    private Long id;
    private String description;
    private String primaryPhoto;
    private Instant createdAt;
    private List<Images> images;
    private String organizationName = "Wild";
    private int Feedtype = 2;
    private Causes causes;
    private Instant updatedAt;

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

    public int getFeedtype() {
        return Feedtype;
    }

    public void setFeedtype(int feedtype) {
        Feedtype = feedtype;
    }

    public Causes getCauses() {
        return causes;
    }

    public void setCauses(Causes causes) {
        this.causes = causes;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPrimaryPhoto() {
        return primaryPhoto;
    }

    public void setPrimaryPhoto(String primaryPhoto) {
        this.primaryPhoto = primaryPhoto;
    }


    @Override
    public int compareTo(PostFeedResponse o) {
        return o.getCreatedAt().compareTo(o.getCreatedAt());
    }
}
