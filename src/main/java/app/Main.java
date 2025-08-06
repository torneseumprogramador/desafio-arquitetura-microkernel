package app;

import core.Kernel;
import core.CoreRoutes;
import core.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Servidor HTTP simples para API REST do Sistema Microkernel Ecommerce.
 */
public class Main {
    private static final int PORT = 8080;
    private static HttpServer server;

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
            
            System.out.println("🚀 Servidor iniciado na porta " + PORT);
            System.out.println("📡 API disponível em: http://localhost:" + PORT);
            System.out.println("📋 Endpoints disponíveis:");
            System.out.println("   🏠 Sistema:");
            System.out.println("     GET  /              - Página inicial");
            System.out.println("     GET  /api           - Informações da API");
            System.out.println("     GET  /api/docs      - Documentação da API");
            System.out.println("     GET  /api/health    - Status da aplicação");
            System.out.println("     GET  /api/health/detailed - Status detalhado");
            System.out.println("     GET  /api/health/database - Status do banco");
            System.out.println("   👥 Usuários:");
            System.out.println("     GET  /api/users     - Listar todos os usuários");
            System.out.println("     POST /api/users     - Criar novo usuário");
            System.out.println("     GET  /api/users/{id} - Buscar usuário por ID");
            System.out.println("     PUT  /api/users/{id} - Atualizar usuário");
            System.out.println("     DELETE /api/users/{id} - Deletar usuário");
            System.out.println("   📦 Produtos:");
            System.out.println("     GET  /api/products     - Listar todos os produtos");
            System.out.println("     POST /api/products     - Criar novo produto");
            System.out.println("     GET  /api/products/{id} - Buscar produto por ID");
            System.out.println("     PUT  /api/products/{id} - Atualizar produto");
            System.out.println("     DELETE /api/products/{id} - Deletar produto");
            System.out.println("     GET  /api/products/available - Listar produtos disponíveis");
            System.out.println("     PUT  /api/products/{id}/stock - Atualizar estoque");
            System.out.println("   📋 Pedidos:");
            System.out.println("     GET  /api/orders     - Listar todos os pedidos");
            System.out.println("     POST /api/orders     - Criar novo pedido");
            System.out.println("     GET  /api/orders/{id} - Buscar pedido por ID");
            System.out.println("     PUT  /api/orders/{id} - Atualizar pedido");
            System.out.println("     DELETE /api/orders/{id} - Deletar pedido");
            System.out.println("     GET  /api/orders/user/{userId} - Buscar pedidos por usuário");
            System.out.println("     POST /api/orders/{id}/products - Adicionar produto ao pedido");
            System.out.println("     PUT  /api/orders/{id}/finalize - Finalizar pedido");
            System.out.println("\n⏹️  Pressione Ctrl+C para parar o servidor\n");

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
    }

    private static void loadPluginApis() {
        try {
            // Carregar plugin de usuários
            plugins.user.UserPlugin userPlugin = new plugins.user.UserPlugin();
            server.createContext("/api/users", userPlugin.getHttpHandler());
            System.out.println("✅ Plugin de Usuários carregado como API");
            
            // Carregar plugin de produtos
            plugins.product.ProductPlugin productPlugin = new plugins.product.ProductPlugin();
            server.createContext("/api/products", productPlugin.getHttpHandler());
            System.out.println("✅ Plugin de Produtos carregado como API");
            
            // Carregar plugin de pedidos
            plugins.order.OrderPlugin orderPlugin = new plugins.order.OrderPlugin();
            server.createContext("/api/orders", orderPlugin.getHttpHandler());
            System.out.println("✅ Plugin de Pedidos carregado como API");
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar plugins: " + e.getMessage());
        }
    }
} 