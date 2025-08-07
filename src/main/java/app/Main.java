package app;

import core.Kernel;
import core.CoreRoutes;
import core.HttpHandler;
import core.controllers.HomeController;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.ArrayList;
import java.util.List;

/**
 * Servidor HTTP simples para API REST do Sistema Microkernel Ecommerce.
 */
public class Main {
    private static final int PORT = 8080;
    private static HttpServer server;
    private static List<HomeController.PluginInfo> loadedPlugins = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("🛒 Sistema Microkernel Ecommerce API");
        System.out.println("=====================================\n");

        try {
            // Inicializar Kernel
            Kernel kernel = new Kernel();
            kernel.initialize();

            // Criar servidor HTTP
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            
            // Configurar endpoints
            setupEndpoints();
            
            // Configurar thread pool
            server.setExecutor(Executors.newFixedThreadPool(10));
            
            // Iniciar servidor
            server.start();
            
            // Exibir informações dinâmicas
            displayServerInfo();
            displayEndpoints();

        } catch (Exception e) {
            System.err.println("❌ Erro ao inicializar o servidor: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void setupEndpoints() {
        // Configurar rotas do core
        CoreRoutes coreRoutes = new CoreRoutes();
        server.createContext("/", coreRoutes.getRouteRegistry()::handleRequest);
        
        // Carregar plugins como APIs
        loadPluginApis();
        
        // Injetar informações dos plugins no HomeController
        HomeController.setLoadedPlugins(loadedPlugins);
    }

    private static void loadPluginApis() {
        try {
            // Carregar plugin de usuários
            plugins.user.UserPlugin userPlugin = new plugins.user.UserPlugin();
            server.createContext("/api/users", (com.sun.net.httpserver.HttpHandler) userPlugin.getHttpHandler()::handle);
            loadedPlugins.add(new HomeController.PluginInfo(userPlugin.getName(), "/api/users", userPlugin));
            System.out.println("✅ Plugin de Usuários carregado como API");
            
            // Carregar plugin de produtos
            plugins.product.ProductPlugin productPlugin = new plugins.product.ProductPlugin();
            server.createContext("/api/products", (com.sun.net.httpserver.HttpHandler) productPlugin.getHttpHandler()::handle);
            loadedPlugins.add(new HomeController.PluginInfo(productPlugin.getName(), "/api/products", productPlugin));
            System.out.println("✅ Plugin de Produtos carregado como API");
            
            // // Carregar plugin de pedidos (desabilitado para teste)
            // plugins.order.OrderPlugin orderPlugin = new plugins.order.OrderPlugin();
            // server.createContext("/api/orders", (com.sun.net.httpserver.HttpHandler) orderPlugin.getHttpHandler()::handle);
            // loadedPlugins.add(new HomeController.PluginInfo(orderPlugin.getName(), "/api/orders", orderPlugin));
            // System.out.println("✅ Plugin de Pedidos carregado como API");
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar plugins: " + e.getMessage());
        }
    }

    private static void displayServerInfo() {
        System.out.println("🚀 Servidor iniciado na porta " + PORT);
        System.out.println("📡 API disponível em: http://localhost:" + PORT);
        System.out.println("📦 Plugins carregados: " + loadedPlugins.size());
        System.out.println();
    }

    private static void displayEndpoints() {
        System.out.println("📋 Endpoints disponíveis:");
        
        // Endpoints do sistema (sempre disponíveis)
        System.out.println("   🏠 Sistema:");
        System.out.println("     GET  /              - Página inicial");
        System.out.println("     GET  /api           - Informações da API");
        System.out.println("     GET  /api/docs      - Documentação da API");
        System.out.println("     GET  /api/health    - Status da aplicação");
        System.out.println("     GET  /api/health/detailed - Status detalhado");
        System.out.println("     GET  /api/health/database - Status do banco");
        System.out.println("   📚 Swagger:");
        System.out.println("     GET  /api/swagger   - Documentação OpenAPI (JSON)");
        System.out.println("     GET  /api/swagger-ui - Interface Swagger UI");
        
        // Endpoints dos plugins carregados dinamicamente
        for (HomeController.PluginInfo plugin : loadedPlugins) {
            System.out.println("   " + plugin.getPlugin().getEmoji() + " " + plugin.getName() + ":");
            displayPluginEndpoints(plugin);
        }
        
        System.out.println("\n🌐 Acesse a documentação interativa:");
        System.out.println("   📖 Swagger UI: http://localhost:" + PORT + "/api/swagger-ui");
        System.out.println("   📄 OpenAPI JSON: http://localhost:" + PORT + "/api/swagger");
        System.out.println("\n⏹️  Pressione Ctrl+C para parar o servidor");
        
        // Manter o servidor rodando
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            System.out.println("\n🛑 Servidor interrompido");
        } finally {
            if (server != null) {
                server.stop(0);
            }
        }
    }

    private static void displayPluginEndpoints(HomeController.PluginInfo plugin) {
        System.out.println("   " + plugin.getPlugin().getEmoji() + " " + plugin.getName() + ":");
        
        // Listar endpoints do plugin
        List<String> routes = plugin.getPlugin().getAvailableRoutes();
        for (String route : routes) {
            String[] parts = route.split(" - ", 2);
            String methodPath = parts[0].trim();
            String description = parts.length > 1 ? parts[1].trim() : "";
            
            String[] methodPathParts = methodPath.split("\\s+", 2);
            String method = methodPathParts[0];
            String path = methodPathParts[1];
            
            System.out.println("     " + method + " " + path + " - " + description);
        }
        System.out.println();
    }
} 