package dev.devdreamer.ecommerce.basketservice.security.auth;

import dev.devdreamer.ecommerce.basketservice.domain.user.User;
import dev.devdreamer.ecommerce.basketservice.dto.auth.LoginRequestDTO;
import dev.devdreamer.ecommerce.basketservice.dto.auth.LoginResponseDTO;
import dev.devdreamer.ecommerce.basketservice.dto.auth.RegisterRequestDTO;
import dev.devdreamer.ecommerce.basketservice.repository.UserRepository;
import dev.devdreamer.ecommerce.basketservice.security.jwt.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public String register(RegisterRequestDTO request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = User.create( request.email(), encodedPassword);

        userRepository.save(user);

        return tokenService.generateToken(user);
    }

    public LoginResponseDTO login(LoginRequestDTO request) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        );
        User user = (User) authenticationToken.getPrincipal();
        String token = tokenService.generateToken(user);
        return new LoginResponseDTO(token);
    }
}
