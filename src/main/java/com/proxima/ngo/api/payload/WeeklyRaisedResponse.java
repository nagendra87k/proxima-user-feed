package com.proxima.ngo.api.payload;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

@JsonAutoDetect
public class WeeklyRaisedResponse {

    private int total_amount = 1200;
    private List<CauseRaisedFeedResponse> cause_raised;
    private int Feedtype = 3;

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public List<CauseRaisedFeedResponse> getCause_raised() {
        return cause_raised;
    }

    public void setCause_raised(List<CauseRaisedFeedResponse> cause_raised) {
        this.cause_raised = cause_raised;
    }

    public int getFeedtype() {
        return Feedtype;
    }

    public void setFeedtype(int feedtype) {
        Feedtype = feedtype;
    }
}
