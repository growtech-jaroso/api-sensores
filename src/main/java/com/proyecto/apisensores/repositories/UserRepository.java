package com.proyecto.apisensores.repositories;

import com.proyecto.apisensores.entities.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<User, String> {}
