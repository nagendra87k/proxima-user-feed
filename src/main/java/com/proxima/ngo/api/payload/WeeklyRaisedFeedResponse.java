package com.proxima.ngo.api.payload;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class WeeklyRaisedFeedResponse{

    private String title;
    private String amount;

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
}
