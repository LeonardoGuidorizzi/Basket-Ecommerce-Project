package dev.devdreamer.ecommerce.basketservice.order;

import dev.devdreamer.ecommerce.basketservice.configuration.SecurityConfiguration;
import dev.devdreamer.ecommerce.basketservice.controller.OrderController;
import dev.devdreamer.ecommerce.basketservice.dto.order.OrderResponseDTO;
import dev.devdreamer.ecommerce.basketservice.exception.security.CustomAccessDeniedHandler;
import dev.devdreamer.ecommerce.basketservice.exception.security.CustomAuthenticationEntryPoint;
import dev.devdreamer.ecommerce.basketservice.repository.UserRepository;
import dev.devdreamer.ecommerce.basketservice.security.filter.SecurityFilter;
import dev.devdreamer.ecommerce.basketservice.security.jwt.TokenService;
import dev.devdreamer.ecommerce.basketservice.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@Import({
        SecurityConfiguration.class,
        SecurityFilter.class,
        CustomAuthenticationEntryPoint.class,
        CustomAccessDeniedHandler.class
})
class OrderControllerTest {

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
    private OrderService orderService;

    // ─────────────────────────────────────────────────────────────
    // POST /checkout
    // ─────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("POST /checkout → 201 Created quando checkout realizado com sucesso")
    void checkout_shouldReturn201_whenSuccess() throws Exception {
        when(orderService.checkout()).thenReturn(OrderResponseDTO.builder().build());

        mockMvc.perform(post("/api/v1/basketservice/order/checkout"))
                .andExpect(status().isCreated());

        verify(orderService).checkout();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /checkout → 403 Forbidden quando usuário é ADMIN")
    void checkout_shouldReturn403_whenAdmin() throws Exception {
        mockMvc.perform(post("/api/v1/basketservice/order/checkout"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(orderService);
    }

    @Test
    @DisplayName("POST /checkout → 401 Unauthorized quando não autenticado")
    void checkout_shouldReturn401_whenNotAuthenticated() throws Exception {
        mockMvc.perform(post("/api/v1/basketservice/order/checkout"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(orderService);
    }

    // ─────────────────────────────────────────────────────────────
    // GET /me
    // ─────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("GET /me → 200 OK com lista de orders do usuário autenticado")
    void getMyOrders_shouldReturn200_withOrders() throws Exception {
        when(orderService.getMyOrders()).thenReturn(List.of(OrderResponseDTO.builder().build()));

        mockMvc.perform(get("/api/v1/basketservice/order/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(orderService).getMyOrders();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("GET /me → 200 OK com lista vazia quando usuário não tem orders")
    void getMyOrders_shouldReturn200_withEmptyList() throws Exception {
        when(orderService.getMyOrders()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/basketservice/order/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(orderService).getMyOrders();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /me → 403 Forbidden quando usuário é ADMIN")
    void getMyOrders_shouldReturn403_whenAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/basketservice/order/me"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(orderService);
    }

    @Test
    @DisplayName("GET /me → 401 Unauthorized quando não autenticado")
    void getMyOrders_shouldReturn401_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/basketservice/order/me"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(orderService);
    }

    // ─────────────────────────────────────────────────────────────
    // GET /admin — getAllOrders
    // ─────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /admin → 200 OK com todas as orders quando usuário é ADMIN")
    void getAllOrders_shouldReturn200_whenAdmin() throws Exception {
        when(orderService.getAllOrders()).thenReturn(
                List.of(OrderResponseDTO.builder().build(), OrderResponseDTO.builder().build())
        );

        mockMvc.perform(get("/api/v1/basketservice/order/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(orderService).getAllOrders();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("GET /admin → 403 Forbidden quando usuário é USER")
    void getAllOrders_shouldReturn403_whenUser() throws Exception {
        mockMvc.perform(get("/api/v1/basketservice/order/admin"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(orderService);
    }

    @Test
    @DisplayName("GET /admin → 401 Unauthorized quando não autenticado")
    void getAllOrders_shouldReturn401_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/basketservice/order/admin"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(orderService);
    }
}
