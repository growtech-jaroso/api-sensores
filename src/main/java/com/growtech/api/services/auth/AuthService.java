package com.growtech.api.services.auth;

import com.growtech.api.dtos.AuthInfo;
import com.growtech.api.dtos.requests.UserLoginDto;
import com.growtech.api.dtos.requests.UserRegisterDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface AuthService {
  Mono<AuthInfo> registerUser(UserRegisterDto userRegisterDTO);
  Mono<AuthInfo> loginUser(UserLoginDto userLoginDTO);
}
