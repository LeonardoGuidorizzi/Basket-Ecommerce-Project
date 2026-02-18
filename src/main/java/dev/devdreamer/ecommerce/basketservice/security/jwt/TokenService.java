package dev.devdreamer.ecommerce.basketservice.security.filter;

import dev.devdreamer.ecommerce.basketservice.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;

@Service
public class TokenService {

    @Value("${security.jwt.secret}")
    private String secret;

    public String generateToken(User user) {

        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("userId", user.getId())
                .withClaim("role", user.getRole().name())
                .withExpiresAt(Instant.now().plus(2, ChronoUnit.HOURS))
                .sign(algorithm);
    }

    public Long validateToken(String token) {

        Algorithm algorithm = Algorithm.HMAC256(secret);

        DecodedJWT jwt = JWT.require(algorithm)
                .build()
                .verify(token);

        return jwt.getClaim("userId").asLong();
    }
}
