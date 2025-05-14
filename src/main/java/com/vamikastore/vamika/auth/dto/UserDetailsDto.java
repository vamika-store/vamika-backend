package com.vamikastore.vamika.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDto {

    private UUID id;
    private String firstName;
    private String lastName ;
    private String email;
    private String phoneNumber;
    private Object authorityList;
}
