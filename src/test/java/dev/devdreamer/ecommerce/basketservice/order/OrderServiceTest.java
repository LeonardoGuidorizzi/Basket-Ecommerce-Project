package dev.devdreamer.ecommerce.basketservice.order;

import dev.devdreamer.ecommerce.basketservice.Enum.BasketStatus;
import dev.devdreamer.ecommerce.basketservice.Enum.OrderStatus;
import dev.devdreamer.ecommerce.basketservice.domain.basket.Basket;
import dev.devdreamer.ecommerce.basketservice.domain.basket.BasketItem;
import dev.devdreamer.ecommerce.basketservice.domain.order.Order;
import dev.devdreamer.ecommerce.basketservice.domain.user.User;
import dev.devdreamer.ecommerce.basketservice.dto.order.OrderResponseDTO;
import dev.devdreamer.ecommerce.basketservice.mapper.OrderMapper;
import dev.devdreamer.ecommerce.basketservice.repository.BasketRepository;
import dev.devdreamer.ecommerce.basketservice.repository.OrderRepository;
import dev.devdreamer.ecommerce.basketservice.security.util.SecurityUtils;
import dev.devdreamer.ecommerce.basketservice.service.OrderService;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = User.builder().id("user-1").build();
    }

    // ─────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────

    /** Basket com 1 item válido, pronto para checkout */
    private Basket basketWithItems() {
        BasketItem item = BasketItem.builder()
                .productId(10L)
                .productName("Notebook")
                .unitPrice(new BigDecimal("299.99"))
                .quantity(2)
                .build();

        return Basket.builder()
                .id("basket-1")
                .userId("user-1")
                .items( new ArrayList<>(List.of(item)))
                .totalAmount(new BigDecimal("599.98"))
                .status(BasketStatus.ACTIVE)
                .build();
    }

    /** Basket sem itens */
    private Basket emptyBasket() {
        return Basket.builder()
                .id("basket-1")
                .userId("user-1")
                .items(List.of())
                .totalAmount(BigDecimal.ZERO)
                .status(BasketStatus.ACTIVE)
                .build();
    }

    private OrderResponseDTO stubDto() {
        return OrderResponseDTO.builder().build();
    }

    // ─────────────────────────────────────────────────────────────
    // checkout
    // ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("checkout → salva order com status PENDING")
    void checkout_shouldSaveOrderWithPendingStatus() {
        Basket basket = basketWithItems();

        try (MockedStatic<SecurityUtils> sec = mockStatic(SecurityUtils.class);
             MockedStatic<OrderMapper> mapper = mockStatic(OrderMapper.class)) {

            sec.when(SecurityUtils::getAuthenticatedUserId).thenReturn(mockUser);
            when(basketRepository.findByUserId("user-1")).thenReturn(Optional.of(basket));
            when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));
            mapper.when(() -> OrderMapper.toDto(any(Order.class))).thenReturn(stubDto());

            orderService.checkout();

            verify(orderRepository).save(argThat(order ->
                    order.getStatus() == OrderStatus.PENDING &&
                            order.getUserId().equals("user-1") &&
                            order.getBasketId().equals("basket-1")
            ));
        }
    }

    @Test
    @DisplayName("checkout → basket fica com status CHECKED_OUT após checkout")
    void checkout_shouldMarkBasketAsCheckedOut() {
        Basket basket = spy(basketWithItems());

        try (MockedStatic<SecurityUtils> sec = mockStatic(SecurityUtils.class);
             MockedStatic<OrderMapper> mapper = mockStatic(OrderMapper.class)) {

            sec.when(SecurityUtils::getAuthenticatedUserId).thenReturn(mockUser);
            when(basketRepository.findByUserId("user-1")).thenReturn(Optional.of(basket));
            when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
            mapper.when(() -> OrderMapper.toDto(any())).thenReturn(stubDto());

            orderService.checkout();

            verify(basket).clear();
            verify(basket).updateBasketStatus(BasketStatus.CHECKED_OUT);
            verify(basketRepository).save(basket);
        }
    }

    @Test
    @DisplayName("checkout → retorna DTO da order criada")
    void checkout_shouldReturnOrderDto() {
        Basket basket = basketWithItems();
        OrderResponseDTO expectedDto = OrderResponseDTO.builder().build();

        try (MockedStatic<SecurityUtils> sec = mockStatic(SecurityUtils.class);
             MockedStatic<OrderMapper> mapper = mockStatic(OrderMapper.class)) {

            sec.when(SecurityUtils::getAuthenticatedUserId).thenReturn(mockUser);
            when(basketRepository.findByUserId("user-1")).thenReturn(Optional.of(basket));
            when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
            mapper.when(() -> OrderMapper.toDto(any(Order.class))).thenReturn(expectedDto);

            OrderResponseDTO result = orderService.checkout();

            assertThat(result).isEqualTo(expectedDto);
        }
    }

    @Test
    @DisplayName("checkout → lança RuntimeException quando basket não encontrado")
    void checkout_shouldThrow_whenBasketNotFound() {
        try (MockedStatic<SecurityUtils> sec = mockStatic(SecurityUtils.class)) {
            sec.when(SecurityUtils::getAuthenticatedUserId).thenReturn(mockUser);
            when(basketRepository.findByUserId("user-1")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> orderService.checkout())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Basket not found");

            verifyNoInteractions(orderRepository);
        }
    }

    @Test
    @DisplayName("checkout → lança IllegalStateException quando basket está vazio")
    void checkout_shouldThrow_whenBasketIsEmpty() {
        try (MockedStatic<SecurityUtils> sec = mockStatic(SecurityUtils.class)) {
            sec.when(SecurityUtils::getAuthenticatedUserId).thenReturn(mockUser);
            when(basketRepository.findByUserId("user-1")).thenReturn(Optional.of(emptyBasket()));

            assertThatThrownBy(() -> orderService.checkout())
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Cannot checkout an empty basket");

            verifyNoInteractions(orderRepository);
        }
    }

    @Test
    @DisplayName("checkout → lança IllegalStateException quando items é null")
    void checkout_shouldThrow_whenBasketItemsIsNull() {
        Basket basket = Basket.builder()
                .id("basket-1")
                .userId("user-1")
                .items(null)
                .status(BasketStatus.ACTIVE)
                .build();

        try (MockedStatic<SecurityUtils> sec = mockStatic(SecurityUtils.class)) {
            sec.when(SecurityUtils::getAuthenticatedUserId).thenReturn(mockUser);
            when(basketRepository.findByUserId("user-1")).thenReturn(Optional.of(basket));

            assertThatThrownBy(() -> orderService.checkout())
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Cannot checkout an empty basket");

            verifyNoInteractions(orderRepository);
        }
    }

    @Test
    @DisplayName("checkout → basket não é salvo se orderRepository lançar exceção")
    void checkout_shouldNotSaveBasket_whenOrderSaveFails() {
        Basket basket = basketWithItems();

        try (MockedStatic<SecurityUtils> sec = mockStatic(SecurityUtils.class)) {
            sec.when(SecurityUtils::getAuthenticatedUserId).thenReturn(mockUser);
            when(basketRepository.findByUserId("user-1")).thenReturn(Optional.of(basket));
            doThrow(new RuntimeException("DB error")).when(orderRepository).save(any());

            assertThatThrownBy(() -> orderService.checkout())
                    .isInstanceOf(RuntimeException.class);

            verify(basketRepository, never()).save(any());
        }
    }

    // ─────────────────────────────────────────────────────────────
    // getMyOrders
    // ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getMyOrders → retorna lista de orders do usuário autenticado")
    void getMyOrders_shouldReturnUserOrders() {
        Order order = mock(Order.class);
        OrderResponseDTO dto = stubDto();

        try (MockedStatic<SecurityUtils> sec = mockStatic(SecurityUtils.class);
             MockedStatic<OrderMapper> mapper = mockStatic(OrderMapper.class)) {

            sec.when(SecurityUtils::getAuthenticatedUserId).thenReturn(mockUser);
            when(orderRepository.findByUserId("user-1")).thenReturn(List.of(order));
            mapper.when(() -> OrderMapper.toDtoList(any())).thenReturn(List.of(dto)); // ← any()

            List<OrderResponseDTO> result = orderService.getMyOrders();

            assertThat(result)
                    .asInstanceOf(InstanceOfAssertFactories.LIST)
                    .hasSize(1)
                    .containsExactly(dto);
            verify(orderRepository).findByUserId("user-1");
        }
    }

    @Test
    @DisplayName("getMyOrders → retorna lista vazia quando usuário não tem orders")
    void getMyOrders_shouldReturnEmptyList_whenNoOrders() {
        try (MockedStatic<SecurityUtils> sec = mockStatic(SecurityUtils.class);
             MockedStatic<OrderMapper> mapper = mockStatic(OrderMapper.class)) {

            sec.when(SecurityUtils::getAuthenticatedUserId).thenReturn(mockUser);
            when(orderRepository.findByUserId("user-1")).thenReturn(List.of());
            mapper.when(() -> OrderMapper.toDtoList(List.of())).thenReturn(List.of());

            assertThat(orderService.getMyOrders()).isEqualTo(List.of());
        }
    }

    // ─────────────────────────────────────────────────────────────
    // getAllOrders
    // ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getAllOrders → retorna todas as orders")
    void getAllOrders_shouldReturnAllOrders() {
        Order o1 = mock(Order.class);
        Order o2 = mock(Order.class);
        List<OrderResponseDTO> dtos = List.of(stubDto(), stubDto());

        try (MockedStatic<OrderMapper> mapper = mockStatic(OrderMapper.class)) {
            when(orderRepository.findAll()).thenReturn(List.of(o1, o2));
            mapper.when(() -> OrderMapper.toDtoList(List.of(o1, o2))).thenReturn(dtos);

            assertThat(orderService.getAllOrders())
                    .asInstanceOf(InstanceOfAssertFactories.LIST)
                    .hasSize(2);
        }
    }

    @Test
    @DisplayName("getAllOrders → retorna lista vazia quando não há orders")
    void getAllOrders_shouldReturnEmptyList_whenNoOrders() {
        try (MockedStatic<OrderMapper> mapper = mockStatic(OrderMapper.class)) {
            when(orderRepository.findAll()).thenReturn(List.of());
            mapper.when(() -> OrderMapper.toDtoList(List.of())).thenReturn(List.of());

            assertThat(orderService.getAllOrders()).isEqualTo(List.of());
        }
    }
}