package dev.devdreamer.ecommerce.basketservice.repository;

import dev.devdreamer.ecommerce.basketservice.domain.user.User;
import org.springframework.boot.security.autoconfigure.SecurityProperties;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
}
