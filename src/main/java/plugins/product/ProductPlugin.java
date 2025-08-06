package plugins.product;

import core.Plugin;
import core.DatabaseManager;
import core.HttpHandler;
import plugins.product.services.ProductService;
import plugins.product.repositories.ProductRepository;
import plugins.product.routes.ProductRoutes;
import java.sql.Connection;

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
        return "Product Plugin - Gerenciamento de Produtos";
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