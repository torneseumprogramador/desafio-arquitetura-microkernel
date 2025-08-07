package core;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

/**
 * Interface para handlers HTTP.
 * Usada pelos plugins para expor seus endpoints.
 */
@FunctionalInterface
public interface HttpHandler {
    /**
     * Processa uma requisição HTTP.
     * @param exchange Troca HTTP contendo request e response
     * @throws IOException Se houver erro de I/O
     */
    void handle(HttpExchange exchange) throws IOException;
}
