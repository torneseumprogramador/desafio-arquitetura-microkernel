package plugins.order;

import core.Plugin;
import core.DatabaseManager;
import core.HttpHandler;
import plugins.order.services.OrderService;
import plugins.order.repositories.OrderRepository;
import plugins.order.routes.OrderRoutes;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Plugin para gerenciamento de pedidos.
 * Demonstra um plugin autocontido com sistema de rotas declarativo.
 */
public class OrderPlugin implements Plugin {
    private final OrderService orderService;
    private final OrderRoutes orderRoutes;

    public OrderPlugin() {
        // Inicializar o plugin com suas dependências
        DatabaseManager dbManager = DatabaseManager.getInstance();
        Connection connection = dbManager.getConnection();
        OrderRepository orderRepository = new OrderRepository(connection);
        this.orderService = new OrderService(orderRepository);
        
        // Configurar sistema de rotas
        this.orderRoutes = new OrderRoutes(orderService);
    }

    @Override
    public String getName() {
        return "Order API Plugin - Gerenciamento de Pedidos via REST";
    }

    @Override
    public void execute() {
        System.out.println("=== Plugin de Pedidos Ativado ===");
        System.out.println("📦 Inicializando dependências do plugin:");
        System.out.println("   ✅ OrderRepository conectado ao banco");
        System.out.println("   ✅ OrderService inicializado");
        System.out.println("   ✅ OrderController configurado");
        System.out.println("   ✅ RouteRegistry configurado");
        System.out.println("   ✅ Rotas declarativas configuradas:");
        
        // Mostrar rotas configuradas
        orderRoutes.getRouteDefinitions().forEach(route -> 
            System.out.println("      " + route.getMethod() + " " + route.getPath() + " → " + route.getHandlerMethod())
        );
        
        System.out.println("   ✅ Entidades carregadas");
        System.out.println("=== Plugin de Pedidos pronto para uso ===\n");
    }

    @Override
    public List<String> getAvailableRoutes() {
        return List.of(
            "GET  /api/orders     - Listar todos os pedidos",
            "POST /api/orders     - Criar novo pedido",
            "GET  /api/orders/{id} - Buscar pedido por ID",
            "PUT  /api/orders/{id} - Atualizar pedido",
            "DELETE /api/orders/{id} - Deletar pedido",
            "GET  /api/orders/user/{userId} - Buscar pedidos por usuário",
            "POST /api/orders/{id}/products - Adicionar produto ao pedido",
            "PUT  /api/orders/{id}/finalize - Finalizar pedido"
        );
    }

    @Override
    public String getEmoji() {
        return "📋";
    }

    @Override
    public Map<String, Object> getSwaggerInfo() {
        return Map.of(
            "name", "Pedidos",
            "description", "Gerenciamento de pedidos e produtos do pedido",
            "basePath", "/api/orders",
            "version", "1.0.0"
        );
    }

    @Override
    public Map<String, Object> getOpenApiSchemas() {
        return Map.of(
            "Order", Map.of(
                "type", "object",
                "properties", Map.of(
                    "id", Map.of("type", "integer"),
                    "user_id", Map.of("type", "integer"),
                    "total_amount", Map.of("type", "number", "format", "decimal"),
                    "status", Map.of("type", "string"),
                    "created_at", Map.of("type", "string", "format", "date-time")
                )
            ),
            "OrderProduct", Map.of(
                "type", "object",
                "properties", Map.of(
                    "product_id", Map.of("type", "integer"),
                    "quantity", Map.of("type", "integer")
                )
            )
        );
    }

    @Override
    public Map<String, Object> getOpenApiPaths() {
        return Map.of(
            "/api/orders", Map.of(
                "get", Map.of(
                    "tags", List.of("Pedidos"),
                    "summary", "Listar todos os pedidos",
                    "description", "Retorna lista de todos os pedidos",
                    "responses", createResponses("Lista de pedidos")
                ),
                "post", Map.of(
                    "tags", List.of("Pedidos"),
                    "summary", "Criar novo pedido",
                    "description", "Cria um novo pedido no sistema",
                    "requestBody", createRequestBody("Order"),
                    "responses", createResponses("Pedido criado")
                )
            ),
            "/api/orders/{id}", Map.of(
                "get", Map.of(
                    "tags", List.of("Pedidos"),
                    "summary", "Buscar pedido por ID",
                    "description", "Retorna um pedido específico por ID",
                    "parameters", createIdParameter(),
                    "responses", createResponses("Pedido encontrado")
                ),
                "put", Map.of(
                    "tags", List.of("Pedidos"),
                    "summary", "Atualizar pedido",
                    "description", "Atualiza um pedido existente",
                    "parameters", createIdParameter(),
                    "requestBody", createRequestBody("Order"),
                    "responses", createResponses("Pedido atualizado")
                ),
                "delete", Map.of(
                    "tags", List.of("Pedidos"),
                    "summary", "Deletar pedido",
                    "description", "Remove um pedido do sistema",
                    "parameters", createIdParameter(),
                    "responses", createResponses("Pedido deletado")
                )
            ),
            "/api/orders/user/{userId}", Map.of(
                "get", Map.of(
                    "tags", List.of("Pedidos"),
                    "summary", "Buscar pedidos por usuário",
                    "description", "Retorna todos os pedidos de um usuário específico",
                    "parameters", createUserIdParameter(),
                    "responses", createResponses("Pedidos do usuário")
                )
            ),
            "/api/orders/{id}/products", Map.of(
                "post", Map.of(
                    "tags", List.of("Pedidos"),
                    "summary", "Adicionar produto ao pedido",
                    "description", "Adiciona um produto a um pedido existente",
                    "parameters", createIdParameter(),
                    "requestBody", createRequestBody("OrderProduct"),
                    "responses", createResponses("Produto adicionado ao pedido")
                )
            ),
            "/api/orders/{id}/finalize", Map.of(
                "put", Map.of(
                    "tags", List.of("Pedidos"),
                    "summary", "Finalizar pedido",
                    "description", "Finaliza um pedido mudando seu status para PAID",
                    "parameters", createIdParameter(),
                    "responses", createResponses("Pedido finalizado")
                )
            )
        );
    }

    @Override
    public List<String> getOpenApiTags() {
        return List.of("Pedidos");
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
            "description", "ID do pedido",
            "schema", Map.of("type", "integer")
        ));
    }

    private List<Map<String, Object>> createUserIdParameter() {
        return List.of(Map.of(
            "name", "userId",
            "in", "path",
            "required", true,
            "description", "ID do usuário",
            "schema", Map.of("type", "integer")
        ));
    }

    /**
     * Retorna o HttpHandler do plugin.
     * @return HttpHandler configurado com o sistema de rotas
     */
    public HttpHandler getHttpHandler() {
        return orderRoutes.getRouteRegistry()::handleRequest;
    }

    /**
     * Retorna o OrderService para uso externo.
     * @return OrderService do plugin
     */
    public OrderService getOrderService() {
        return orderService;
    }
} 