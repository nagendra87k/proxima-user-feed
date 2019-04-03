package com.proxima.ngo.api.payload;

import com.proxima.ngo.api.model.PostImages;

import java.util.ArrayList;
import java.util.List;

public class PostFeedResponse {

    private Long id;
    private String description;
    private List<PostImages> postImages = new ArrayList<>();

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

    public List<PostImages> getPostImages() {
        return postImages;
    }

    public void setPostImages(List<PostImages> postImages) {
        this.postImages = postImages;
    }
}
