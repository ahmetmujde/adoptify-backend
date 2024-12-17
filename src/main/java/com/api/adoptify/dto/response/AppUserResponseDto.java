package com.api.adoptify.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserResponseDto {
    private Long id;
    private String userName;
    private String email;
    private String phoneNumber;
    private Boolean criminalRecord;
    private String gender;
    private String addressArea;
    private String addressCity;
    private String addressState;
}
