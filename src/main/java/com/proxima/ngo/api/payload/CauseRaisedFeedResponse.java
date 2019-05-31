package com.proxima.ngo.api.payload;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.proxima.ngo.api.model.User;

@JsonAutoDetect
public class CauseRaisedFeedResponse{

    private String title;
    private String amount="$24";
    private User user;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
