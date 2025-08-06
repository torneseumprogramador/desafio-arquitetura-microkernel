package core;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Registro de rotas que gerencia todas as rotas da aplicação.
 * Permite definir rotas de forma declarativa.
 */
public class RouteRegistry {
    private final List<Route> routes;
    
    public RouteRegistry() {
        this.routes = new ArrayList<>();
    }
    
    /**
     * Adiciona uma rota ao registro.
     * @param method Método HTTP (GET, POST, PUT, DELETE)
     * @param path Path da rota (ex: /api/users/{id})
     * @param handlerMethod Nome do método handler no controller
     * @param controller Instância do controller
     */
    public void addRoute(String method, String path, String handlerMethod, Object controller) {
        routes.add(new Route(method, path, handlerMethod, controller));
    }
    
    /**
     * Adiciona múltiplas rotas de uma vez.
     * @param routeDefinitions Lista de definições de rotas
     */
    public void addRoutes(List<RouteDefinition> routeDefinitions) {
        for (RouteDefinition def : routeDefinitions) {
            addRoute(def.getMethod(), def.getPath(), def.getHandlerMethod(), def.getController());
        }
    }
    
    /**
     * Processa uma requisição HTTP usando as rotas registradas.
     * @param exchange HttpExchange da requisição
     * @throws IOException em caso de erro
     */
    public void handleRequest(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        
        // Procurar rota que pode processar a requisição
        for (Route route : routes) {
            if (route.canHandle(method, path)) {
                executeHandler(route, exchange, path);
                return;
            }
        }
        
        // Nenhuma rota encontrada
        sendNotFound(exchange);
    }
    
    /**
     * Executa o handler da rota usando reflection.
     * @param route Rota encontrada
     * @param exchange HttpExchange
     * @param requestPath Path da requisição
     * @throws IOException em caso de erro
     */
    private void executeHandler(Route route, HttpExchange exchange, String requestPath) throws IOException {
        try {
            Object controller = route.getController();
            String handlerMethod = route.getHandlerMethod();
            
            // Buscar método no controller
            Method method = controller.getClass().getMethod(handlerMethod, HttpExchange.class);
            
            // Executar método
            method.invoke(controller, exchange);
            
        } catch (Exception e) {
            sendError(exchange, 500, "Internal server error: " + e.getMessage());
        }
    }
    
    /**
     * Envia resposta 404 quando nenhuma rota é encontrada.
     * @param exchange HttpExchange
     * @throws IOException em caso de erro
     */
    private void sendNotFound(HttpExchange exchange) throws IOException {
        String response = "{\"error\":\"Not found\"}";
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(404, response.getBytes().length);
        
        try (var os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
    
    /**
     * Envia resposta de erro.
     * @param exchange HttpExchange
     * @param statusCode Código de status HTTP
     * @param errorMessage Mensagem de erro
     * @throws IOException em caso de erro
     */
    private void sendError(HttpExchange exchange, int statusCode, String errorMessage) throws IOException {
        String response = "{\"error\":\"" + errorMessage + "\"}";
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        
        try (var os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
    
    /**
     * Lista todas as rotas registradas.
     * @return Lista de rotas
     */
    public List<Route> getRoutes() {
        return new ArrayList<>(routes);
    }
    
    /**
     * Classe interna para definir rotas de forma mais limpa.
     */
    public static class RouteDefinition {
        private final String method;
        private final String path;
        private final String handlerMethod;
        private final Object controller;
        
        public RouteDefinition(String method, String path, String handlerMethod, Object controller) {
            this.method = method;
            this.path = path;
            this.handlerMethod = handlerMethod;
            this.controller = controller;
        }
        
        public String getMethod() { return method; }
        public String getPath() { return path; }
        public String getHandlerMethod() { return handlerMethod; }
        public Object getController() { return controller; }
    }
} 