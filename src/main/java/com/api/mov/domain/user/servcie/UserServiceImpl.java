package com.api.mov.domain.user.servcie;

import com.api.mov.domain.user.repository.UserRepository;
import com.api.mov.domain.user.web.dto.UserSignUpReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Override
    public void signUp(UserSignUpReq userSignUpReq) {
        if(userRepository.existsByUsername(userSignUpReq.getUsername())) {

        }
    }
}
