package com.vamikastore.vamika.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AddressRequest {

    private String name;
    private String addressLine;
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String phoneNumber;

}
