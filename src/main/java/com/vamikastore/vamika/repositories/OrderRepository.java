package com.vamikastore.vamika.repositories;

import com.vamikastore.vamika.auth.entities.User;
import com.vamikastore.vamika.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByUser(User user);
}
