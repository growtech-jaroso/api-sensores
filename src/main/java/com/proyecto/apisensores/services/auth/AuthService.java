package com.proyecto.apisensores.services.auth;

import com.proyecto.apisensores.dtos.AuthInfo;
import com.proyecto.apisensores.dtos.UserLoginDto;
import com.proyecto.apisensores.dtos.UserRegisterDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface AuthService {
  Mono<AuthInfo> registerUser(UserRegisterDto userRegisterDTO);
  Mono<AuthInfo> loginUser(UserLoginDto userLoginDTO);
}
