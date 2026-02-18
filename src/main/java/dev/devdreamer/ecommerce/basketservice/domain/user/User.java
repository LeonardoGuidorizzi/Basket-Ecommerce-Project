package dev.devdreamer.ecommerce.basketservice.domain.user;

import dev.devdreamer.ecommerce.basketservice.Enum.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String id;

    private String email;

    private String password;

    private UserRole role;

    private LocalDateTime createdAt;

    public static User create(String email, String encodedPassword) {
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
