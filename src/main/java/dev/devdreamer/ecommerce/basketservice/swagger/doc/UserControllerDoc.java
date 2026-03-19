package dev.devdreamer.ecommerce.basketservice.swagger.doc;

import dev.devdreamer.ecommerce.basketservice.dto.auth.LoginRequestDTO;
import dev.devdreamer.ecommerce.basketservice.dto.auth.LoginResponseDTO;
import dev.devdreamer.ecommerce.basketservice.dto.auth.RegisterRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface UserControllerDoc {
    @Operation(summary = "Register a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "User already exists or invalid data")
    })
    ResponseEntity<Void> register(
            @RequestBody(description = "User registration data", required = true)
            RegisterRequestDTO request
    );

    @Operation(summary = "Authenticate user and generate JWT token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    ResponseEntity<LoginResponseDTO> login(
            @RequestBody(description = "User login credentials", required = true)
            LoginRequestDTO request
    );
}
