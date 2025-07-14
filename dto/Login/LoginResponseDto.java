package com.codeera.expensetracker.dto.Login;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponseDto {

    private String token;

    private String email;


}
