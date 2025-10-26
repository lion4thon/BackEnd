package com.api.mov.domain.user.servcie;

import com.api.mov.domain.user.entity.User;
import com.api.mov.domain.user.repository.UserRepository;
import com.api.mov.domain.user.web.dto.UserSignUpReq;
import com.api.mov.global.exception.CustomException;
import com.api.mov.global.response.code.user.UserErrorResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void signUp(UserSignUpReq userSignUpReq) {

        if(userRepository.existsByUsername(userSignUpReq.getUsername())) {
            throw new CustomException(UserErrorResponseCode.DUPLICATE_USERNAME_409);
        }

        User user = User.builder()
                .username(userSignUpReq.getUsername())
                .password(passwordEncoder.encode(userSignUpReq.getPassword()))
                .build();

        userRepository.save(user);
    }
}
