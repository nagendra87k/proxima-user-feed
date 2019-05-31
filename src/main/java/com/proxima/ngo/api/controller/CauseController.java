package com.proxima.ngo.api.controller;


import com.proxima.ngo.api.exception.AppException;
import com.proxima.ngo.api.exception.ResourceNotFoundException;
import com.proxima.ngo.api.model.*;
import com.proxima.ngo.api.payload.*;
import com.proxima.ngo.api.repository.*;
import com.proxima.ngo.api.service.FileStorageService;
import com.proxima.ngo.api.util.ObjectMapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private PostRepository postRepository;

    @Autowired
    private PhotoRepository photoRepository;



    @PostMapping(value = "/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<?> createCause(@RequestPart(value = "cover") MultipartFile cover,@RequestPart(value = "photos") MultipartFile[] photos, @RequestParam("title") String title,@RequestParam("description") String description,@RequestParam("location") String location, @RequestParam("email") String email, @RequestParam("type_id") Long typeId){

        User user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("Email", "id", email));
        Boolean isAvailable = userRepository.existsByEmail(email);
        Causes causes = new Causes();
        causes.setTitle(title);
        causes.setDescription(description);
        causes.setLocation(location);
        causes.setEmail(email);

        user.setId(user.getId());
        causes.setUser(user);
        causes.setUpdated(false);

        Type type = causeTypeRepository.findAllById(typeId).orElseThrow(()-> new AppException("Cause type not set."));
        type.setId(type.getId());
        causes.setType(type);

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
        List<Type> type = causeTypeRepository.findAll();
        return ResponseEntity.ok().body(type);
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
    public ResponseEntity<?> getCauseDetailsById(@RequestParam(value = "id", required = true) Long id){

        Map<String,Object> data = new HashMap();
        List<Type> type = causeTypeRepository.findAll();
        Optional<Causes> causes = causeRepository.findById(id);
        data.put("CauseType", type);
        data.put("CauseDetails",causes);
        return ResponseEntity.ok().body(data);
    }

    @PostMapping(value = "/update",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<?> updateCause(@RequestBody @RequestParam("id") Long id,@RequestPart(value = "cover") MultipartFile cover,@RequestPart(value = "photos") MultipartFile[] photos, @RequestParam("title") String title,@RequestParam("description") String description,@RequestParam("location") String location,@RequestParam("email") String email, @RequestParam("type_id") Long typeId){

        User user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("Email", "id", email));

        Causes causes = new Causes();
        causes.setId(id);
        causes.setTitle(title);
        causes.setDescription(description);
        causes.setLocation(location);
        causes.setEmail(email);
        user.setId(user.getId());
        causes.setUser(user);
        causes.setUpdated(true);
        Type type = causeTypeRepository.findAllById(typeId).orElseThrow(()-> new AppException("Cause type not set."));
        type.setId(type.getId());
        causes.setType(type);
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
    public ResponseEntity<?> getCauseListByOrgranzationId(@RequestBody @RequestParam(value = "email") String email){

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

    @PostMapping(value = "/create-post",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<?> createPost(@RequestBody @RequestParam(value = "cause_id", required = true) Long cause_id, @RequestPart(value = "primary_photo") MultipartFile primary_photo,@RequestPart(value = "images") MultipartFile[] images, @RequestParam("description") String description){

        Boolean isAvailable = causeRepository.existsById(cause_id);
       // Causes causes = causeRepository.findCausesById(cause_id);
        Causes causes = causeRepository.findCausesById(cause_id);
        User org = userRepository.findByEmailOrId(causes.getEmail());
        Posts posts = new Posts();
        posts.setDescription(description);
        causes.setId(causes.getId());
        posts.setCauses(causes);

        if (isAvailable){
            Posts postsData = postRepository.save(posts);
            Long postId = postsData.getId();
            String coustomName = org.getId()+"-"+cause_id+"-"+postId+"-primary";
            posts.setPrimaryPhoto(fileStorageService.storeFile(primary_photo,coustomName));
            String uploadedFileName = Arrays.stream(images).map(x -> x.getOriginalFilename()).filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));
            if (StringUtils.isEmpty(uploadedFileName)) {
                return new ResponseEntity<String>("please select files!", HttpStatus.OK);
            }
            if (images.length==0) {
                return new ResponseEntity<String>("please select files!", HttpStatus.OK);
            }
            List<Images> imagesList = new ArrayList<>();
            int i=1;
            for(MultipartFile photo : images) {
                String extn = photo.getOriginalFilename().substring( photo.getOriginalFilename().lastIndexOf(".") + 1);
                Images postImages = new Images();
                String coustomFileName = org.getId()+"-"+cause_id+"-"+postId+"-"+ i;
                postImages.setName(fileStorageService.storeFile(photo,coustomFileName));
                postImages.setType(extn);
                imagesList.add(postImages);
                i++;
            }
            posts.setImages(imagesList);
            postRepository.save(posts);
            return ResponseEntity.ok(new ApiResponse(true, "Post Created successfully"));

        }else{
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Cause id "+cause_id+" doesn't exist."));
        }
    }

    @GetMapping(value = "/feeds")
    @Transactional
    public ResponseEntity<?> getOrganizationFeeds(@RequestBody @RequestParam(value = "email", required = true) String email,@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "limit", defaultValue = "5") int limit) {

        Pageable pageable = PageRequest.of(page, limit);
        Boolean isAvailable = userRepository.existsByEmail(email);
        if (isAvailable) {

            List<Object> resultsData = new ArrayList<>();
            List<Causes> causes = causeRepository.findAllByEmail(email,pageable);


            List<CauseFeedResponse> causeFeedResponses = ObjectMapperUtils.mapAll(causes, CauseFeedResponse.class);
            List<Posts> posts = postRepository.findAll();
            List<PostFeedResponse> feedResponseList = ObjectMapperUtils.mapAll(posts, PostFeedResponse.class);


            List<CauseRaisedFeedResponse> weeklyRaisedFeedResponses = ObjectMapperUtils.mapAll(causes, CauseRaisedFeedResponse.class);

            WeeklyRaisedResponse weeklyRaisedResponse = new WeeklyRaisedResponse();
            weeklyRaisedResponse.setCause_raised(weeklyRaisedFeedResponses);

//            List<?> weeklyRaisedFeedList = new ArrayList(Arrays.asList(weeklyRaisedFeedResponses));

            resultsData.addAll(causeFeedResponses);
            resultsData.addAll(feedResponseList);
            resultsData.add(weeklyRaisedResponse);
            return ResponseEntity.ok().body(resultsData);

        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Email id " + email + " doesn't exist."));
        }
    }

    @GetMapping("/posts")
    public ResponseEntity getAllPost(){

        Pageable pageable = PageRequest.of(0, 5);
        List<Posts> posts = postRepository.findAll();
        return ResponseEntity.ok().body(posts);
    }

    @PostMapping("/denotion")
    public ResponseEntity addDenotion(){

        Pageable pageable = PageRequest.of(0, 5);
        List<Posts> posts = postRepository.findAll();

        return ResponseEntity.ok().body(posts);
    }

}
