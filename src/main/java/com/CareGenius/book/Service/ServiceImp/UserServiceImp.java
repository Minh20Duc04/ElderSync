package com.CareGenius.book.Service.ServiceImp;

import com.CareGenius.book.Repository.UserRepository;
import com.CareGenius.book.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;




}
