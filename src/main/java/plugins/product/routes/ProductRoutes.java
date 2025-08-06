package plugins.product;

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
    
    public ProductRoutes(ProductService productService) {
        this.controller = new ProductController(productService);
        this.routeRegistry = new RouteRegistry();
        setupRoutes();
    }
    
    /**
     * Configura todas as rotas do plugin de produtos.
     */
    private void setupRoutes() {
        List<RouteRegistry.RouteDefinition> routes = new ArrayList<>();
        
        // Rotas de produtos
        routes.add(new RouteRegistry.RouteDefinition("GET", "/api/products", "listProducts", controller));
        routes.add(new RouteRegistry.RouteDefinition("GET", "/api/products/available", "listAvailableProducts", controller));
        routes.add(new RouteRegistry.RouteDefinition("GET", "/api/products/{id}", "getProductById", controller));
        routes.add(new RouteRegistry.RouteDefinition("POST", "/api/products", "createProduct", controller));
        routes.add(new RouteRegistry.RouteDefinition("PUT", "/api/products/{id}", "updateProduct", controller));
        routes.add(new RouteRegistry.RouteDefinition("PUT", "/api/products/{id}/stock", "updateStock", controller));
        routes.add(new RouteRegistry.RouteDefinition("DELETE", "/api/products/{id}", "deleteProduct", controller));
        
        routeRegistry.addRoutes(routes);
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
        List<RouteRegistry.RouteDefinition> definitions = new ArrayList<>();
        
        definitions.add(new RouteRegistry.RouteDefinition("GET", "/api/products", "listProducts", controller));
        definitions.add(new RouteRegistry.RouteDefinition("GET", "/api/products/available", "listAvailableProducts", controller));
        definitions.add(new RouteRegistry.RouteDefinition("GET", "/api/products/{id}", "getProductById", controller));
        definitions.add(new RouteRegistry.RouteDefinition("POST", "/api/products", "createProduct", controller));
        definitions.add(new RouteRegistry.RouteDefinition("PUT", "/api/products/{id}", "updateProduct", controller));
        definitions.add(new RouteRegistry.RouteDefinition("PUT", "/api/products/{id}/stock", "updateStock", controller));
        definitions.add(new RouteRegistry.RouteDefinition("DELETE", "/api/products/{id}", "deleteProduct", controller));
        
        return definitions;
    }
} 