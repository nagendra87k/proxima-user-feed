package com.proxima.ngo.api.payload;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class CauseRaisedFeedResponse{

    private String title;
    private String amount="$24";

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
