package plugins.user;

import core.Plugin;
import core.DatabaseManager;
import core.HttpHandler;
import plugins.user.services.UserService;
import plugins.user.repositories.UserRepository;
import plugins.user.routes.UserRoutes;
import java.sql.Connection;

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