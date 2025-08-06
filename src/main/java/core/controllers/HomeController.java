package core.controllers;

import core.SimpleController;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

/**
 * Controller para a página inicial da aplicação.
 * Mostra informações sobre o sistema Microkernel.
 */
public class HomeController extends SimpleController {
    
    /**
     * GET / - Página inicial da aplicação
     */
    public void getHome(HttpExchange exchange) throws IOException {
        String response = buildHomeJson();
        sendJsonResponse(exchange, 200, response);
    }
    
    /**
     * GET /api - Informações da API
     */
    public void getApiInfo(HttpExchange exchange) throws IOException {
        String response = buildApiInfoJson();
        sendJsonResponse(exchange, 200, response);
    }
    
    /**
     * GET /api/docs - Documentação da API
     */
    public void getApiDocs(HttpExchange exchange) throws IOException {
        String response = buildApiDocsJson();
        sendJsonResponse(exchange, 200, response);
    }
    
    private String buildHomeJson() {
        return "{" +
               "\"application\": \"Sistema Microkernel Ecommerce\"," +
               "\"version\": \"1.0.0\"," +
               "\"description\": \"Sistema de e-commerce baseado em arquitetura Microkernel\"," +
               "\"architecture\": \"Plugin-based Microkernel\"," +
               "\"status\": \"running\"," +
               "\"endpoints\": {" +
               "  \"home\": \"/\"," +
               "  \"health\": \"/api/health\"," +
               "  \"api_info\": \"/api\"," +
               "  \"docs\": \"/api/docs\"," +
               "  \"users\": \"/api/users\"," +
               "  \"products\": \"/api/products\"," +
               "  \"orders\": \"/api/orders\"" +
               "}," +
               "\"plugins\": [" +
               "  \"UserPlugin - Gerenciamento de usuários\"," +
               "  \"ProductPlugin - Gerenciamento de produtos\"," +
               "  \"OrderPlugin - Gerenciamento de pedidos\"" +
               "]," +
               "\"features\": [" +
               "  \"Arquitetura Microkernel\"," +
               "  \"Plugins autocontidos\"," +
               "  \"Sistema de rotas declarativo\"," +
               "  \"Banco de dados SQLite\"," +
               "  \"API REST\"," +
               "  \"Seed automático\"" +
               "]" +
               "}";
    }
    
    private String buildApiInfoJson() {
        return "{" +
               "\"api\": \"Microkernel Ecommerce API\"," +
               "\"version\": \"1.0.0\"," +
               "\"base_url\": \"/api\"," +
               "\"endpoints\": {" +
               "  \"health\": {" +
               "    \"method\": \"GET\"," +
               "    \"path\": \"/api/health\"," +
               "    \"description\": \"Verificar status da aplicação\"" +
               "  }," +
               "  \"users\": {" +
               "    \"method\": \"GET\"," +
               "    \"path\": \"/api/users\"," +
               "    \"description\": \"Listar usuários\"" +
               "  }," +
               "  \"products\": {" +
               "    \"method\": \"GET\"," +
               "    \"path\": \"/api/products\"," +
               "    \"description\": \"Listar produtos\"" +
               "  }," +
               "  \"orders\": {" +
               "    \"method\": \"GET\"," +
               "    \"path\": \"/api/orders\"," +
               "    \"description\": \"Listar pedidos\"" +
               "  }" +
               "}," +
               "\"documentation\": \"/api/docs\"" +
               "}";
    }
    
    private String buildApiDocsJson() {
        return "{" +
               "\"documentation\": \"Microkernel Ecommerce API Documentation\"," +
               "\"version\": \"1.0.0\"," +
               "\"endpoints\": {" +
               "  \"users\": {" +
               "    \"base_path\": \"/api/users\"," +
               "    \"operations\": [" +
               "      {\"method\": \"GET\", \"path\": \"/api/users\", \"description\": \"Listar todos os usuários\"}," +
               "      {\"method\": \"GET\", \"path\": \"/api/users/{id}\", \"description\": \"Buscar usuário por ID\"}," +
               "      {\"method\": \"POST\", \"path\": \"/api/users\", \"description\": \"Criar novo usuário\"}," +
               "      {\"method\": \"PUT\", \"path\": \"/api/users/{id}\", \"description\": \"Atualizar usuário\"}," +
               "      {\"method\": \"DELETE\", \"path\": \"/api/users/{id}\", \"description\": \"Deletar usuário\"}" +
               "    ]" +
               "  }," +
               "  \"products\": {" +
               "    \"base_path\": \"/api/products\"," +
               "    \"operations\": [" +
               "      {\"method\": \"GET\", \"path\": \"/api/products\", \"description\": \"Listar todos os produtos\"}," +
               "      {\"method\": \"GET\", \"path\": \"/api/products/available\", \"description\": \"Listar produtos disponíveis\"}," +
               "      {\"method\": \"GET\", \"path\": \"/api/products/{id}\", \"description\": \"Buscar produto por ID\"}," +
               "      {\"method\": \"POST\", \"path\": \"/api/products\", \"description\": \"Criar novo produto\"}," +
               "      {\"method\": \"PUT\", \"path\": \"/api/products/{id}\", \"description\": \"Atualizar produto\"}," +
               "      {\"method\": \"PUT\", \"path\": \"/api/products/{id}/stock\", \"description\": \"Atualizar estoque\"}," +
               "      {\"method\": \"DELETE\", \"path\": \"/api/products/{id}\", \"description\": \"Deletar produto\"}" +
               "    ]" +
               "  }," +
               "  \"orders\": {" +
               "    \"base_path\": \"/api/orders\"," +
               "    \"operations\": [" +
               "      {\"method\": \"GET\", \"path\": \"/api/orders\", \"description\": \"Listar todos os pedidos\"}," +
               "      {\"method\": \"GET\", \"path\": \"/api/orders/{id}\", \"description\": \"Buscar pedido por ID\"}," +
               "      {\"method\": \"GET\", \"path\": \"/api/orders/user/{userId}\", \"description\": \"Buscar pedidos por usuário\"}," +
               "      {\"method\": \"POST\", \"path\": \"/api/orders\", \"description\": \"Criar novo pedido\"}," +
               "      {\"method\": \"POST\", \"path\": \"/api/orders/{id}/products\", \"description\": \"Adicionar produto ao pedido\"}," +
               "      {\"method\": \"PUT\", \"path\": \"/api/orders/{id}\", \"description\": \"Atualizar pedido\"}," +
               "      {\"method\": \"PUT\", \"path\": \"/api/orders/{id}/finalize\", \"description\": \"Finalizar pedido\"}," +
               "      {\"method\": \"DELETE\", \"path\": \"/api/orders/{id}\", \"description\": \"Deletar pedido\"}" +
               "    ]" +
               "  }" +
               "}" +
               "}";
    }
} 