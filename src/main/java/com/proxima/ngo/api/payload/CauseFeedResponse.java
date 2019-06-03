package com.proxima.ngo.api.payload;

import com.proxima.ngo.api.model.Photos;
import com.proxima.ngo.api.model.User;

import java.time.Instant;
import java.util.List;

public class CauseFeedResponse implements Comparable<CauseFeedResponse>{

    private Long id;
    private String title;
    private String description;
    private List<Photos> photos;
    private String amount;
    private Instant createdAt;
    private Instant updatedAt;
    private String organizationName = "Wild";
    private int Feedtype = 1;
    private User user;

    private Boolean isUpdated;

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

    public int getFeedtype() {
        return Feedtype;
    }

    public void setFeedtype(int feedtype) {
        Feedtype = feedtype;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getUpdated() {
        return isUpdated;
    }

    public void setUpdated(Boolean updated) {
        isUpdated = updated;
    }

    @Override
    public int compareTo(CauseFeedResponse o) {
        return o.getCreatedAt().compareTo(o.getCreatedAt());
    }
}
