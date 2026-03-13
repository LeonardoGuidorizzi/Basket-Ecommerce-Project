package dev.devdreamer.ecommerce.basketservice.basket;

import dev.devdreamer.ecommerce.basketservice.Enum.BasketStatus;
import dev.devdreamer.ecommerce.basketservice.configuration.SecurityConfiguration;
import dev.devdreamer.ecommerce.basketservice.controller.BasketController;


import dev.devdreamer.ecommerce.basketservice.dto.basket.BasketResponseDTO;
import dev.devdreamer.ecommerce.basketservice.exception.security.CustomAccessDeniedHandler;
import dev.devdreamer.ecommerce.basketservice.exception.security.CustomAuthenticationEntryPoint;
import dev.devdreamer.ecommerce.basketservice.repository.UserRepository;
import dev.devdreamer.ecommerce.basketservice.security.filter.SecurityFilter;
import dev.devdreamer.ecommerce.basketservice.security.jwt.TokenService;
import dev.devdreamer.ecommerce.basketservice.service.BasketService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BasketController.class)
@Import({
        SecurityConfiguration.class,         // SecurityFilterChain real
        SecurityFilter.class,                 // filtro JWT real
        CustomAuthenticationEntryPoint.class, // escreve 401 de verdade
        CustomAccessDeniedHandler.class       // escreve 403 de verdade
})
class BasketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ── dependências do SecurityFilter ─────────────────────────────
    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    // ── cache ───────────────────────────────────────────────────────
    @MockitoBean
    private CacheManager cacheManager;

    // ── dependência do controller ───────────────────────────────────
    @MockitoBean
    private BasketService basketService;

    // ─────────────────────────────────────────────────────────────
    // POST /items — addItem
    // ─────────────────────────────────────────────────────────────

    @Test
    @WithMockUser
    @DisplayName("POST /items → 204 No Content when item added successfully")
    void addItem_shouldReturn204() throws Exception {
        doNothing().when(basketService).addItem(1L, 2);

        mockMvc.perform(post("/api/v1/basketservice/basket/items")
                        .param("productId", "1")
                        .param("quantity", "2"))
                .andExpect(status().isNoContent());

        verify(basketService).addItem(1L, 2);
    }

    @Test
    @DisplayName("POST /items → 401 Unauthorized when not authenticated")
    void addItem_shouldReturn401_whenNotAuthenticated() throws Exception {
        mockMvc.perform(post("/api/v1/basketservice/basket/items")
                        .param("productId", "1")
                        .param("quantity", "2"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(basketService);
    }

    @Test
    @WithMockUser
    @DisplayName("POST /items → 400 Bad Request when productId is missing")
    void addItem_shouldReturn400_whenProductIdMissing() throws Exception {
        mockMvc.perform(post("/api/v1/basketservice/basket/items")
                        .param("quantity", "2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("POST /items → 400 Bad Request when quantity is missing")
    void addItem_shouldReturn400_whenQuantityMissing() throws Exception {
        mockMvc.perform(post("/api/v1/basketservice/basket/items")
                        .param("productId", "1"))
                .andExpect(status().isBadRequest());
    }

    // ─────────────────────────────────────────────────────────────
    // PUT /items — updateQuantity
    // ─────────────────────────────────────────────────────────────

    @Test
    @WithMockUser
    @DisplayName("PUT /items → 204 No Content when quantity updated successfully")
    void updateQuantity_shouldReturn204() throws Exception {
        doNothing().when(basketService).updateQuantity(1L, 5);

        mockMvc.perform(put("/api/v1/basketservice/basket/items")
                        .param("productId", "1")
                        .param("quantity", "5"))
                .andExpect(status().isNoContent());

        verify(basketService).updateQuantity(1L, 5);
    }

    @Test
    @DisplayName("PUT /items → 401 Unauthorized when not authenticated")
    void updateQuantity_shouldReturn401_whenNotAuthenticated() throws Exception {
        mockMvc.perform(put("/api/v1/basketservice/basket/items")
                        .param("productId", "1")
                        .param("quantity", "5"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(basketService);
    }

    // ─────────────────────────────────────────────────────────────
    // DELETE /items — deleteItem
    // ─────────────────────────────────────────────────────────────

    @Test
    @WithMockUser
    @DisplayName("DELETE /items → 204 No Content when item removed successfully")
    void deleteItem_shouldReturn204() throws Exception {
        doNothing().when(basketService).removeItem(1L);

        mockMvc.perform(delete("/api/v1/basketservice/basket/items")
                        .param("productId", "1"))
                .andExpect(status().isNoContent());

        verify(basketService).removeItem(1L);
    }

    @Test
    @DisplayName("DELETE /items → 401 Unauthorized when not authenticated")
    void deleteItem_shouldReturn401_whenNotAuthenticated() throws Exception {
        mockMvc.perform(delete("/api/v1/basketservice/basket/items")
                        .param("productId", "1"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(basketService);
    }

    // ─────────────────────────────────────────────────────────────
    // GET /items — getAllBaskets (ADMIN only)
    // ─────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /items → 200 OK with list when user is ADMIN")
    void getAllBaskets_shouldReturn200_whenAdmin() throws Exception {
        when(basketService.getAllBaskets()).thenReturn(List.of(buildBasketResponse()));

        mockMvc.perform(get("/api/v1/basketservice/basket/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].totalAmount").value(0));

        verify(basketService).getAllBaskets();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("GET /items → 403 Forbidden when user is not ADMIN")
    void getAllBaskets_shouldReturn403_whenNotAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/basketservice/basket/admin"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(basketService);
    }

    @Test
    @DisplayName("GET /items → 401 Unauthorized when not authenticated")
    void getAllBaskets_shouldReturn401_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/basketservice/basket/items"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(basketService);
    }

    // ─────────────────────────────────────────────────────────────
    // GET /me — getMyBasket
    // ─────────────────────────────────────────────────────────────

    @Test
    @WithMockUser
    @DisplayName("GET /me → 200 OK with basket of authenticated user")
    void getMyBasket_shouldReturn200() throws Exception {
        when(basketService.getMyBasket()).thenReturn(buildBasketResponse());

        mockMvc.perform(get("/api/v1/basketservice/basket/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.totalAmount").value(0));

        verify(basketService).getMyBasket();
    }

    @Test
    @DisplayName("GET /me → 401 Unauthorized when not authenticated")
    void getMyBasket_shouldReturn401_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/basketservice/basket/me"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(basketService);
    }

    // ─────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────

    private BasketResponseDTO buildBasketResponse() {
        return BasketResponseDTO.builder()
                .items(List.of())
                .totalAmount(BigDecimal.ZERO)
                .status(BasketStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
    }
}