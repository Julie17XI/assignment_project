package com.example.cloudflareassignment.controller;


import com.example.cloudflareassignment.controller.dto.CertDTO;
import com.example.cloudflareassignment.domain.CertDomain;
import com.example.cloudflareassignment.repo.CertRepo;
import lombok.extern.slf4j.Slf4j;
import com.example.cloudflareassignment.controller.dto.UserDTO;
import com.example.cloudflareassignment.domain.UserDomain;
import com.example.cloudflareassignment.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.example.cloudflareassignment.service.CertService;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CertRepo certRepo;

    @Autowired
    private CertService certService;

    @ResponseBody
    @PostMapping("/v1/user")
    public ResponseEntity<ApiResponse<UserDTO>> saveUser(@RequestBody @Validated UserDTO userDTO) {
        log.info("save user {}", userDTO.getName());

        byte[] salt = createSalt();
        UserDomain userDomain = UserDomain.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .salt(salt)
                .password(getSaltedHash(userDTO.getPassword(),createSalt()))
                .build();
        userDomain = userRepo.save(userDomain);
        return ResponseEntity.ok(
                ApiResponse.of(Status.OK, "user saved", UserDTO.from(userDomain)));

    }

    @ResponseBody
    @DeleteMapping("/v1/user/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> deleteUser(@PathVariable Long id){
        UserDomain userDomain = userRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "user not found"));
        log.info("delete user {}", userDomain.getName());

        userRepo.deleteById(id);
        return ResponseEntity.ok(
                ApiResponse.of(Status.OK, "user deleted", UserDTO.from(userDomain)));
    }

    @ResponseBody
    @GetMapping("/v1/user/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUser(@PathVariable Long id){
        UserDomain userDomain = userRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "user not found"));
        log.info("get user {}", userDomain.getName());

        return ResponseEntity.ok(
                ApiResponse.of(Status.OK, "user got", UserDTO.from(userDomain)));
    }

    @ResponseBody
    @PostMapping("/v1/user/{userId}/cert")
    public ResponseEntity<ApiResponse<CertDTO>> saveCert(@PathVariable Long userId, @RequestBody @Validated CertDTO certDTO){
        UserDomain userDomain = userRepo.findById(userId).orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "user not found"));
        log.info("save certification to user {}", userDomain.getName());

        CertDomain certDomain = CertDomain.builder()
                .user(userDomain)
                .isActive(certDTO.getIsActive())
                .pk(certDTO.getPk().getBytes(StandardCharsets.UTF_8))
                .cert(certDTO.getCert().getBytes(StandardCharsets.UTF_8))
                .build();

        certDomain = certRepo.save(certDomain);
        return ResponseEntity.ok(
                ApiResponse.of(Status.OK, "certificate saved", CertDTO.from(certDomain)));
    }

    @ResponseBody
    @GetMapping("/v1/user/{userId}/cert")
    public ResponseEntity<ApiResponse<List<CertDTO>>> getCert(@PathVariable Long userId){
        UserDomain userDomain = userRepo.findById(userId).orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "user not found"));
        log.info("show certifications from user {}", userDomain.getName());

        List<CertDomain> certDomains = userDomain.getCerts();
        return ResponseEntity.ok(
                ApiResponse.of(Status.OK, "show certificates", certDomains.stream().map(CertDTO::from)
                        .collect(Collectors.toList())));
    }

    @ResponseBody
    @PatchMapping ("/v1/cert/activate/{certId}")
    public ResponseEntity<ApiResponse<CertDTO>> flipCert(@PathVariable Long certId, @RequestBody Map<String, Boolean> updates){
        CertDomain certDomain = certRepo.findById(certId).orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "cert not found"));
        log.info("change cert {} active status", certDomain.getId());

        Boolean newStatus = updates.get("isActive");
        CertDomain updatedCert = null;
        try {
            updatedCert = certService.updateCertStatus(certId, newStatus);
        } catch (Exception e) {
            log.error("Failed to change status", e);
            return ResponseEntity.internalServerError().build();
        }

        try {
            certService.notifyExternalSystem(updatedCert);
        } catch (Exception e) {
            log.error("Failed to inform external system", e);
            // should user know the external system failure
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(
                ApiResponse.of(Status.OK, "certificate changed", CertDTO.from(updatedCert)));

    }

    private String getSaltedHash(String password, byte[] salt) {
        String saltedHash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedPassword) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            saltedHash = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return saltedHash;
    }

    private byte[] createSalt() {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }
}