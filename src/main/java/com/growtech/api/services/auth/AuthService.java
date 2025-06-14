package com.growtech.api.services.auth;

import com.growtech.api.dtos.responses.AuthInfo;
import com.growtech.api.dtos.requests.UserLoginDto;
import com.growtech.api.dtos.requests.passwords.UserRegisterDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface AuthService {
  Mono<AuthInfo> registerUser(UserRegisterDto userRegisterDTO);
  Mono<AuthInfo> loginUser(UserLoginDto userLoginDTO);
}
