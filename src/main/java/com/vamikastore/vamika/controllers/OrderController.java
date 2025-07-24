package com.vamikastore.vamika.controllers;

import com.vamikastore.vamika.auth.dto.OrderResponse;
import com.vamikastore.vamika.dto.OrderRequest;
import com.vamikastore.vamika.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/order")
@CrossOrigin
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest, Principal principal) throws Exception {
        OrderResponse orderResponse = orderService.createOrder(orderRequest, principal);

        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }
}
