package com.CareGenius.book.Service.ServiceImp;

import com.CareGenius.book.Dto.UserDto;
import com.CareGenius.book.Dto.UserResponseDto;
import com.CareGenius.book.Model.Role;
import com.CareGenius.book.Model.User;
import com.CareGenius.book.Repository.UserRepository;
import com.CareGenius.book.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager manager;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;



    @Override
    public User register(UserDto userDto) {
        User user = map(userDto);
        return userRepository.save(user);
    }

    @Override
    public UserResponseDto authenticateUser(UserDto userDto) {
        User userDB = (User) userDetailsService.loadUserByUsername(userDto.getEmail());
        if(userDB == null){
            throw new IllegalArgumentException("Can not find User with this email " +userDto.getEmail());
        }
        manager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
        String token = "Bearer ".concat(jwtService.generateToken(userDto.getEmail()));

        return new UserResponseDto(
                token,
                userDB.getUid(),
                userDB.getFullName(),
                userDB.getAddress(),
                userDB.getEmail(),
                userDB.getGender().name(),
                userDB.getRole().name()
        );
    }

    @Override
    public String sendResetPasswordEmail(String email) {
        User userDB = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("Can not find user with this email: " + email));

        Random rand = new Random();

        char[] keyboardChars = {
                'a','b','c','d','e','f','g','h','i','j',
                'A','B','C','D','E','F','G','H','I','J',
                '0','1','2','3','4','5',
                '!','@','#','$','%','^',
                '(',')','-','_','=','+',
                '.','/',',','?',' '
        };

        StringBuilder stringBuild = new StringBuilder();

        for(int i=1; i< 10; ++i){
            int newChar = rand.nextInt(keyboardChars.length);
            stringBuild.append(keyboardChars[newChar]);
        }

        userDB.setPassword(passwordEncoder.encode(stringBuild.toString()));
        userRepository.save(userDB);

        sendEmail(email, stringBuild.toString());

        return "Please check your email to obtain your new password";
    }

    @Override
    @Transactional
    public String deleteUserByUid(String userUid) {
        User userDB = userRepository.findById(userUid).orElseThrow(()-> new IllegalArgumentException("User not found"));
        log.info("Found this :" +userDB.getUid());
        userRepository.delete(userDB);
        return "Delete user successfully!";
    }

    private void sendEmail(String email, String newPassword){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Elder Sync - Password Reset");
        message.setText("Your new password is: " + newPassword + "\nPlease change it after logging in.");
        message.setFrom(fromEmail);

        mailSender.send(message);
    }

    protected User map(UserDto userDto) {
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
