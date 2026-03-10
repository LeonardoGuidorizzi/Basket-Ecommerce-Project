package dev.devdreamer.ecommerce.basketservice.user;

import dev.devdreamer.ecommerce.basketservice.controller.UserController;
import dev.devdreamer.ecommerce.basketservice.dto.auth.LoginRequestDTO;
import dev.devdreamer.ecommerce.basketservice.dto.auth.LoginResponseDTO;
import dev.devdreamer.ecommerce.basketservice.dto.auth.RegisterRequestDTO;
import dev.devdreamer.ecommerce.basketservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;




@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void shouldRegisterUser() {
        RegisterRequestDTO request =
                new RegisterRequestDTO("test","test@email.com", "123456");

        when(userService.register(any()))
                .thenReturn("token");

        ResponseEntity<Void> response =
                userController.register(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(userService).register(request);
    }

    @Test
    void shouldLoginUser() {
        LoginRequestDTO request =
                new LoginRequestDTO("test@email.com", "123456");

        when(userService.login(any()))
                .thenReturn(new LoginResponseDTO("jwt-token"));

        ResponseEntity<LoginResponseDTO> response =
                userController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwt-token", response.getBody().token());
    }
}
