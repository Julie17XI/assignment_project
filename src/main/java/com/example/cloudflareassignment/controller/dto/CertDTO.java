package com.example.cloudflareassignment.controller.dto;


import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import com.example.cloudflareassignment.domain.CertDomain;


@Data
@Builder
public class CertDTO {

    String id;

    String user_id;

    Boolean isActive;

    @NotBlank(message = "private key cannot be blank")
    String pk;

    @NotBlank(message = "cert cannot be blank")
    String cert;

    public static CertDTO from(CertDomain certDomain) {
        return CertDTO.builder()
                .id(certDomain.getId().toString())
                .user_id(certDomain.getUser().getId().toString())
                .isActive(certDomain.isActive())
                .pk(new String(certDomain.getPk()))
                .cert(new String(certDomain.getCert()))
                .build();

    }
}
