package com.codeera.expensetracker.service.User;

import com.codeera.expensetracker.dto.Registration.UserRegisterRequestDto;
import com.codeera.expensetracker.dto.Registration.UserResponseDto;

public interface UserService {

    UserResponseDto saveUser(UserRegisterRequestDto userRegisterRequestDto);

}
