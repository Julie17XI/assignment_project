package com.example.cloudflareassignment.service;

import com.example.cloudflareassignment.controller.dto.CertDTO;
import com.example.cloudflareassignment.domain.CertDomain;
import com.example.cloudflareassignment.repo.CertRepo;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CertService {
    @Autowired
    private CertRepo certRepo;


    @Builder
    @Data
    public static class CertActivationRequest {
        private Long certId;

    }
    JsonMapper jsonMapper = new JsonMapper();
    private String url = "https://eo4ehqx8kn8orrf.m.pipedream.net/v1/cert/activation";
    private OkHttpClient restClient;
    @PostConstruct
    private void init(){
        this.restClient = new OkHttpClient.Builder()
                .connectionPool(new okhttp3.ConnectionPool(10, 30, TimeUnit.SECONDS))
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

    }


    public CertDomain updateCertStatus(Long id, Boolean newStatus) throws IOException{
        CertDomain certDomain = certRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Cert not found"));
        if (!certDomain.isActive() == newStatus){
            certDomain.setActive(newStatus);
            certDomain = certRepo.save(certDomain);
        }
        return certDomain;
    }
    public void notifyExternalSystem(CertDomain certDomain) throws IOException{
        CertDTO certDTO = CertDTO.from(certDomain);
        String payload = jsonMapper.writeValueAsString(certDTO);
        okhttp3.RequestBody body = okhttp3.RequestBody.create(payload, okhttp3.MediaType.parse("application/json"));
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (okhttp3.Response response = restClient.newCall(request).execute()) {
            ResponseBody responseBody =  response.body();
            if( responseBody == null){
                throw new HttpClientErrorException(HttpStatus.valueOf(response.code()), "error from external service");
            }
            log.info("Response from external service {} - {}.", response.code(), responseBody.string());
            if (response.code() != HttpStatus.OK.value()) {
                throw new HttpClientErrorException(HttpStatus.valueOf(response.code()), "error from external service");
            }
        }

    }
}


