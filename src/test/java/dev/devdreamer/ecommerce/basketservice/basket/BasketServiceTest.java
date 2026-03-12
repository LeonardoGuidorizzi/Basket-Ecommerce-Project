package dev.devdreamer.ecommerce.basketservice.basket;


import dev.devdreamer.ecommerce.basketservice.Enum.UserRole;
import dev.devdreamer.ecommerce.basketservice.client.ProductClient;
import dev.devdreamer.ecommerce.basketservice.client.response.PlatziProductResponse;
import dev.devdreamer.ecommerce.basketservice.domain.basket.Basket;
import dev.devdreamer.ecommerce.basketservice.domain.product.Product;
import dev.devdreamer.ecommerce.basketservice.domain.user.User;
import dev.devdreamer.ecommerce.basketservice.dto.basket.BasketResponseDTO;
import dev.devdreamer.ecommerce.basketservice.mapper.BasketMapper;
import dev.devdreamer.ecommerce.basketservice.repository.BasketRepository;
import dev.devdreamer.ecommerce.basketservice.security.util.SecurityUtils;
import dev.devdreamer.ecommerce.basketservice.service.BasketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


import java.math.BigDecimal;


@ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private BasketService basketService;

    private User mockUser;
    private Basket mockBasket;
    private PlatziProductResponse mockProductResponse;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id("user-001")
                .email("john@example.com")
                .password("encoded_secret")
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .build();

        mockBasket = Basket.create(mockUser.getId());

        mockProductResponse = PlatziProductResponse.builder()
                .id(10L)
                .title("Wireless Headphones")
                .slug("wireless-headphones")
                .price(new BigDecimal("199.99"))
                .description("High quality headphones")
                .build();
    }

    // -------------------------------------------------------------------------
    // addItem
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("addItem()")
    class AddItem {

        @Test
        @DisplayName("deve criar novo carrinho quando usuário ainda não tem um")
        void shouldCreateNewBasketWhenNoneExists() {
            try (MockedStatic<SecurityUtils> secUtils = mockStatic(SecurityUtils.class)) {
                secUtils.when(SecurityUtils::getAuthenticatedUserId).thenReturn(mockUser);

                when(basketRepository.findByUserId(mockUser.getId())).thenReturn(Optional.empty());
                when(productClient.findById(10L)).thenReturn(mockProductResponse);

                assertThatNoException().isThrownBy(() -> basketService.addItem(10L, 2));

                verify(basketRepository).save(any(Basket.class));
            }
        }

        @Test
        @DisplayName("deve reusar carrinho existente do usuário")
        void shouldReuseExistingBasket() {
            try (MockedStatic<SecurityUtils> secUtils = mockStatic(SecurityUtils.class)) {
                secUtils.when(SecurityUtils::getAuthenticatedUserId).thenReturn(mockUser);

                when(basketRepository.findByUserId(mockUser.getId())).thenReturn(Optional.of(mockBasket));
                when(productClient.findById(10L)).thenReturn(mockProductResponse);

                basketService.addItem(10L, 3);

                verify(basketRepository).save(mockBasket);
            }
        }

        @Test
        @DisplayName("deve buscar produto com o id correto no client externo")
        void shouldFetchProductByCorrectId() {
            try (MockedStatic<SecurityUtils> secUtils = mockStatic(SecurityUtils.class)) {
                secUtils.when(SecurityUtils::getAuthenticatedUserId).thenReturn(mockUser);

                when(basketRepository.findByUserId(mockUser.getId())).thenReturn(Optional.of(mockBasket));
                when(productClient.findById(10L)).thenReturn(mockProductResponse);

                basketService.addItem(10L, 1);

                verify(productClient).findById(10L);
            }
        }

        @Test
        @DisplayName("deve mapear id, title e price do PlatziProductResponse para o Product")
        void shouldMapProductFieldsCorrectly() {
            try (MockedStatic<SecurityUtils> secUtils = mockStatic(SecurityUtils.class)) {
                secUtils.when(SecurityUtils::getAuthenticatedUserId).thenReturn(mockUser);

                Basket spyBasket = spy(mockBasket);
                when(basketRepository.findByUserId(mockUser.getId())).thenReturn(Optional.of(spyBasket));
                when(productClient.findById(10L)).thenReturn(mockProductResponse);

                basketService.addItem(10L, 2);

                verify(spyBasket).addItem(
                        argThat(p ->
                                p.getId().equals(10L) &&
                                        p.getName().equals("Wireless Headphones") &&
                                        p.getUnitPrice().compareTo(new BigDecimal("199.99")) == 0
                        ),
                        eq(2)
                );
            }
        }

        @Test
        @DisplayName("deve salvar o carrinho após adicionar item")
        void shouldSaveBasketAfterAddingItem() {
            try (MockedStatic<SecurityUtils> secUtils = mockStatic(SecurityUtils.class)) {
                secUtils.when(SecurityUtils::getAuthenticatedUserId).thenReturn(mockUser);

                when(basketRepository.findByUserId(mockUser.getId())).thenReturn(Optional.of(mockBasket));
                when(productClient.findById(10L)).thenReturn(mockProductResponse);

                basketService.addItem(10L, 5);

                verify(basketRepository, times(1)).save(mockBasket);
            }
        }
    }

    // -------------------------------------------------------------------------
    // updateQuantity
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("updateQuantity()")
    class UpdateQuantity {

        @Test
        @DisplayName("deve atualizar quantidade e salvar o carrinho")
        void shouldUpdateQuantityAndSave() {
            try (MockedStatic<SecurityUtils> secUtils = mockStatic(SecurityUtils.class)) {
                secUtils.when(SecurityUtils::getAuthenticatedUserId).thenReturn(mockUser);

                // o item precisa existir no carrinho antes de atualizar
                Product product = Product.fromExternal(
                        mockProductResponse.id(),
                        mockProductResponse.title(),
                        mockProductResponse.price()
                );
                mockBasket.addItem(product, 1);

                Basket spyBasket = spy(mockBasket);
                when(basketRepository.findByUserId(mockUser.getId())).thenReturn(Optional.of(spyBasket));

                basketService.updateQuantity(10L, 7);

                verify(spyBasket).updateQuantity(10L, 7);
                verify(basketRepository).save(spyBasket);
            }
        }

        @Test
        @DisplayName("deve lançar IllegalArgumentException quando item não existe no carrinho")
        void shouldThrowWhenItemNotInBasket() {
            try (MockedStatic<SecurityUtils> secUtils = mockStatic(SecurityUtils.class)) {
                secUtils.when(SecurityUtils::getAuthenticatedUserId).thenReturn(mockUser);

                // carrinho existe mas está vazio — nenhum item com productId 10
                when(basketRepository.findByUserId(mockUser.getId())).thenReturn(Optional.of(mockBasket));

                assertThatThrownBy(() -> basketService.updateQuantity(10L, 7))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("There's no Basket Item");

                verify(basketRepository, never()).save(any());
            }
        }

        @Test
        @DisplayName("deve lançar RuntimeException quando carrinho não existe")
        void shouldThrowWhenBasketNotFound() {
            try (MockedStatic<SecurityUtils> secUtils = mockStatic(SecurityUtils.class)) {
                secUtils.when(SecurityUtils::getAuthenticatedUserId).thenReturn(mockUser);

                when(basketRepository.findByUserId(mockUser.getId())).thenReturn(Optional.empty());

                assertThatThrownBy(() -> basketService.updateQuantity(10L, 7))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("Basket not found");

                verify(basketRepository, never()).save(any());
            }
        }
    }

    // -------------------------------------------------------------------------
    // removeItem
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("removeItem()")
    class RemoveItem {

        @Test
        @DisplayName("deve remover o item e salvar o carrinho")
        void shouldRemoveItemAndSave() {
            try (MockedStatic<SecurityUtils> secUtils = mockStatic(SecurityUtils.class)) {
                secUtils.when(SecurityUtils::getAuthenticatedUserId).thenReturn(mockUser);

                // o item precisa existir no carrinho antes de remover
                Product product = Product.fromExternal(
                        mockProductResponse.id(),
                        mockProductResponse.title(),
                        mockProductResponse.price()
                );
                mockBasket.addItem(product, 1);

                Basket spyBasket = spy(mockBasket);
                when(basketRepository.findByUserId(mockUser.getId())).thenReturn(Optional.of(spyBasket));

                basketService.removeItem(10L);

                verify(spyBasket).removeItem(10L);
                verify(basketRepository).save(spyBasket);
            }
        }

        @Test
        @DisplayName("deve salvar carrinho mesmo quando item não existe (removeItem é silencioso)")
        void shouldSaveEvenWhenItemNotFound() {
            try (MockedStatic<SecurityUtils> secUtils = mockStatic(SecurityUtils.class)) {
                secUtils.when(SecurityUtils::getAuthenticatedUserId).thenReturn(mockUser);

                // carrinho vazio — removeItem não lança exceção, apenas ignora
                when(basketRepository.findByUserId(mockUser.getId())).thenReturn(Optional.of(mockBasket));

                assertThatNoException().isThrownBy(() -> basketService.removeItem(999L));
                verify(basketRepository).save(mockBasket);
            }
        }

        @Test
        @DisplayName("deve lançar RuntimeException quando carrinho não existe")
        void shouldThrowWhenBasketNotFound() {
            try (MockedStatic<SecurityUtils> secUtils = mockStatic(SecurityUtils.class)) {
                secUtils.when(SecurityUtils::getAuthenticatedUserId).thenReturn(mockUser);

                when(basketRepository.findByUserId(mockUser.getId())).thenReturn(Optional.empty());

                assertThatThrownBy(() -> basketService.removeItem(10L))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("Basket not found");

                verify(basketRepository, never()).save(any());
            }
        }
    }

    // -------------------------------------------------------------------------
    // getMyBasket
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("getMyBasket()")
    class GetMyBasket {

        @Test
        @DisplayName("deve retornar DTO do carrinho do usuário autenticado")
        void shouldReturnBasketDto() {
            try (MockedStatic<SecurityUtils> secUtils = mockStatic(SecurityUtils.class);
                 MockedStatic<BasketMapper> mapperUtils = mockStatic(BasketMapper.class)) {

                secUtils.when(SecurityUtils::getAuthenticatedUserId).thenReturn(mockUser);
                when(basketRepository.findByUserId(mockUser.getId())).thenReturn(Optional.of(mockBasket));

                BasketResponseDTO expectedDto = mock(BasketResponseDTO.class);
                mapperUtils.when(() -> BasketMapper.toDto(mockBasket)).thenReturn(expectedDto);

                BasketResponseDTO result = basketService.getMyBasket();

                assertThat(result).isEqualTo(expectedDto);
                verify(basketRepository).findByUserId(mockUser.getId());
            }
        }

        @Test
        @DisplayName("deve lançar RuntimeException quando carrinho não existe")
        void shouldThrowWhenBasketNotFound() {
            try (MockedStatic<SecurityUtils> secUtils = mockStatic(SecurityUtils.class)) {
                secUtils.when(SecurityUtils::getAuthenticatedUserId).thenReturn(mockUser);

                when(basketRepository.findByUserId(mockUser.getId())).thenReturn(Optional.empty());

                assertThatThrownBy(() -> basketService.getMyBasket())
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("Basket not found");
            }
        }
    }

    // -------------------------------------------------------------------------
    // getAllBaskets
    // -------------------------------------------------------------------------
    @Nested
    @DisplayName("getAllBaskets()")
    class GetAllBaskets {

        @Test
        @DisplayName("deve retornar lista de DTOs de todos os carrinhos")
        void shouldReturnAllBasketDtos() {
            try (MockedStatic<BasketMapper> mapperUtils = mockStatic(BasketMapper.class)) {
                List<Basket> baskets = List.of(mockBasket, Basket.create("user-002"));
                when(basketRepository.findAll()).thenReturn(baskets);

                List<BasketResponseDTO> expectedDtos = List.of(
                        mock(BasketResponseDTO.class),
                        mock(BasketResponseDTO.class)
                );
                mapperUtils.when(() -> BasketMapper.toDtoList(baskets)).thenReturn(expectedDtos);

                List<BasketResponseDTO> result = basketService.getAllBaskets();

                assertThat(result).hasSize(2).isEqualTo(expectedDtos);
                verify(basketRepository).findAll();
            }
        }

        @Test
        @DisplayName("deve retornar lista vazia quando não há carrinhos")
        void shouldReturnEmptyListWhenNoBaskets() {
            try (MockedStatic<BasketMapper> mapperUtils = mockStatic(BasketMapper.class)) {
                when(basketRepository.findAll()).thenReturn(List.of());
                mapperUtils.when(() -> BasketMapper.toDtoList(List.of())).thenReturn(List.of());

                List<BasketResponseDTO> result = basketService.getAllBaskets();

                assertThat(result).isEmpty();
            }
        }
    }
}