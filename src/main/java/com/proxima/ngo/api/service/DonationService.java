package com.proxima.ngo.api.service;

import com.proxima.ngo.api.model.Donations;
import com.proxima.ngo.api.payload.DonationRequest;
import com.proxima.ngo.api.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class DonationService {
    @Autowired
    private DonationRepository donationRepository;

    public Donations createDonation(DonationRequest donationRequest) {

        Donations donations = new Donations();
        donations.setUser(donationRequest.getUser());
        donations.setCauses(donationRequest.getCauses());
        donations.setAmount(donationRequest.getAmount());
        return donationRepository.save(donations);

//        D poll = new Poll();
//        poll.setQuestion(pollRequest.getQuestion());
//
//        pollRequest.getChoices().forEach(choiceRequest -> {
//            poll.addChoice(new Choice(choiceRequest.getText()));
//        });
//
//        Instant now = Instant.now();
//        Instant expirationDateTime = now.plus(Duration.ofDays(pollRequest.getPollLength().getDays()))
//                .plus(Duration.ofHours(pollRequest.getPollLength().getHours()));
//
//        poll.setExpirationDateTime(expirationDateTime);
//
//        return pollRepository.save(poll);
    }
}
