package com.growtech.api.services.users;

import com.growtech.api.dtos.requests.passwords.UserEditDto;
import com.growtech.api.dtos.responses.UserInfo;
import com.growtech.api.dtos.requests.passwords.ChangePasswordDto;
import com.growtech.api.dtos.requests.passwords.UserRegisterDto;
import com.growtech.api.entities.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

@Service
public interface UserService {
  Mono<User> findByEmail(String email);
  Mono<User> findByUsername(String username);
  Boolean passwordMatches(String rawPassword, String encodedPassword);
  Mono<User> create(UserRegisterDto userRegisterDTO);
  Mono<Tuple2<List<UserInfo>, Long>> getAllUsersPaginated(PageRequest pageRequest);
  Flux<String> getAllUserEmails();
  Mono<String> deleteUser(User authUser, String userId);
  Mono<UserInfo> getUserById(String userId);
  Mono<String> changePassword(User user, ChangePasswordDto changePasswordDto);
  Mono<UserInfo> editUser(String userId, UserEditDto userEditDto);
}
