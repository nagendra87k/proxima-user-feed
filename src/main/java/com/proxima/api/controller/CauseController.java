package com.proxima.api.controller;


import com.proxima.api.exception.ResourceNotFoundException;
import com.proxima.api.model.CauseType;
import com.proxima.api.model.Causes;
import com.proxima.api.model.Photos;
import com.proxima.api.model.User;
import com.proxima.api.payload.ApiResponse;
import com.proxima.api.payload.CauseResponse;
import com.proxima.api.payload.NewCauseRequst;
import com.proxima.api.payload.OrganizationResponse;
import com.proxima.api.repository.CauseRepository;
import com.proxima.api.repository.CauseTypeRepository;
import com.proxima.api.repository.PhotoRepository;
import com.proxima.api.repository.UserRepository;
import com.proxima.api.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cause")
public class CauseController {

    @Autowired
    private CauseRepository causeRepository;

    @Autowired
    private CauseTypeRepository causeTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private PhotoRepository photoRepository;

    @PostMapping(value = "/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<?> createCause(@RequestPart(value = "cover") MultipartFile cover,@RequestPart(value = "photos") MultipartFile[] photos, @RequestParam("title") String title,@RequestParam("description") String description,@RequestParam("location") String location,@RequestParam("email") String email){

        User user = userRepository.findByEmail(email)

                .orElseThrow(()->new ResourceNotFoundException("Email", "id", email));

        Boolean isAvailable = userRepository.existsByEmail(email);
        Causes causes = new Causes();

        causes.setTitle(title);
        causes.setDescription(description);
        causes.setLocation(location);
        causes.setEmail(email);

        if (isAvailable){
            String coustomName;
            Causes causes1 = causeRepository.save(causes);
            Long causeId = causes1.getId();
            Long orgId = user.getId();
            coustomName = orgId+"-"+causeId+"-cover";

            String fileName = fileStorageService.storeFile(cover,coustomName);
            causes.setCover(fileName);



            String uploadedFileName = Arrays.stream(photos).map(x -> x.getOriginalFilename())
                    .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));
            if (StringUtils.isEmpty(uploadedFileName)) {
                return new ResponseEntity<String>("please select files!", HttpStatus.OK);
            }
            if (photos.length==0) {
                return new ResponseEntity<String>("please select files!", HttpStatus.OK);
            }

            List<Photos> fileNames = new ArrayList<>();
            int i=1;
            for(MultipartFile photo : photos) {
                Photos photos1 = new Photos();
                coustomName = orgId+"-"+causeId+"-"+ i;
                fileStorageService.storeFile(photo,coustomName);
                photos1.setName(coustomName);
                fileNames.add(photos1);
                photos1.setName(coustomName);
                i++;
            }
            causes.setPhotos(fileNames);
            causeRepository.save(causes);
            return ResponseEntity.ok(new ApiResponse(true, "Cause created successfully"));

        }else{
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Email id "+email+" doesn't exist."));
        }

    }

    @GetMapping("/casueType")
    public ResponseEntity getCauseType(){
        List<CauseType> causeType = causeTypeRepository.findAll();
        return ResponseEntity.ok().body(causeType);
    }

    @PostMapping("/organization")
    public Map<String, Object> getOrganizationProfile(@RequestParam(value = "email") String email){
        Map<String, Object> orgnization = new HashMap();

        User user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("Email", "id", email));
       User users = userRepository.findUserById(user.getId());
        OrganizationResponse organizationResponse = new OrganizationResponse();
        organizationResponse.setId(users.getId());
        organizationResponse.setAbout(users.getAbout());
        organizationResponse.setName(users.getName());
        organizationResponse.setNationality(users.getNationality());
        organizationResponse.setTags(users.getTags());
        organizationResponse.setFollowers("22K");
        organizationResponse.setTotal_raised(2400);
        organizationResponse.getProfile_pic(users.getFileName());

        List<Causes> causes = causeRepository.findByEmail(email);


//        causes.forEach(
//
//                cause->causeResponse.setId(cause.getId()),
//                cause1->causeResponse.setCover(causes.)
//        );
//        for (Causes causes1 : causes) {
//            CauseResponse causeResponse = new CauseResponse();
//            causeResponse.setId(causes.);
//            causeResponse.setCover(causes.);
//            causeResponse.setTitle(causes.getTitle());
//            causeResponse.setAmount("2000");
//        }



        orgnization.put("organization",organizationResponse);
        orgnization.put("causes",causes);
        return orgnization;
    }

}
