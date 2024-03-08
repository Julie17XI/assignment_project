package com.example.cloudflareassignment.repo;


import com.example.cloudflareassignment.domain.CertDomain;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface CertRepo extends CrudRepository<CertDomain, Long>{
    List<CertDomain> findByUserId(Long userId);
}