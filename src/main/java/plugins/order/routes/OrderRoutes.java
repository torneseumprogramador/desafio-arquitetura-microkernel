package plugins.order;

import core.RouteRegistry;
import plugins.order.controllers.OrderController;
import plugins.order.services.OrderService;
import java.util.List;
import java.util.ArrayList;

/**
 * Configuração de rotas para o plugin de pedidos.
 * Define todas as rotas de forma declarativa.
 */
public class OrderRoutes {
    
    private final OrderController controller;
    private final RouteRegistry routeRegistry;
    
    public OrderRoutes(OrderService orderService) {
        this.controller = new OrderController(orderService);
        this.routeRegistry = new RouteRegistry();
        setupRoutes();
    }
    
    /**
     * Configura todas as rotas do plugin de pedidos.
     */
    private void setupRoutes() {
        List<RouteRegistry.RouteDefinition> routes = new ArrayList<>();
        
        // Rotas de pedidos
        routes.add(new RouteRegistry.RouteDefinition("GET", "/api/orders", "listOrders", controller));
        routes.add(new RouteRegistry.RouteDefinition("GET", "/api/orders/{id}", "getOrderById", controller));
        routes.add(new RouteRegistry.RouteDefinition("GET", "/api/orders/user/{userId}", "getOrdersByUserId", controller));
        routes.add(new RouteRegistry.RouteDefinition("POST", "/api/orders", "createOrder", controller));
        routes.add(new RouteRegistry.RouteDefinition("POST", "/api/orders/{id}/products", "addProductToOrder", controller));
        routes.add(new RouteRegistry.RouteDefinition("PUT", "/api/orders/{id}", "updateOrder", controller));
        routes.add(new RouteRegistry.RouteDefinition("PUT", "/api/orders/{id}/finalize", "finalizeOrder", controller));
        routes.add(new RouteRegistry.RouteDefinition("DELETE", "/api/orders/{id}", "deleteOrder", controller));
        
        routeRegistry.addRoutes(routes);
    }
    
    /**
     * Retorna o RouteRegistry configurado.
     * @return RouteRegistry com todas as rotas de pedidos
     */
    public RouteRegistry getRouteRegistry() {
        return routeRegistry;
    }
    
    /**
     * Lista todas as rotas configuradas.
     * @return Lista de rotas para debug/documentação
     */
    public List<RouteRegistry.RouteDefinition> getRouteDefinitions() {
        List<RouteRegistry.RouteDefinition> definitions = new ArrayList<>();
        
        definitions.add(new RouteRegistry.RouteDefinition("GET", "/api/orders", "listOrders", controller));
        definitions.add(new RouteRegistry.RouteDefinition("GET", "/api/orders/{id}", "getOrderById", controller));
        definitions.add(new RouteRegistry.RouteDefinition("GET", "/api/orders/user/{userId}", "getOrdersByUserId", controller));
        definitions.add(new RouteRegistry.RouteDefinition("POST", "/api/orders", "createOrder", controller));
        definitions.add(new RouteRegistry.RouteDefinition("POST", "/api/orders/{id}/products", "addProductToOrder", controller));
        definitions.add(new RouteRegistry.RouteDefinition("PUT", "/api/orders/{id}", "updateOrder", controller));
        definitions.add(new RouteRegistry.RouteDefinition("PUT", "/api/orders/{id}/finalize", "finalizeOrder", controller));
        definitions.add(new RouteRegistry.RouteDefinition("DELETE", "/api/orders/{id}", "deleteOrder", controller));
        
        return definitions;
    }
} 