package com.proxima.api.controller;


import com.proxima.api.model.CauseType;
import com.proxima.api.model.Causes;
import com.proxima.api.payload.ApiResponse;
import com.proxima.api.payload.NewCauseRequst;
import com.proxima.api.repository.CauseRepository;
import com.proxima.api.repository.CauseTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cause")
public class CauseController {

    @Autowired
    private CauseRepository causeRepository;

    @Autowired
    private CauseTypeRepository causeTypeRepository;

    @PostMapping("/create")
    public ResponseEntity createCause(@Valid @RequestBody  NewCauseRequst newCauseRequst){

       Causes causes = new Causes();
       causes.setTitle(newCauseRequst.getTitle());
       causes.setDescription(newCauseRequst.getDescription());
       causes.setLocation(newCauseRequst.getLocation());
       causeRepository.save(causes);
       return ResponseEntity.ok(new ApiResponse(true, "Cause create successfully"));
    }

    @GetMapping("/casueType")
    public ResponseEntity getCauseType(){
        List<CauseType> causeType = causeTypeRepository.findAll();
        return ResponseEntity.ok().body(causeType);
    }

    @GetMapping("/organization/{email}")
    public ResponseEntity getOrganizationProfile(){

        return ResponseEntity.ok(new ApiResponse(null,null));
    }
}
