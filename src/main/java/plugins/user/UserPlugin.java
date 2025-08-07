package plugins.user;

import core.Plugin;
import core.DatabaseManager;
import core.HttpHandler;
import plugins.user.services.UserService;
import plugins.user.repositories.UserRepository;
import plugins.user.routes.UserRoutes;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Plugin para gerenciamento de usuários.
 * Demonstra um plugin autocontido com sistema de rotas declarativo.
 */
public class UserPlugin implements Plugin {
    private final UserService userService;
    private final UserRoutes userRoutes;

    public UserPlugin() {
        // Inicializar o plugin com suas dependências
        DatabaseManager dbManager = DatabaseManager.getInstance();
        Connection connection = dbManager.getConnection();
        UserRepository userRepository = new UserRepository(connection);
        this.userService = new UserService(userRepository);
        
        // Configurar sistema de rotas
        this.userRoutes = new UserRoutes(userService);
    }

    @Override
    public String getName() {
        return "User API Plugin - Gerenciamento de Usuários via REST";
    }

    @Override
    public void execute() {
        System.out.println("=== Plugin de Usuários Ativado ===");
        System.out.println("📦 Inicializando dependências do plugin:");
        System.out.println("   ✅ UserRepository conectado ao banco");
        System.out.println("   ✅ UserService inicializado");
        System.out.println("   ✅ UserController configurado");
        System.out.println("   ✅ RouteRegistry configurado");
        System.out.println("   ✅ Rotas declarativas configuradas:");
        
        // Mostrar rotas configuradas
        userRoutes.getRouteDefinitions().forEach(route -> 
            System.out.println("      " + route.getMethod() + " " + route.getPath() + " → " + route.getHandlerMethod())
        );
        
        System.out.println("   ✅ Entidades carregadas");
        System.out.println("=== Plugin de Usuários pronto para uso ===\n");
    }

    @Override
    public List<String> getAvailableRoutes() {
        return List.of(
            "GET  /api/users     - Listar todos os usuários",
            "POST /api/users     - Criar novo usuário",
            "GET  /api/users/{id} - Buscar usuário por ID",
            "PUT  /api/users/{id} - Atualizar usuário",
            "DELETE /api/users/{id} - Deletar usuário"
        );
    }

    @Override
    public String getEmoji() {
        return "👥";
    }

    @Override
    public Map<String, Object> getSwaggerInfo() {
        return Map.of(
            "name", "Usuários",
            "description", "Gerenciamento de usuários do sistema",
            "basePath", "/api/users",
            "version", "1.0.0"
        );
    }

    @Override
    public Map<String, Object> getOpenApiSchemas() {
        return Map.of(
            "User", Map.of(
                "type", "object",
                "properties", Map.of(
                    "id", Map.of("type", "integer"),
                    "name", Map.of("type", "string"),
                    "email", Map.of("type", "string"),
                    "password", Map.of("type", "string"),
                    "created_at", Map.of("type", "string", "format", "date-time")
                )
            )
        );
    }

    @Override
    public Map<String, Object> getOpenApiPaths() {
        return Map.of(
            "/api/users", Map.of(
                "get", Map.of(
                    "tags", List.of("Usuários"),
                    "summary", "Listar todos os usuários",
                    "description", "Retorna lista de todos os usuários cadastrados",
                    "responses", createResponses("Lista de usuários")
                ),
                "post", Map.of(
                    "tags", List.of("Usuários"),
                    "summary", "Criar novo usuário",
                    "description", "Cria um novo usuário no sistema",
                    "requestBody", createRequestBody("User"),
                    "responses", createResponses("Usuário criado")
                )
            ),
            "/api/users/{id}", Map.of(
                "get", Map.of(
                    "tags", List.of("Usuários"),
                    "summary", "Buscar usuário por ID",
                    "description", "Retorna um usuário específico por ID",
                    "parameters", createIdParameter(),
                    "responses", createResponses("Usuário encontrado")
                ),
                "put", Map.of(
                    "tags", List.of("Usuários"),
                    "summary", "Atualizar usuário",
                    "description", "Atualiza um usuário existente",
                    "parameters", createIdParameter(),
                    "requestBody", createRequestBody("User"),
                    "responses", createResponses("Usuário atualizado")
                ),
                "delete", Map.of(
                    "tags", List.of("Usuários"),
                    "summary", "Deletar usuário",
                    "description", "Remove um usuário do sistema",
                    "parameters", createIdParameter(),
                    "responses", createResponses("Usuário deletado")
                )
            )
        );
    }

    @Override
    public List<String> getOpenApiTags() {
        return List.of("Usuários");
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
            "description", "ID do usuário",
            "schema", Map.of("type", "integer")
        ));
    }

    /**
     * Retorna o HttpHandler do plugin.
     * @return HttpHandler configurado com o sistema de rotas
     */
    public HttpHandler getHttpHandler() {
        return userRoutes.getRouteRegistry()::handleRequest;
    }

    /**
     * Retorna o UserService para uso externo.
     * @return UserService do plugin
     */
    public UserService getUserService() {
        return userService;
    }
} 