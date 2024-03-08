package com.example.cloudflareassignment.repo;


import com.example.cloudflareassignment.domain.UserDomain;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepo extends CrudRepository<UserDomain, Long>{

}
