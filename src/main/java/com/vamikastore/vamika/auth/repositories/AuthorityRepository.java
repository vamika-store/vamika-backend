package com.vamikastore.vamika.auth.repositories;

import com.vamikastore.vamika.auth.entities.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, UUID>{

    Authority findByRoleCode(String user);
}

