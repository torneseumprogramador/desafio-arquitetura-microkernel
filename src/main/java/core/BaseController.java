package core;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Pattern;

/**
 * Classe base para controllers HTTP com funcionalidades comuns.
 */
public abstract class BaseController implements HttpController {
    
    protected static final String CONTENT_TYPE_JSON = "application/json";
    protected static final String CONTENT_TYPE_TEXT = "text/plain";
    
    /**
     * Envia uma resposta HTTP.
     * @param exchange HttpExchange
     * @param statusCode Código de status HTTP
     * @param response Corpo da resposta
     * @param contentType Tipo de conteúdo
     * @throws IOException em caso de erro
     */
    protected void sendResponse(HttpExchange exchange, int statusCode, String response, String contentType) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", contentType);
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
    
    /**
     * Envia resposta JSON.
     * @param exchange HttpExchange
     * @param statusCode Código de status HTTP
     * @param jsonResponse Resposta JSON
     * @throws IOException em caso de erro
     */
    protected void sendJsonResponse(HttpExchange exchange, int statusCode, String jsonResponse) throws IOException {
        sendResponse(exchange, statusCode, jsonResponse, CONTENT_TYPE_JSON);
    }
    
    /**
     * Envia resposta de texto.
     * @param exchange HttpExchange
     * @param statusCode Código de status HTTP
     * @param textResponse Resposta de texto
     * @throws IOException em caso de erro
     */
    protected void sendTextResponse(HttpExchange exchange, int statusCode, String textResponse) throws IOException {
        sendResponse(exchange, statusCode, textResponse, CONTENT_TYPE_TEXT);
    }
    
    /**
     * Envia resposta de erro.
     * @param exchange HttpExchange
     * @param statusCode Código de status HTTP
     * @param errorMessage Mensagem de erro
     * @throws IOException em caso de erro
     */
    protected void sendError(HttpExchange exchange, int statusCode, String errorMessage) throws IOException {
        String errorJson = "{\"error\":\"" + errorMessage + "\"}";
        sendJsonResponse(exchange, statusCode, errorJson);
    }
    
    /**
     * Extrai ID de um path.
     * @param path Caminho da requisição
     * @return ID extraído ou -1 se não encontrado
     */
    protected int extractId(String path) {
        try {
            String[] parts = path.split("/");
            return Integer.parseInt(parts[parts.length - 1]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return -1;
        }
    }
    
    /**
     * Verifica se o path corresponde ao padrão.
     * @param path Caminho da requisição
     * @param pattern Padrão regex
     * @return true se corresponde, false caso contrário
     */
    protected boolean matchesPattern(String path, String pattern) {
        return Pattern.matches(pattern, path);
    }
    
    /**
     * Obtém o método HTTP da requisição.
     * @param exchange HttpExchange
     * @return Método HTTP
     */
    protected String getMethod(HttpExchange exchange) {
        return exchange.getRequestMethod();
    }
    
    /**
     * Obtém o path da requisição.
     * @param exchange HttpExchange
     * @return Path da requisição
     */
    protected String getPath(HttpExchange exchange) {
        return exchange.getRequestURI().getPath();
    }
} 