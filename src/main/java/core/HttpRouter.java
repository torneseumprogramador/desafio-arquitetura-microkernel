package core;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Router HTTP para gerenciar múltiplos controllers.
 * Encaminha requisições para o controller apropriado.
 */
public class HttpRouter implements HttpHandler {
    
    private final List<HttpController> controllers;
    
    public HttpRouter() {
        this.controllers = new ArrayList<>();
    }
    
    /**
     * Adiciona um controller ao router.
     * @param controller Controller a ser adicionado
     */
    public void addController(HttpController controller) {
        controllers.add(controller);
    }
    
    /**
     * Adiciona múltiplos controllers ao router.
     * @param controllers Lista de controllers a serem adicionados
     */
    public void addControllers(List<HttpController> controllers) {
        this.controllers.addAll(controllers);
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        
        // Procurar controller que pode processar a requisição
        for (HttpController controller : controllers) {
            if (controller.canHandle(method, path)) {
                controller.handle(exchange);
                return;
            }
        }
        
        // Nenhum controller encontrado
        sendNotFound(exchange);
    }
    
    /**
     * Envia resposta 404 quando nenhum controller é encontrado.
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
} 