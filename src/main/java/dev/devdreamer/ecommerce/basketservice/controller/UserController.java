package dev.devdreamer.ecommerce.basketservice.controller;

import dev.devdreamer.ecommerce.basketservice.dto.auth.LoginRequestDTO;
import dev.devdreamer.ecommerce.basketservice.dto.auth.LoginResponseDTO;
import dev.devdreamer.ecommerce.basketservice.dto.auth.RegisterRequestDTO;
import dev.devdreamer.ecommerce.basketservice.service.UserService;
import dev.devdreamer.ecommerce.basketservice.swagger.doc.UserControllerDoc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/basketservice/auth")
@RequiredArgsConstructor
public class UserController implements UserControllerDoc {
    private final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDTO request){
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request){
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(request));
    }
}
