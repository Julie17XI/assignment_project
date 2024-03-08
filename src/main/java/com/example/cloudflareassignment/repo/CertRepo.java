package com.example.cloudflareassignment.repo;


import com.example.cloudflareassignment.domain.CertDomain;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertRepo extends CrudRepository<CertDomain, Long>{

}