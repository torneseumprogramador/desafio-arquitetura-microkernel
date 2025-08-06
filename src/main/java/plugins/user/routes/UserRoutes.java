package plugins.user;

import core.RouteRegistry;
import plugins.user.controllers.UserController;
import plugins.user.services.UserService;
import java.util.List;
import java.util.ArrayList;

/**
 * Configuração de rotas para o plugin de usuários.
 * Define todas as rotas de forma declarativa.
 */
public class UserRoutes {
    
    private final UserController controller;
    private final RouteRegistry routeRegistry;
    
    public UserRoutes(UserService userService) {
        this.controller = new UserController(userService);
        this.routeRegistry = new RouteRegistry();
        setupRoutes();
    }
    
    /**
     * Configura todas as rotas do plugin de usuários.
     */
    private void setupRoutes() {
        List<RouteRegistry.RouteDefinition> routes = new ArrayList<>();
        
        // Rotas de usuários
        routes.add(new RouteRegistry.RouteDefinition("GET", "/api/users", "listUsers", controller));
        routes.add(new RouteRegistry.RouteDefinition("GET", "/api/users/{id}", "getUserById", controller));
        routes.add(new RouteRegistry.RouteDefinition("POST", "/api/users", "createUser", controller));
        routes.add(new RouteRegistry.RouteDefinition("PUT", "/api/users/{id}", "updateUser", controller));
        routes.add(new RouteRegistry.RouteDefinition("DELETE", "/api/users/{id}", "deleteUser", controller));
        
        routeRegistry.addRoutes(routes);
    }
    
    /**
     * Retorna o RouteRegistry configurado.
     * @return RouteRegistry com todas as rotas de usuários
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
        
        definitions.add(new RouteRegistry.RouteDefinition("GET", "/api/users", "listUsers", controller));
        definitions.add(new RouteRegistry.RouteDefinition("GET", "/api/users/{id}", "getUserById", controller));
        definitions.add(new RouteRegistry.RouteDefinition("POST", "/api/users", "createUser", controller));
        definitions.add(new RouteRegistry.RouteDefinition("PUT", "/api/users/{id}", "updateUser", controller));
        definitions.add(new RouteRegistry.RouteDefinition("DELETE", "/api/users/{id}", "deleteUser", controller));
        
        return definitions;
    }
} 