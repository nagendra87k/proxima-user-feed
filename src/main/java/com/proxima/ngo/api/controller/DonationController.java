package com.proxima.ngo.api.controller;

import com.proxima.ngo.api.model.Donations;
import com.proxima.ngo.api.payload.ApiResponse;
import com.proxima.ngo.api.payload.DonationRequest;
import com.proxima.ngo.api.repository.DonationRepository;
import com.proxima.ngo.api.service.DonationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/donation")
public class DonationController {

    private static final Logger logger = LoggerFactory.getLogger(DonationController.class);

    @Autowired
    private DonationService donationService;


    @PostMapping(value = "/add")
    public ResponseEntity<?> createPoll(@Valid @RequestBody DonationRequest donationRequest) {
        Donations donations = donationService.createDonation(donationRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{donationId}").buildAndExpand(donations.getId()).toUri();

//        return ResponseEntity.created(location).body(new ApiResponse(true, "Donation Added Successfully"));
        return ResponseEntity.ok().body(new ApiResponse(true, "Donation Added Successfully"));
    }



}
