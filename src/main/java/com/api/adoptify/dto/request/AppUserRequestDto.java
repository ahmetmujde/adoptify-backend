package com.api.adoptify.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserRequestDto {
    private String userName;
    private String email;
    private String password;
    private String phoneNumber;
    private Boolean criminalRecord;
    private Long genderId;
    private AddressRequestDto address;
}