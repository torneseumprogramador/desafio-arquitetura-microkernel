package core;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

/**
 * Interface base para controllers HTTP.
 * Define métodos padrão para gerenciar requisições HTTP.
 */
public interface HttpController {
    
    /**
     * Processa uma requisição HTTP.
     * @param exchange HttpExchange contendo os dados da requisição
     * @throws IOException em caso de erro de I/O
     */
    void handle(HttpExchange exchange) throws IOException;
    
    /**
     * Verifica se o controller pode processar a requisição.
     * @param method Método HTTP (GET, POST, PUT, DELETE, etc.)
     * @param path Caminho da requisição
     * @return true se o controller pode processar, false caso contrário
     */
    boolean canHandle(String method, String path);
} 