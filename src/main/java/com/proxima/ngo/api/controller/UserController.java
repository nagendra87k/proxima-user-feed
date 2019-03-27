package com.proxima.ngo.api.controller;

import com.proxima.ngo.api.exception.AppException;
import com.proxima.ngo.api.exception.ResourceNotFoundException;
import com.proxima.ngo.api.model.Role;
import com.proxima.ngo.api.model.RoleName;
import com.proxima.ngo.api.model.User;
import com.proxima.ngo.api.payload.*;
import com.proxima.ngo.api.payload.*;
import com.proxima.ngo.api.repository.RoleRepository;
import com.proxima.ngo.api.repository.UserRepository;
import com.proxima.ngo.api.service.FileStorageService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.Random;

@RestController
@RequestMapping("/organization")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private RoleRepository roleRepository;

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
    public ResponseEntity resetPassword(@Valid @RequestBody RestResponse restResponse) {

        User user = userRepository.findByEmail(restResponse.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "id", restResponse.getEmail()));
        user.setPassword(passwordEncoder.encode(restResponse.getPassword()));
        userRepository.save(user);
        return new ResponseEntity(new ApiResponse(true, "Password reset successfully."), HttpStatus.OK);
    }



    @PostMapping("/user/profile")
    public ResponseEntity registerUser(@RequestBody OrganizationRequest organizationRequest)  {
        logger.info("MSG",organizationRequest);
        User user =  userRepository.findByEmail(organizationRequest.getEmail()).orElseThrow(()->new ResourceNotFoundException("Email", "id", organizationRequest.getEmail()));
        user.setName(organizationRequest.getName());
        user.setNationality(organizationRequest.getNationality());
        user.setMobile(organizationRequest.getMobile());
        user.setTags(organizationRequest.getTags());
        user.setAbout(organizationRequest.getAbout());
        Role userRole = roleRepository.findByName(RoleName.ROLE_ORGANIZATION).orElseThrow(() -> new AppException("User Role not set."));
        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);
        return ResponseEntity.ok().body(new ApiResponse(true, "Organization profile updated successfully"));
    }

    @PostMapping(value = "/user/profilePic", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileResponse profilePic(@RequestParam(value = "file") MultipartFile file, @RequestParam("email") String email){

        User user =  userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("Email", "id", email));
        Long id = user.getId();
        String coustomName = "org-"+id;
        String fileName = fileStorageService.storeFile(file,coustomName);
        user.setFileName(fileName);
        userRepository.save(user);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/organization/user/loadfile/").path(fileName).toUriString();
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
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }
}
