package core;

import core.controllers.HomeController;
import core.controllers.HealthController;
import core.controllers.SwaggerController;
import java.util.List;

/**
 * Configuração das rotas do core da aplicação.
 * Define endpoints básicos como home, health e swagger.
 */
public class CoreRoutes {
    private final RouteRegistry routeRegistry;
    private final List<Route> routeDefinitions;

    public CoreRoutes() {
        this.routeRegistry = new RouteRegistry();
        this.routeDefinitions = createRouteDefinitions();
        setupRoutes();
    }

    private List<Route> createRouteDefinitions() {
        return List.of(
            // Home routes
            new Route("GET", "/", "getHome", new HomeController()),
            new Route("GET", "/api", "getApiInfo", new HomeController()),
            new Route("GET", "/api/docs", "getApiDocs", new HomeController()),
            
            // Health routes
            new Route("GET", "/api/health", "getHealth", new HealthController()),
            new Route("GET", "/api/health/detailed", "getHealthDetailed", new HealthController()),
            new Route("GET", "/api/health/database", "getHealthDatabase", new HealthController()),
            
            // Swagger routes
            new Route("GET", "/api/swagger", "getSwaggerJson", new SwaggerController()),
            new Route("GET", "/api/swagger-ui", "getSwaggerUi", new SwaggerController())
        );
    }

    private void setupRoutes() {
        for (Route route : routeDefinitions) {
            routeRegistry.addRoute(route.getMethod(), route.getPath(), route.getHandlerMethod(), route.getController());
        }
    }

    public RouteRegistry getRouteRegistry() {
        return routeRegistry;
    }

    public List<Route> getRouteDefinitions() {
        return routeDefinitions;
    }
} 