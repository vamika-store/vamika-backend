package com.vamikastore.vamika.auth.repositories;

import com.vamikastore.vamika.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserDetailRepository extends JpaRepository<User, UUID> {
    User findByEmail(String username);
}
