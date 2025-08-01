package com.CareGenius.book.Service.ServiceImp;

import com.CareGenius.book.Dto.UserDto;
import com.CareGenius.book.Model.Role;
import com.CareGenius.book.Model.User;
import com.CareGenius.book.Repository.UserRepository;
import com.CareGenius.book.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
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

        for(int i=1; i<= 10; ++i){
            int newChar = rand.nextInt(keyboardChars.length);
            stringBuild.append(keyboardChars[newChar]);
        }

        userDB.setPassword(stringBuild.toString());
        userRepository.save(userDB);

        sendEmail(email, stringBuild.toString());

        return "Please check your email to obtain your new password";
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
