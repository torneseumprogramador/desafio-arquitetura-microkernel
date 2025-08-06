package plugins.order.controllers;

import core.SimpleController;
import plugins.order.services.OrderService;
import plugins.order.entities.Order;
import plugins.order.entities.OrderProduct;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

/**
 * Controller de pedidos usando sistema de rotas declarativo.
 * Cada método representa um endpoint específico.
 */
public class OrderController extends SimpleController {
    
    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    /**
     * GET /api/orders - Lista todos os pedidos
     */
    public void listOrders(HttpExchange exchange) throws IOException {
        try {
            List<Order> orders = orderService.getAllOrders();
            String response = buildOrdersJson(orders);
            sendJsonResponse(exchange, 200, response);
        } catch (SQLException e) {
            sendError(exchange, 500, "Database error: " + e.getMessage());
        }
    }
    
    /**
     * GET /api/orders/{id} - Busca pedido por ID
     */
    public void getOrderById(HttpExchange exchange) throws IOException {
        try {
            String path = getPath(exchange);
            int orderId = extractId(path);
            
            Optional<Order> order = orderService.findOrderById(orderId);
            if (order.isPresent()) {
                String response = buildOrderJson(order.get());
                sendJsonResponse(exchange, 200, response);
            } else {
                sendError(exchange, 404, "Order not found");
            }
        } catch (SQLException e) {
            sendError(exchange, 500, "Database error: " + e.getMessage());
        }
    }
    
    /**
     * GET /api/orders/user/{userId} - Busca pedidos por usuário
     */
    public void getOrdersByUserId(HttpExchange exchange) throws IOException {
        try {
            String path = getPath(exchange);
            int userId = extractId(path);
            
            List<Order> orders = orderService.findOrdersByUserId(userId);
            String response = buildOrdersJson(orders);
            sendJsonResponse(exchange, 200, response);
        } catch (SQLException e) {
            sendError(exchange, 500, "Database error: " + e.getMessage());
        }
    }
    
    /**
     * POST /api/orders - Cria novo pedido
     */
    public void createOrder(HttpExchange exchange) throws IOException {
        try {
            // TODO: Implementar leitura do body da requisição
            Order newOrder = orderService.createOrder(1, new BigDecimal("99.99"));
            String response = "{\"message\":\"Pedido criado com sucesso\",\"id\":" + newOrder.getId() + "}";
            sendJsonResponse(exchange, 201, response);
        } catch (IllegalArgumentException e) {
            sendError(exchange, 400, e.getMessage());
        } catch (SQLException e) {
            sendError(exchange, 500, "Database error: " + e.getMessage());
        }
    }
    
    /**
     * POST /api/orders/{id}/products - Adiciona produto ao pedido
     */
    public void addProductToOrder(HttpExchange exchange) throws IOException {
        try {
            String path = getPath(exchange);
            int orderId = extractId(path);
            
            // TODO: Implementar leitura do body da requisição
            OrderProduct orderProduct = orderService.addProductToOrder(
                orderId, 1, 1, new BigDecimal("99.99")
            );
            String response = "{\"message\":\"Produto adicionado ao pedido\",\"id\":" + orderProduct.getId() + "}";
            sendJsonResponse(exchange, 201, response);
        } catch (IllegalArgumentException e) {
            sendError(exchange, 400, e.getMessage());
        } catch (SQLException e) {
            sendError(exchange, 500, "Database error: " + e.getMessage());
        }
    }
    
    /**
     * PUT /api/orders/{id} - Atualiza pedido
     */
    public void updateOrder(HttpExchange exchange) throws IOException {
        String path = getPath(exchange);
        int orderId = extractId(path);
        String response = "{\"message\":\"Pedido atualizado com sucesso\",\"id\":" + orderId + "}";
        sendJsonResponse(exchange, 200, response);
    }
    
    /**
     * PUT /api/orders/{id}/finalize - Finaliza pedido
     */
    public void finalizeOrder(HttpExchange exchange) throws IOException {
        try {
            String path = getPath(exchange);
            int orderId = extractId(path);
            
            orderService.finalizeOrder(orderId);
            String response = "{\"message\":\"Pedido finalizado com sucesso\",\"id\":" + orderId + "}";
            sendJsonResponse(exchange, 200, response);
        } catch (IllegalArgumentException e) {
            sendError(exchange, 400, e.getMessage());
        } catch (SQLException e) {
            sendError(exchange, 500, "Database error: " + e.getMessage());
        }
    }
    
    /**
     * DELETE /api/orders/{id} - Deleta pedido
     */
    public void deleteOrder(HttpExchange exchange) throws IOException {
        String path = getPath(exchange);
        int orderId = extractId(path);
        String response = "{\"message\":\"Pedido deletado com sucesso\",\"id\":" + orderId + "}";
        sendJsonResponse(exchange, 200, response);
    }
    
    private String buildOrdersJson(List<Order> orders) {
        StringBuilder json = new StringBuilder("{\"orders\":[");
        for (int i = 0; i < orders.size(); i++) {
            if (i > 0) json.append(",");
            json.append(buildOrderJson(orders.get(i)));
        }
        json.append("]}");
        return json.toString();
    }
    
    private String buildOrderJson(Order order) {
        return "{\"id\":" + order.getId() + 
               ",\"userId\":" + order.getUserId() +
               ",\"totalAmount\":" + order.getTotalAmount() +
               ",\"status\":\"" + order.getStatus() + "\"" +
               ",\"createdAt\":\"" + order.getCreatedAt() + "\"}";
    }
} 