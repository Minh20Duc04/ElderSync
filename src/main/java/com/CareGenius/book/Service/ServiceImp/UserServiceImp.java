package com.CareGenius.book.Service.ServiceImp;

import com.CareGenius.book.Dto.UserDto;
import com.CareGenius.book.Model.Role;
import com.CareGenius.book.Model.User;
import com.CareGenius.book.Repository.UserRepository;
import com.CareGenius.book.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager manager;

    @Override
    public User register(UserDto userDto) {
        User user = map(userDto);
        return userRepository.save(user);
    }

    @Override
    public Map<String, Object> authenticateUser(UserDto userDto) {
        Map<String, Object> authenticatedUser = new HashMap<>();
        User userDB = (User) userDetailsService.loadUserByUsername(userDto.getEmail());
        if(userDB == null){
            throw new IllegalArgumentException("Can not find User with this email " +userDto.getEmail());
        }
        manager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
        authenticatedUser.put("token", "Bearer".concat(jwtService.generateToken(userDto.getEmail())));
        authenticatedUser.put("user", userDB);
        return authenticatedUser;
    }

    private User map(UserDto userDto) {
        return User.builder()
                .fullName(userDto.getFirstName() + " " + userDto.getLastName())
                .email(userDto.getEmail())
                .address(userDto.getAddress())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(Role.USER)
                .gender(userDto.getGender())
                .build();
    }



}
