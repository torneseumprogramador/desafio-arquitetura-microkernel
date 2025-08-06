package plugins.order.services;

import plugins.order.entities.Order;
import plugins.order.entities.OrderProduct;
import plugins.order.repositories.OrderRepository;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service para lógica de negócio de pedidos.
 * Pertence ao plugin de gerenciamento de pedidos.
 */
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Cria um novo pedido.
     */
    public Order createOrder(Integer userId, BigDecimal totalAmount) throws SQLException {
        if (userId == null) {
            throw new IllegalArgumentException("ID do usuário é obrigatório");
        }
        if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor total deve ser maior que zero");
        }

        Order order = new Order(userId, totalAmount);
        return orderRepository.save(order);
    }

    /**
     * Busca pedido por ID.
     */
    public Optional<Order> findOrderById(Integer id) throws SQLException {
        if (id == null) {
            return Optional.empty();
        }

        return orderRepository.findById(id);
    }

    /**
     * Lista pedidos por usuário.
     */
    public List<Order> findOrdersByUserId(Integer userId) throws SQLException {
        if (userId == null) {
            throw new IllegalArgumentException("ID do usuário é obrigatório");
        }

        return orderRepository.findByUserId(userId);
    }

    /**
     * Atualiza o status de um pedido.
     */
    public void updateOrderStatus(Integer orderId, String status) throws SQLException {
        if (orderId == null) {
            throw new IllegalArgumentException("ID do pedido é obrigatório");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status é obrigatório");
        }

        orderRepository.updateStatus(orderId, status.trim());
    }

    /**
     * Adiciona um produto ao pedido.
     */
    public OrderProduct addProductToOrder(Integer orderId, Integer productId, Integer quantity, BigDecimal unitPrice) throws SQLException {
        if (orderId == null) {
            throw new IllegalArgumentException("ID do pedido é obrigatório");
        }
        if (productId == null) {
            throw new IllegalArgumentException("ID do produto é obrigatório");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço unitário deve ser maior que zero");
        }

        OrderProduct orderProduct = new OrderProduct(orderId, productId, quantity, unitPrice);
        return orderRepository.addProductToOrder(orderProduct);
    }

    /**
     * Busca produtos de um pedido.
     */
    public List<OrderProduct> getOrderProducts(Integer orderId) throws SQLException {
        if (orderId == null) {
            throw new IllegalArgumentException("ID do pedido é obrigatório");
        }

        return orderRepository.findProductsByOrderId(orderId);
    }

    /**
     * Finaliza um pedido (muda status para PAID).
     */
    public void finalizeOrder(Integer orderId) throws SQLException {
        updateOrderStatus(orderId, "PAID");
    }

    /**
     * Cancela um pedido (muda status para CANCELLED).
     */
    public void cancelOrder(Integer orderId) throws SQLException {
        updateOrderStatus(orderId, "CANCELLED");
    }
} 