package dev.devdreamer.ecommerce.basketservice;

import dev.devdreamer.ecommerce.basketservice.domain.user.User;
import dev.devdreamer.ecommerce.basketservice.dto.auth.LoginRequestDTO;
import dev.devdreamer.ecommerce.basketservice.dto.auth.LoginResponseDTO;
import dev.devdreamer.ecommerce.basketservice.dto.auth.RegisterRequestDTO;
import dev.devdreamer.ecommerce.basketservice.exception.custom.EmailAlreadyExistsException;
import dev.devdreamer.ecommerce.basketservice.repository.UserRepository;
import dev.devdreamer.ecommerce.basketservice.security.jwt.TokenService;
import dev.devdreamer.ecommerce.basketservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.mongodb.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    TokenService tokenService;

    @Mock
    UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService service;

    @Test
    void shouldLoginSuccessfully(){

        LoginRequestDTO request = new LoginRequestDTO("user@gmail.com", "123456");

        User user = User.create("user@gmail.com", "encoded");

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );

        when(authenticationManager.authenticate(
                any(Authentication.class)
        )).thenReturn(authentication);

        when(tokenService.generateToken(user))
                .thenReturn("fake-jwt");

        LoginResponseDTO response = service.login(request);
        assertNotNull(response);
        assertEquals("fake-jwt", response.token());
    }

    @Test
    void shouldCreateUserSucessfully(){

        RegisterRequestDTO request = new RegisterRequestDTO("user", "user@gmail.com","123456");


        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(request.password()))
                .thenReturn("encodedPassword");

        when(tokenService.generateToken(any(User.class)))
                .thenReturn("fake-jwt-token");


        String response = service.register(request);
        assertNotNull(response);

        verify(userRepository).save(any(User.class));
        verify(tokenService).generateToken(any(User.class));
    }


    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        RegisterRequestDTO request =
                new RegisterRequestDTO("test", "test@email.com", "123456");

        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.of(mock(User.class)));

        // Act & Assert
        assertThrows(EmailAlreadyExistsException.class,
                () -> service.register(request));

        verify(userRepository, never()).save(any());
        verify(tokenService, never()).generateToken(any());
    }


}
