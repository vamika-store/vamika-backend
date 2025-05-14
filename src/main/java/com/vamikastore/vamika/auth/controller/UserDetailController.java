package com.vamikastore.vamika.auth.controller;

import com.vamikastore.vamika.auth.dto.UserDetailsDto;
import com.vamikastore.vamika.auth.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class UserDetailController {

        @Autowired
        private UserDetailsService userDetailsService;

        @GetMapping("/profile")
        public ResponseEntity<UserDetailsDto> getUserProfile(Principal principal) {
            /*
             * if (principal == null) { return new
             * ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Return 401 if the user is not
             * authenticated }
             */
            User user = (User) userDetailsService.loadUserByUsername(principal.getName());

            if (user == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            UserDetailsDto userDetailsDto = UserDetailsDto.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .authorityList(user.getAuthorities().toArray())
                    .build();

            return new ResponseEntity<>(userDetailsDto, HttpStatus.OK);
        }

    }

