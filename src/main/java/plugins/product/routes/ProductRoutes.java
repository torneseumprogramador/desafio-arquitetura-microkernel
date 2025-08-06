package plugins.product.routes;

import core.RouteRegistry;
import plugins.product.controllers.ProductController;
import plugins.product.services.ProductService;
import java.util.List;
import java.util.ArrayList;

/**
 * Configuração de rotas para o plugin de produtos.
 * Define todas as rotas de forma declarativa.
 */
public class ProductRoutes {
    
    private final ProductController controller;
    private final RouteRegistry routeRegistry;
    private final List<RouteRegistry.RouteDefinition> routeDefinitions;
    
    public ProductRoutes(ProductService productService) {
        this.controller = new ProductController(productService);
        this.routeRegistry = new RouteRegistry();
        this.routeDefinitions = new ArrayList<>();
        setupRoutes();
    }
    
    /**
     * Configura todas as rotas do plugin de produtos.
     */
    private void setupRoutes() {
        // Rotas de produtos
        addRoute("GET", "/api/products", "listProducts", controller);
        addRoute("GET", "/api/products/available", "listAvailableProducts", controller);
        addRoute("GET", "/api/products/{id}", "getProductById", controller);
        addRoute("POST", "/api/products", "createProduct", controller);
        addRoute("PUT", "/api/products/{id}", "updateProduct", controller);
        addRoute("PUT", "/api/products/{id}/stock", "updateStock", controller);
        addRoute("DELETE", "/api/products/{id}", "deleteProduct", controller);
        
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
     * @return RouteRegistry com todas as rotas de produtos
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