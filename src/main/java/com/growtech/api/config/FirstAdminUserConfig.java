package com.growtech.api.config;

import com.growtech.api.entities.User;
import com.growtech.api.enums.UserRole;
import com.growtech.api.repositories.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class FirstAdminUserConfig {
  @Value("${app.users.admin.email}")
  private String firstAdminEmail;
  @Value("${app.users.admin.username}")
  private String firstAdminUsername;
  @Value("${app.users.admin.password}")
  private String firstAdminPassword;

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  public FirstAdminUserConfig(PasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void createFirstAdminUser() {
    this.userRepository.count()
      .flatMap(count -> {
        if (count == 0) {
          User user = User.builder()
            .email(this.firstAdminEmail)
            .username(this.firstAdminUsername)
            .password(this.passwordEncoder.encode(this.firstAdminPassword))
            .role(UserRole.ADMIN)
            .build();
          return this.userRepository.save(user)
            .then(Mono.just(true));
        }
        return Mono.just(false);
      }).subscribe(isCreated -> {
        if (isCreated) {
          log.info("First admin user created");
        }
      });
  }
}
