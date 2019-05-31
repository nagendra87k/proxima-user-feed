package com.proxima.ngo.api.payload;

import com.proxima.ngo.api.model.Causes;
import com.proxima.ngo.api.model.User;

public class DonationRequest {

    private User user;
    private Causes causes;
    private Long amount;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Causes getCauses() {
        return causes;
    }

    public void setCauses(Causes causes) {
        this.causes = causes;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
