package com.proxima.ngo.api.controller;


import com.proxima.ngo.api.exception.ResourceNotFoundException;
import com.proxima.ngo.api.model.CauseType;
import com.proxima.ngo.api.model.Causes;
import com.proxima.ngo.api.model.Photos;
import com.proxima.ngo.api.model.User;
import com.proxima.ngo.api.payload.*;
import com.proxima.ngo.api.payload.*;
import com.proxima.ngo.api.repository.CauseRepository;
import com.proxima.ngo.api.repository.CauseTypeRepository;
import com.proxima.ngo.api.repository.PhotoRepository;
import com.proxima.ngo.api.repository.UserRepository;
import com.proxima.ngo.api.service.FileStorageService;
import com.proxima.ngo.api.util.ObjectMapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.swagger2.mappers.ModelMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cause")
public class CauseController {

    private static final Logger logger = LoggerFactory.getLogger(CauseController.class);

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

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<?> createCause(@RequestPart(value = "cover") MultipartFile cover,@RequestPart(value = "photos") MultipartFile[] photos, @RequestParam("title") String title,@RequestParam("description") String description,@RequestParam("location") String location,@RequestParam("email") String email){

        User user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("Email", "id", email));
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
            causes.setCover(fileStorageService.storeFile(cover,coustomName));
            String uploadedFileName = Arrays.stream(photos).map(x -> x.getOriginalFilename()).filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));
            if (StringUtils.isEmpty(uploadedFileName)) {
                return new ResponseEntity<String>("please select files!", HttpStatus.OK);
            }
            if (photos.length==0) {
                return new ResponseEntity<String>("please select files!", HttpStatus.OK);
            }
            List<Photos> fileNames = new ArrayList<>();
            int i=1;
            for(MultipartFile photo : photos) {
                String extn = photo.getOriginalFilename().substring( photo.getOriginalFilename().lastIndexOf(".") + 1);
                Photos photos1 = new Photos();
                String coustomFileName;
                coustomFileName = orgId+"-"+causeId+"-"+ i;
                photos1.setName(fileStorageService.storeFile(photo,coustomFileName));
                photos1.setType(extn);
                fileNames.add(photos1);
//                photos1.setName(coustomName);
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
    public ResponseEntity<Map<String,Object>> getOrganizationProfile(@RequestBody UserProfile userProfile){

        Map<String, Object> organization = new HashMap();
        User user = userRepository.findByEmail(userProfile.getEmail()).orElseThrow(()->new ResourceNotFoundException("Email", "id", userProfile.getEmail()));
        User users = userRepository.findUserById(user.getId());
        OrganizationResponse organizationResponse = new OrganizationResponse();
        organizationResponse.setId(users.getId());
        organizationResponse.setProfile_pic(users.getFileName());
        organizationResponse.setName(users.getName());
        organizationResponse.setNationality(users.getNationality());
        organizationResponse.setTags(users.getTags());
        organizationResponse.setFollowers("22K");
        organizationResponse.setTotal_raised(2400);
        List<Causes> causes = causeRepository.findByEmail(userProfile.getEmail());
        organization.put("organization",organizationResponse);
        CauseResponse causeResponse = new CauseResponse();
        organization.put("causes",causes);
        return ResponseEntity.ok().body(organization);

    }

    @GetMapping("/details")
    public ResponseEntity getCauseDetailsById(@RequestParam(value = "id", required = true) Long id){

        Optional<Causes> causes = causeRepository.findById(id);
        return ResponseEntity.ok().body(causes);
    }

    @PostMapping(value = "/update",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<?> updateCause(@RequestBody @RequestParam("id") Long id,@RequestPart(value = "cover") MultipartFile cover,@RequestPart(value = "photos") MultipartFile[] photos, @RequestParam("title") String title,@RequestParam("description") String description,@RequestParam("location") String location,@RequestParam("email") String email,@RequestParam("type") CauseType type){

        User user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("Email", "id", email));

        Causes causes = new Causes();
        causes.setId(id);
        causes.setTitle(title);
        causes.setDescription(description);
        causes.setLocation(location);
        causes.setEmail(email);
//        causes.setTypes(type);
        String coustomName;
        Long causeId = id;
        Long orgId = user.getId();
        coustomName = orgId+"-"+causeId+"-cover";
        causes.setCover(fileStorageService.storeFile(cover,coustomName));
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
            String extn = photo.getOriginalFilename().substring( photo.getOriginalFilename().lastIndexOf(".") + 1);
            Photos photoRef = new Photos();
            String coustomFileName;
            coustomFileName = orgId+"-"+causeId+"-"+ i;
            photoRef.setName(fileStorageService.storeFile(photo,coustomFileName));
            photoRef.setType(extn);
            fileNames.add(photoRef);
            i++;
        }
        causes.setPhotos(fileNames);
        causeRepository.save(causes);
        return ResponseEntity.ok(new ApiResponse(true, "Cause updated successfully"));
    }
    @DeleteMapping("/delete")
    public ResponseEntity deleteCauseById(@RequestParam(value = "id", required = true) Long id){
        causeRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse(true,"Cause deleted successfully"));
    }

    @GetMapping("/causeList")
    public ResponseEntity<?> getCauseListByOrgranzationId(@RequestParam(value = "email") String email){

        List<Causes> causes = causeRepository.findAllByEmail(email);
        List<CauseListResponse> listResponseList = ObjectMapperUtils.mapAll(causes, CauseListResponse.class);
        return ResponseEntity.ok().body(listResponseList);
    }
    @GetMapping("/loadfile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {

        Resource resource = fileStorageService.loadFileAsResource(fileName);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }
}
