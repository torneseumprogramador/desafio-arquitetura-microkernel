package core;

import core.controllers.HomeController;
import core.controllers.HealthController;
import java.util.List;
import java.util.ArrayList;

/**
 * Configuração de rotas para o core da aplicação.
 * Define rotas básicas como home, health e documentação.
 */
public class CoreRoutes {
    
    private final HomeController homeController;
    private final HealthController healthController;
    private final RouteRegistry routeRegistry;
    private final List<RouteRegistry.RouteDefinition> routeDefinitions;
    
    public CoreRoutes() {
        this.homeController = new HomeController();
        this.healthController = new HealthController();
        this.routeRegistry = new RouteRegistry();
        this.routeDefinitions = new ArrayList<>();
        setupRoutes();
    }
    
    /**
     * Configura todas as rotas do core da aplicação.
     */
    private void setupRoutes() {
        // Rotas do Home
        addRoute("GET", "/", "getHome", homeController);
        addRoute("GET", "/api", "getApiInfo", homeController);
        addRoute("GET", "/api/docs", "getApiDocs", homeController);
        
        // Rotas do Health
        addRoute("GET", "/api/health", "getHealth", healthController);
        addRoute("GET", "/api/health/detailed", "getDetailedHealth", healthController);
        addRoute("GET", "/api/health/database", "getDatabaseHealth", healthController);
        
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
     * @return RouteRegistry com todas as rotas do core
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