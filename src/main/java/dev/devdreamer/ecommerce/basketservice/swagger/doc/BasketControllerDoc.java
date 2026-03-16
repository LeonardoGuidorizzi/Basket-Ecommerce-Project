package dev.devdreamer.ecommerce.basketservice.swagger.doc;
import dev.devdreamer.ecommerce.basketservice.dto.basket.BasketResponseDTO;
import dev.devdreamer.ecommerce.basketservice.dto.order.OrderResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BasketControllerDoc {

    @Operation(summary = "Add an item to the authenticated user's basket",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item added successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    ResponseEntity<Void> addItem(
            @Parameter(description = "Product ID to add", required = true) Long productId,
            @Parameter(description = "Quantity to add", required = true) Integer quantity
    );

    @Operation(summary = "Update the quantity of an item in the basket",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Quantity updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Item not found in basket")
    })
    ResponseEntity<Void> updateQuantity(
            @Parameter(description = "Product ID to update", required = true) Long productId,
            @Parameter(description = "New quantity", required = true) Integer quantity
    );

    @Operation(summary = "Remove an item from the basket",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item removed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Item not found in basket")
    })
    ResponseEntity<Void> deleteItem(
            @Parameter(description = "Product ID to remove", required = true) Long productId
    );

    @Operation(summary = "Get all baskets (Admin only)",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All baskets retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied - requires ADMIN role")
    })
    ResponseEntity<List<BasketResponseDTO>> getAllBaskets();

    @Operation(summary = "Get all orders from the authenticated user",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Basket retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    ResponseEntity<BasketResponseDTO> getBasket();
}
