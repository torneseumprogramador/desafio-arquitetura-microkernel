package plugins.order.routes;

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
    private final List<RouteRegistry.RouteDefinition> routeDefinitions;
    
    public OrderRoutes(OrderService orderService) {
        this.controller = new OrderController(orderService);
        this.routeRegistry = new RouteRegistry();
        this.routeDefinitions = new ArrayList<>();
        setupRoutes();
    }
    
    /**
     * Configura todas as rotas do plugin de pedidos.
     */
    private void setupRoutes() {
        // Rotas de pedidos
        addRoute("GET", "/api/orders", "listOrders", controller);
        addRoute("GET", "/api/orders/{id}", "getOrderById", controller);
        addRoute("GET", "/api/orders/user/{userId}", "getOrdersByUserId", controller);
        addRoute("POST", "/api/orders", "createOrder", controller);
        addRoute("POST", "/api/orders/{id}/products", "addProductToOrder", controller);
        addRoute("PUT", "/api/orders/{id}", "updateOrder", controller);
        addRoute("PUT", "/api/orders/{id}/finalize", "finalizeOrder", controller);
        addRoute("DELETE", "/api/orders/{id}", "deleteOrder", controller);
        
        routeRegistry.addRoutes(routeDefinitions);
    }
    
    /**
     * Adiciona uma rota à lista de definições e ao registro.
     * @param method Método HTTP
     * @param path Path da rota
     * @param handlerMethod Nome do método handler
     * @param controller Controller
     */
    private void addRoute(String method, String path, String handlerMethod, Object controller) {
        RouteRegistry.RouteDefinition routeDef = new RouteRegistry.RouteDefinition(method, path, handlerMethod, controller);
        routeDefinitions.add(routeDef);
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
        return new ArrayList<>(routeDefinitions);
    }
} 