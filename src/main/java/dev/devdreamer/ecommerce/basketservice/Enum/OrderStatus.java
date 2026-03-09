package dev.devdreamer.ecommerce.basketservice.Enum;

public enum OrderStatus {
    PENDING,        // Pedido criado, aguardando pagamento
    PAID,           // Pagamento confirmado
    PROCESSING,     // Separando/preparando o pedido
    SHIPPED,        // Pedido enviado
    DELIVERED,      // Pedido entregue
    CANCELLED,      // Pedido cancelado
    REFUNDED        // Pagamento estornado
}
