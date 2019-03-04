package com.proxima.api.controller;

import com.proxima.api.exception.AppException;
import com.proxima.api.exception.ResourceNotFoundException;
import com.proxima.api.model.Role;
import com.proxima.api.model.RoleName;
import com.proxima.api.model.User;
//import com.example.polls.payload.*;
import com.proxima.api.payload.*;
import com.proxima.api.repository.PollRepository;
import com.proxima.api.repository.UserRepository;
import com.proxima.api.repository.VoteRepository;
import com.proxima.api.security.UserPrincipal;
import com.proxima.api.service.FileStorageService;
import com.proxima.api.service.PollService;
import com.proxima.api.security.CurrentUser;
import com.proxima.api.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PollRepository pollRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private PollService pollService;

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileStorageService fileStorageService;



    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
        return userSummary;
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {

        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }


    @PostMapping("/user/profile")
    public ResponseEntity registerUser(@RequestBody UserProfile userProfile) {

        Boolean isAvailable = !userRepository.existsByUsername(userProfile.getUsername());
        User user = userRepository.findByEmail(userProfile.getEmail()).orElseThrow(()->new ResourceNotFoundException("Email", "id", userProfile.getEmail()));
        user.setName(userProfile.getName());
        user.setUsername(userProfile.getUsername());
        user.setNationality(userProfile.getNationality());
        user.setMobile(userProfile.getMobile());

        User result;
        if (isAvailable){
             result = userRepository.save(user);
        }else{
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Username "+userProfile.getUsername()+" already exsit."));
        }


        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User profile updated successfully"));
    }

    @PostMapping(value = "/user/profilePic", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileResponse profilePic(@RequestParam(value = "file") MultipartFile file, @RequestParam("email") String email){

        User user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("Email", "id", email));
        Long id = user.getId();
        String coustomName = "user-"+id ;
        String fileName = fileStorageService.storeFile(file,coustomName);


        user.setFileName(fileName);
        userRepository.save(user);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/user/loadfile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }
    @GetMapping("/user/loadfile/{fileName:.+}")
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

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/user/sendOTP")
    public ResponseEntity sendMail(@RequestParam(value = "email") String email) {


        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        int token = generateOTP();
        try {
            helper.setTo(email);
            helper.setText("Token :"+token);
            helper.setSubject("Mail From Proxima");
        } catch (MessagingException e) {
            e.printStackTrace();

        }
            User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Email", "id", email));
            user.setOtp(token);
            userRepository.save(user);
            sender.send(message);
            return new ResponseEntity(new ApiResponse(true,"OTP sent"+email),HttpStatus.OK);
    }

    public int generateOTP(){
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return otp;
    }

    @GetMapping(value = "/user/validateOTP")
    public ResponseEntity validateOTP(@RequestParam(value = "OTP") int OTP){


        Boolean isMechted = userRepository.existsByOtp(OTP);
            if (isMechted){
                return new ResponseEntity(new ApiResponse(true,"OTP Matched."),HttpStatus.ACCEPTED);
            }else {
                return new ResponseEntity(new ApiResponse(false, "OTP Does not matched."), HttpStatus.NOT_FOUND);
            }
    }

    @PostMapping(value = "/user/resetPassword")
    public ResponseEntity resetPassword(@Valid @RequestBody RestResponse restResponse){

        User user = userRepository.findByEmail(restResponse.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "id", restResponse.getEmail()));
        user.setPassword(passwordEncoder.encode(restResponse.getPassword()));
        userRepository.save(user);
        return new ResponseEntity(new ApiResponse(true, "Password reset successfully."), HttpStatus.OK);
    }



//
//    @GetMapping("/users/{username}")
//    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
//
//        long pollCount = pollRepository.countByCreatedBy(user.getId());
//        long voteCount = voteRepository.countByUserId(user.getId());
//
//        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt(), pollCount, voteCount);
//
//        return userProfile;
//    }
//
//    @GetMapping("/users/{username}/polls")
//    public PagedResponse<PollResponse> getPollsCreatedBy(@PathVariable(value = "username") String username,
//                                                         @CurrentUser UserPrincipal currentUser,
//                                                         @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
//                                                         @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
//        return pollService.getPollsCreatedBy(username, currentUser, page, size);
//    }
//
//
//    @GetMapping("/users/{username}/votes")
//    public PagedResponse<PollResponse> getPollsVotedBy(@PathVariable(value = "username") String username,
//                                                       @CurrentUser UserPrincipal currentUser,
//                                                       @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
//                                                       @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
//        return pollService.getPollsVotedBy(username, currentUser, page, size);
//    }

}
