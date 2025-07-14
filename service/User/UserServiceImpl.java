package com.codeera.expensetracker.service.User;


import com.codeera.expensetracker.dto.Registration.UserRegisterRequestDto;
import com.codeera.expensetracker.dto.Registration.UserResponseDto;
import com.codeera.expensetracker.entity.Role;
import com.codeera.expensetracker.entity.User;
import com.codeera.expensetracker.exception.ExceptionUtil;
import com.codeera.expensetracker.mapper.UserMapper;
import com.codeera.expensetracker.repository.RoleRepository;
import com.codeera.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder   mPasswordEncoder;

    @Override
    public UserResponseDto saveUser(UserRegisterRequestDto userRegisterRequestDto) {

        if (userRepository.findByEmail(userRegisterRequestDto.getEmail()).isPresent()) {
            throw ExceptionUtil.duplicate("User : ", userRegisterRequestDto.getEmail());
        }

        User user = UserMapper.mapToUserEntity(userRegisterRequestDto);

        String requestedRole = userRegisterRequestDto.getRole();
        String roleName = "ROLE_" + requestedRole.toUpperCase();
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException(roleName + " not found"));

        // Set role entity
        user.setRoles(Set.of(role));
        //set password
        user.setPassword(mPasswordEncoder.encode(user.getPassword()));
        User savedUser =  userRepository.save(user);

        if (savedUser.getId() <= 0) {
            ExceptionUtil.throwSaveFailed("user");
        }
        return UserMapper.mapToUserResponseDto(savedUser);

    }
}
