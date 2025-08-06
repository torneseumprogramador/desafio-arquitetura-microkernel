package plugins.order.repositories;

import plugins.order.entities.Order;
import plugins.order.entities.OrderProduct;
import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository para acesso aos dados de pedidos.
 * Pertence ao plugin de gerenciamento de pedidos.
 */
public class OrderRepository {
    private final Connection connection;

    public OrderRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Salva um novo pedido.
     */
    public Order save(Order order) throws SQLException {
        String sql = "INSERT INTO orders (user_id, total_amount, status) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, order.getUserId());
            pstmt.setBigDecimal(2, order.getTotalAmount());
            pstmt.setString(3, order.getStatus());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    order.setId(rs.getInt(1));
                }
            }
        }
        return order;
    }

    /**
     * Busca pedido por ID.
     */
    public Optional<Order> findById(Integer id) throws SQLException {
        String sql = "SELECT id, user_id, total_amount, status, created_at FROM orders WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getInt("id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setTotalAmount(rs.getBigDecimal("total_amount"));
                    order.setStatus(rs.getString("status"));
                    order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return Optional.of(order);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Lista pedidos por usuário.
     */
    public List<Order> findByUserId(Integer userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT id, user_id, total_amount, status, created_at FROM orders WHERE user_id = ? ORDER BY created_at DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getInt("id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setTotalAmount(rs.getBigDecimal("total_amount"));
                    order.setStatus(rs.getString("status"));
                    order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    /**
     * Atualiza o status de um pedido.
     */
    public void updateStatus(Integer orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, orderId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Adiciona um produto ao pedido.
     */
    public OrderProduct addProductToOrder(OrderProduct orderProduct) throws SQLException {
        String sql = "INSERT INTO order_products (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, orderProduct.getOrderId());
            pstmt.setInt(2, orderProduct.getProductId());
            pstmt.setInt(3, orderProduct.getQuantity());
            pstmt.setBigDecimal(4, orderProduct.getUnitPrice());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    orderProduct.setId(rs.getInt(1));
                }
            }
        }
        return orderProduct;
    }

    /**
     * Busca produtos de um pedido.
     */
    public List<OrderProduct> findProductsByOrderId(Integer orderId) throws SQLException {
        List<OrderProduct> orderProducts = new ArrayList<>();
        String sql = "SELECT id, order_id, product_id, quantity, unit_price FROM order_products WHERE order_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OrderProduct orderProduct = new OrderProduct();
                    orderProduct.setId(rs.getInt("id"));
                    orderProduct.setOrderId(rs.getInt("order_id"));
                    orderProduct.setProductId(rs.getInt("product_id"));
                    orderProduct.setQuantity(rs.getInt("quantity"));
                    orderProduct.setUnitPrice(rs.getBigDecimal("unit_price"));
                    orderProducts.add(orderProduct);
                }
            }
        }
        return orderProducts;
    }
} 