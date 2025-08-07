package plugins.product;

import core.Plugin;
import core.DatabaseManager;
import core.HttpHandler;
import plugins.product.services.ProductService;
import plugins.product.repositories.ProductRepository;
import plugins.product.routes.ProductRoutes;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Plugin para gerenciamento de produtos.
 * Demonstra um plugin autocontido com sistema de rotas declarativo.
 */
public class ProductPlugin implements Plugin {
    private final ProductService productService;
    private final ProductRoutes productRoutes;

    public ProductPlugin() {
        // Inicializar o plugin com suas dependências
        DatabaseManager dbManager = DatabaseManager.getInstance();
        Connection connection = dbManager.getConnection();
        ProductRepository productRepository = new ProductRepository(connection);
        this.productService = new ProductService(productRepository);
        
        // Configurar sistema de rotas
        this.productRoutes = new ProductRoutes(productService);
    }

    @Override
    public String getName() {
        return "Product API Plugin - Gerenciamento de Produtos via REST";
    }

    @Override
    public void execute() {
        System.out.println("=== Plugin de Produtos Ativado ===");
        System.out.println("📦 Inicializando dependências do plugin:");
        System.out.println("   ✅ ProductRepository conectado ao banco");
        System.out.println("   ✅ ProductService inicializado");
        System.out.println("   ✅ ProductController configurado");
        System.out.println("   ✅ RouteRegistry configurado");
        System.out.println("   ✅ Rotas declarativas configuradas:");
        
        // Mostrar rotas configuradas
        productRoutes.getRouteDefinitions().forEach(route -> 
            System.out.println("      " + route.getMethod() + " " + route.getPath() + " → " + route.getHandlerMethod())
        );
        
        System.out.println("   ✅ Entidades carregadas");
        System.out.println("=== Plugin de Produtos pronto para uso ===\n");
    }

    @Override
    public List<String> getAvailableRoutes() {
        return List.of(
            "GET  /api/products     - Listar todos os produtos",
            "POST /api/products     - Criar novo produto",
            "GET  /api/products/{id} - Buscar produto por ID",
            "PUT  /api/products/{id} - Atualizar produto",
            "DELETE /api/products/{id} - Deletar produto",
            "GET  /api/products/available - Listar produtos disponíveis",
            "PUT  /api/products/{id}/stock - Atualizar estoque"
        );
    }

    @Override
    public String getEmoji() {
        return "📦";
    }

    @Override
    public Map<String, Object> getSwaggerInfo() {
        return Map.of(
            "name", "Produtos",
            "description", "Gerenciamento de produtos do catálogo",
            "basePath", "/api/products",
            "version", "1.0.0"
        );
    }

    @Override
    public Map<String, Object> getOpenApiSchemas() {
        return Map.of(
            "Product", Map.of(
                "type", "object",
                "properties", Map.of(
                    "id", Map.of("type", "integer"),
                    "name", Map.of("type", "string"),
                    "description", Map.of("type", "string"),
                    "price", Map.of("type", "number", "format", "decimal"),
                    "stock", Map.of("type", "integer"),
                    "created_at", Map.of("type", "string", "format", "date-time")
                )
            ),
            "StockUpdate", Map.of(
                "type", "object",
                "properties", Map.of(
                    "stock", Map.of("type", "integer")
                )
            )
        );
    }

    @Override
    public Map<String, Object> getOpenApiPaths() {
        return Map.of(
            "/api/products", Map.of(
                "get", Map.of(
                    "tags", List.of("Produtos"),
                    "summary", "Listar todos os produtos",
                    "description", "Retorna lista de todos os produtos do catálogo",
                    "responses", createResponses("Lista de produtos")
                ),
                "post", Map.of(
                    "tags", List.of("Produtos"),
                    "summary", "Criar novo produto",
                    "description", "Cria um novo produto no catálogo",
                    "requestBody", createRequestBody("Product"),
                    "responses", createResponses("Produto criado")
                )
            ),
            "/api/products/{id}", Map.of(
                "get", Map.of(
                    "tags", List.of("Produtos"),
                    "summary", "Buscar produto por ID",
                    "description", "Retorna um produto específico por ID",
                    "parameters", createIdParameter(),
                    "responses", createResponses("Produto encontrado")
                ),
                "put", Map.of(
                    "tags", List.of("Produtos"),
                    "summary", "Atualizar produto",
                    "description", "Atualiza um produto existente",
                    "parameters", createIdParameter(),
                    "requestBody", createRequestBody("Product"),
                    "responses", createResponses("Produto atualizado")
                ),
                "delete", Map.of(
                    "tags", List.of("Produtos"),
                    "summary", "Deletar produto",
                    "description", "Remove um produto do catálogo",
                    "parameters", createIdParameter(),
                    "responses", createResponses("Produto deletado")
                )
            ),
            "/api/products/available", Map.of(
                "get", Map.of(
                    "tags", List.of("Produtos"),
                    "summary", "Listar produtos disponíveis",
                    "description", "Retorna apenas produtos com estoque disponível",
                    "responses", createResponses("Produtos disponíveis")
                )
            ),
            "/api/products/{id}/stock", Map.of(
                "put", Map.of(
                    "tags", List.of("Produtos"),
                    "summary", "Atualizar estoque",
                    "description", "Atualiza o estoque de um produto",
                    "parameters", createIdParameter(),
                    "requestBody", createRequestBody("StockUpdate"),
                    "responses", createResponses("Estoque atualizado")
                )
            )
        );
    }

    @Override
    public List<String> getOpenApiTags() {
        return List.of("Produtos");
    }

    private Map<String, Object> createResponses(String description) {
        return Map.of(
            "200", Map.of(
                "description", description,
                "content", Map.of(
                    "application/json", Map.of(
                        "schema", Map.of("type", "object")
                    )
                )
            ),
            "404", Map.of("description", "Recurso não encontrado"),
            "400", Map.of("description", "Dados inválidos")
        );
    }

    private Map<String, Object> createRequestBody(String schemaName) {
        return Map.of(
            "required", true,
            "content", Map.of(
                "application/json", Map.of(
                    "schema", Map.of("$ref", "#/components/schemas/" + schemaName)
                )
            )
        );
    }

    private List<Map<String, Object>> createIdParameter() {
        return List.of(Map.of(
            "name", "id",
            "in", "path",
            "required", true,
            "description", "ID do produto",
            "schema", Map.of("type", "integer")
        ));
    }

    /**
     * Retorna o HttpHandler do plugin.
     * @return HttpHandler configurado com o sistema de rotas
     */
    public HttpHandler getHttpHandler() {
        return productRoutes.getRouteRegistry()::handleRequest;
    }

    /**
     * Retorna o ProductService para uso externo.
     * @return ProductService do plugin
     */
    public ProductService getProductService() {
        return productService;
    }
} 