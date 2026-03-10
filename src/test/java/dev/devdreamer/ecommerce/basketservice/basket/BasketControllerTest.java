package dev.devdreamer.ecommerce.basketservice.basket;

import dev.devdreamer.ecommerce.basketservice.controller.BasketController;

import dev.devdreamer.ecommerce.basketservice.dto.basket.BasketResponseDTO;
import dev.devdreamer.ecommerce.basketservice.service.BasketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BasketController.class)
class BasketControllerTest {

    static final String BASE_URL = "/api/v1/basketservice/basket";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Mock
    BasketService basketService;

    // -------------------------------------------------------------------------
    // POST /items  — addItem
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("POST /items")
    class AddItem {

        @Test
        @DisplayName("deve retornar 200 ao adicionar item com parâmetros válidos")
        void shouldReturn200WhenAddingItem() throws Exception {
            doNothing().when(basketService).addItem(10L, 2);

            mockMvc.perform(post(BASE_URL + "/items")
                            .param("productId", "10")
                            .param("quantity", "2")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(basketService).addItem(10L, 2);
        }

        @Test
        @DisplayName("deve retornar 400 quando productId está ausente")
        void shouldReturn400WhenProductIdMissing() throws Exception {
            mockMvc.perform(post(BASE_URL + "/items")
                            .param("quantity", "2"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(basketService);
        }

        @Test
        @DisplayName("deve retornar 400 quando quantity está ausente")
        void shouldReturn400WhenQuantityMissing() throws Exception {
            mockMvc.perform(post(BASE_URL + "/items")
                            .param("productId", "10"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(basketService);
        }

        @Test
        @DisplayName("deve propagar exceção do service como 500")
        void shouldReturn500WhenServiceThrows() throws Exception {
            doThrow(new RuntimeException("unexpected")).when(basketService).addItem(anyLong(), anyInt());

            mockMvc.perform(post(BASE_URL + "/items")
                            .param("productId", "10")
                            .param("quantity", "2"))
                    .andExpect(status().isInternalServerError());
        }
    }

    // -------------------------------------------------------------------------
    // PUT /items  — updateQuantity
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("PUT /items")
    class UpdateQuantity {

        @Test
        @DisplayName("deve retornar 200 ao atualizar quantidade com parâmetros válidos")
        void shouldReturn200WhenUpdatingQuantity() throws Exception {
            doNothing().when(basketService).updateQuantity(10L, 5);

            mockMvc.perform(put(BASE_URL + "/items")
                            .param("productId", "10")
                            .param("quantity", "5"))
                    .andExpect(status().isOk());

            verify(basketService).updateQuantity(10L, 5);
        }

        @Test
        @DisplayName("deve retornar 400 quando productId está ausente")
        void shouldReturn400WhenProductIdMissing() throws Exception {
            mockMvc.perform(put(BASE_URL + "/items")
                            .param("quantity", "5"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(basketService);
        }

        @Test
        @DisplayName("deve retornar 400 quando quantity está ausente")
        void shouldReturn400WhenQuantityMissing() throws Exception {
            mockMvc.perform(put(BASE_URL + "/items")
                            .param("productId", "10"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(basketService);
        }

        @Test
        @DisplayName("deve propagar RuntimeException do service como 500")
        void shouldReturn500WhenServiceThrows() throws Exception {
            doThrow(new RuntimeException("Basket not found"))
                    .when(basketService).updateQuantity(anyLong(), anyInt());

            mockMvc.perform(put(BASE_URL + "/items")
                            .param("productId", "99")
                            .param("quantity", "1"))
                    .andExpect(status().isInternalServerError());
        }
    }

    // -------------------------------------------------------------------------
    // DELETE /items  — deleteItem
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("DELETE /items")
    class DeleteItem {

        @Test
        @DisplayName("deve retornar 204 ao remover item com sucesso")
        void shouldReturn204WhenDeletingItem() throws Exception {
            doNothing().when(basketService).removeItem(10L);

            mockMvc.perform(delete(BASE_URL + "/items")
                            .param("productId", "10"))
                    .andExpect(status().isNoContent());

            verify(basketService).removeItem(10L);
        }

        @Test
        @DisplayName("deve retornar 400 quando productId está ausente")
        void shouldReturn400WhenProductIdMissing() throws Exception {
            mockMvc.perform(delete(BASE_URL + "/items"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(basketService);
        }

        @Test
        @DisplayName("deve propagar RuntimeException do service como 500")
        void shouldReturn500WhenServiceThrows() throws Exception {
            doThrow(new RuntimeException("Basket not found"))
                    .when(basketService).removeItem(anyLong());

            mockMvc.perform(delete(BASE_URL + "/items")
                            .param("productId", "99"))
                    .andExpect(status().isInternalServerError());
        }
    }

    // -------------------------------------------------------------------------
    // GET /items  — getAllBaskets
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("GET /items")
    class GetAllBaskets {

        @Test
        @DisplayName("deve retornar 200 com lista de carrinhos")
        void  shouldReturn200WithBasketList() throws Exception {
            BasketResponseDTO dto1 = mock(BasketResponseDTO.class);
            BasketResponseDTO dto2 = mock(BasketResponseDTO.class);
            when(basketService.getAllBaskets()).thenReturn(List.of(dto1, dto2));

            mockMvc.perform(get(BASE_URL + "/items"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

            verify(basketService).getAllBaskets();
        }

        @Test
        @DisplayName("deve retornar 200 com lista vazia quando não há carrinhos")
        void shouldReturn200WithEmptyList() throws Exception {
            when(basketService.getAllBaskets()).thenReturn(List.of());

            mockMvc.perform(get(BASE_URL + "/items"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("[]"));

            verify(basketService).getAllBaskets();
        }
    }

    // -------------------------------------------------------------------------
    // GET /me  — getMyBasket
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("GET /me")
    class GetMyBasket {

        @Test
        @DisplayName("deve retornar 200 com o carrinho do usuário autenticado")
        void shouldReturn200WithUserBasket() throws Exception {
            BasketResponseDTO dto = mock(BasketResponseDTO.class);
            when(basketService.getMyBasket()).thenReturn(dto);

            mockMvc.perform(get(BASE_URL + "/me"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

            verify(basketService).getMyBasket();
        }

        @Test
        @DisplayName("deve retornar 500 quando carrinho do usuário não existe")
        void shouldReturn500WhenBasketNotFound() throws Exception {
            when(basketService.getMyBasket())
                    .thenThrow(new RuntimeException("Basket not found"));

            mockMvc.perform(get(BASE_URL + "/me"))
                    .andExpect(status().isInternalServerError());
        }
    }
}