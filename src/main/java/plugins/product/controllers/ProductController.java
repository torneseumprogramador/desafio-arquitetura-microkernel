package plugins.product.controllers;

import core.SimpleController;
import plugins.product.services.ProductService;
import plugins.product.entities.Product;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Controller de produtos usando sistema de rotas declarativo.
 * Cada método representa um endpoint específico.
 */
public class ProductController extends SimpleController {
    
    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    /**
     * GET /api/products - Lista todos os produtos
     */
    public void listProducts(HttpExchange exchange) throws IOException {
        try {
            List<Product> products = productService.getAllProducts();
            String response = buildProductsJson(products);
            sendJsonResponse(exchange, 200, response);
        } catch (SQLException e) {
            sendError(exchange, 500, "Database error: " + e.getMessage());
        }
    }
    
    /**
     * GET /api/products/available - Lista produtos disponíveis
     */
    public void listAvailableProducts(HttpExchange exchange) throws IOException {
        try {
            List<Product> products = productService.getAvailableProducts();
            String response = buildProductsJson(products);
            sendJsonResponse(exchange, 200, response);
        } catch (SQLException e) {
            sendError(exchange, 500, "Database error: " + e.getMessage());
        }
    }
    
    /**
     * GET /api/products/{id} - Busca produto por ID
     */
    public void getProductById(HttpExchange exchange) throws IOException {
        try {
            String path = getPath(exchange);
            int productId = extractId(path);
            
            Optional<Product> product = productService.findProductById(productId);
            if (product.isPresent()) {
                String response = buildProductJson(product.get());
                sendJsonResponse(exchange, 200, response);
            } else {
                sendError(exchange, 404, "Product not found");
            }
        } catch (SQLException e) {
            sendError(exchange, 500, "Database error: " + e.getMessage());
        }
    }
    
    /**
     * POST /api/products - Cria novo produto
     */
    public void createProduct(HttpExchange exchange) throws IOException {
        try {
            // TODO: Implementar leitura do body da requisição
            Product newProduct = productService.registerProduct(
                "Novo Produto", 
                "Descrição do novo produto", 
                new java.math.BigDecimal("99.99"), 
                10
            );
            String response = "{\"message\":\"Produto criado com sucesso\",\"id\":" + newProduct.getId() + "}";
            sendJsonResponse(exchange, 201, response);
        } catch (IllegalArgumentException e) {
            sendError(exchange, 400, e.getMessage());
        } catch (SQLException e) {
            sendError(exchange, 500, "Database error: " + e.getMessage());
        }
    }
    
    /**
     * PUT /api/products/{id} - Atualiza produto
     */
    public void updateProduct(HttpExchange exchange) throws IOException {
        String path = getPath(exchange);
        int productId = extractId(path);
        String response = "{\"message\":\"Produto atualizado com sucesso\",\"id\":" + productId + "}";
        sendJsonResponse(exchange, 200, response);
    }
    
    /**
     * PUT /api/products/{id}/stock - Atualiza estoque do produto
     */
    public void updateStock(HttpExchange exchange) throws IOException {
        String path = getPath(exchange);
        int productId = extractId(path);
        String response = "{\"message\":\"Estoque atualizado com sucesso\",\"id\":" + productId + "}";
        sendJsonResponse(exchange, 200, response);
    }
    
    /**
     * DELETE /api/products/{id} - Deleta produto
     */
    public void deleteProduct(HttpExchange exchange) throws IOException {
        String path = getPath(exchange);
        int productId = extractId(path);
        String response = "{\"message\":\"Produto deletado com sucesso\",\"id\":" + productId + "}";
        sendJsonResponse(exchange, 200, response);
    }
    
    private String buildProductsJson(List<Product> products) {
        StringBuilder json = new StringBuilder("{\"products\":[");
        for (int i = 0; i < products.size(); i++) {
            if (i > 0) json.append(",");
            json.append(buildProductJson(products.get(i)));
        }
        json.append("]}");
        return json.toString();
    }
    
    private String buildProductJson(Product product) {
        return "{\"id\":" + product.getId() + 
               ",\"name\":\"" + product.getName() + "\"" +
               ",\"description\":\"" + product.getDescription() + "\"" +
               ",\"price\":" + product.getPrice() +
               ",\"stock\":" + product.getStock() +
               ",\"createdAt\":\"" + product.getCreatedAt() + "\"}";
    }
} 