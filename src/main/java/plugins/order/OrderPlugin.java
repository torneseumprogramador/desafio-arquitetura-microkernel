package plugins.order;

import core.Plugin;
import core.DatabaseManager;
import core.HttpHandler;
import plugins.order.services.OrderService;
import plugins.order.repositories.OrderRepository;
import plugins.order.routes.OrderRoutes;
import java.sql.Connection;

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
        return "Order Plugin - Gerenciamento de Pedidos";
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