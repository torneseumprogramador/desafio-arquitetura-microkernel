package core.controllers;

import core.SimpleController;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * Controller para a página inicial da aplicação.
 * Mostra informações sobre o sistema Microkernel.
 */
public class HomeController extends SimpleController {
    
    // Lista de plugins carregados (será injetada pelo Main)
    private static List<PluginInfo> loadedPlugins = new ArrayList<>();
    
    /**
     * Define os plugins carregados para o controller.
     * @param plugins Lista de plugins carregados
     */
    public static void setLoadedPlugins(List<PluginInfo> plugins) {
        loadedPlugins = new ArrayList<>(plugins);
    }
    
    /**
     * Retorna os plugins carregados.
     * @return Lista de plugins carregados
     */
    public static List<PluginInfo> getLoadedPlugins() {
        return new ArrayList<>(loadedPlugins);
    }
    
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
        StringBuilder pluginsJson = new StringBuilder();
        for (int i = 0; i < loadedPlugins.size(); i++) {
            PluginInfo plugin = loadedPlugins.get(i);
            pluginsJson.append("\"").append(plugin.getName()).append(" - ").append(plugin.getDescription()).append("\"");
            if (i < loadedPlugins.size() - 1) {
                pluginsJson.append(",");
            }
        }
        
        StringBuilder endpointsJson = new StringBuilder();
        endpointsJson.append("\"home\": \"/\",");
        endpointsJson.append("\"health\": \"/api/health\",");
        endpointsJson.append("\"api_info\": \"/api\",");
        endpointsJson.append("\"docs\": \"/api/docs\"");
        
        // Adicionar endpoints dos plugins carregados
        for (PluginInfo plugin : loadedPlugins) {
            endpointsJson.append(",\"").append(plugin.getEndpointName()).append("\": \"").append(plugin.getBasePath()).append("\"");
        }
        
        return "{" +
               "\"application\": \"Sistema Microkernel Ecommerce\"," +
               "\"version\": \"1.0.0\"," +
               "\"description\": \"Sistema de e-commerce baseado em arquitetura Microkernel\"," +
               "\"architecture\": \"Plugin-based Microkernel\"," +
               "\"status\": \"running\"," +
               "\"plugins_loaded\": " + loadedPlugins.size() + "," +
               "\"endpoints\": {" + endpointsJson.toString() + "}," +
               "\"plugins\": [" + pluginsJson.toString() + "]," +
               "\"features\": [" +
               "  \"Arquitetura Microkernel\"," +
               "  \"Plugins autocontidos\"," +
               "  \"Sistema de rotas declarativo\"," +
               "  \"Banco de dados SQLite\"," +
               "  \"API REST\"," +
               "  \"Seed automático\"," +
               "  \"Documentação dinâmica\"" +
               "]" +
               "}";
    }
    
    private String buildApiInfoJson() {
        StringBuilder endpointsJson = new StringBuilder();
        endpointsJson.append("\"health\": {");
        endpointsJson.append("\"method\": \"GET\",");
        endpointsJson.append("\"path\": \"/api/health\",");
        endpointsJson.append("\"description\": \"Verificar status da aplicação\"");
        endpointsJson.append("}");
        
        // Adicionar endpoints dos plugins carregados
        for (PluginInfo plugin : loadedPlugins) {
            endpointsJson.append(",\"").append(plugin.getEndpointName()).append("\": {");
            endpointsJson.append("\"method\": \"GET\",");
            endpointsJson.append("\"path\": \"").append(plugin.getBasePath()).append("\",");
            endpointsJson.append("\"description\": \"").append(plugin.getDescription()).append("\"");
            endpointsJson.append("}");
        }
        
        return "{" +
               "\"api\": \"Microkernel Ecommerce API\"," +
               "\"version\": \"1.0.0\"," +
               "\"base_url\": \"/api\"," +
               "\"plugins_loaded\": " + loadedPlugins.size() + "," +
               "\"endpoints\": {" + endpointsJson.toString() + "}," +
               "\"documentation\": \"/api/docs\"" +
               "}";
    }
    
    private String buildApiDocsJson() {
        StringBuilder endpointsJson = new StringBuilder();
        
        // Adicionar documentação dos plugins carregados
        for (int i = 0; i < loadedPlugins.size(); i++) {
            PluginInfo plugin = loadedPlugins.get(i);
            endpointsJson.append("\"").append(plugin.getEndpointName()).append("\": {");
            endpointsJson.append("\"base_path\": \"").append(plugin.getBasePath()).append("\",");
            endpointsJson.append("\"operations\": [");
            
            // Adicionar operações do plugin
            List<String> routes = plugin.getPlugin().getAvailableRoutes();
            for (int j = 0; j < routes.size(); j++) {
                String route = routes.get(j);
                String[] parts = route.split(" - ", 2);
                String methodPath = parts[0].trim();
                String description = parts.length > 1 ? parts[1].trim() : "";
                
                String[] methodPathParts = methodPath.split("\\s+", 2);
                String method = methodPathParts[0];
                String path = methodPathParts[1];
                
                endpointsJson.append("{\"method\": \"").append(method).append("\", ");
                endpointsJson.append("\"path\": \"").append(path).append("\", ");
                endpointsJson.append("\"description\": \"").append(description).append("\"}");
                
                if (j < routes.size() - 1) {
                    endpointsJson.append(",");
                }
            }
            
            endpointsJson.append("]}");
            
            if (i < loadedPlugins.size() - 1) {
                endpointsJson.append(",");
            }
        }
        
        return "{" +
               "\"documentation\": \"Microkernel Ecommerce API Documentation\"," +
               "\"version\": \"1.0.0\"," +
               "\"plugins_loaded\": " + loadedPlugins.size() + "," +
               "\"endpoints\": {" + endpointsJson.toString() + "}" +
               "}";
    }
    
    /**
     * Classe para armazenar informações dos plugins carregados.
     */
    public static class PluginInfo {
        private final String name;
        private final String basePath;
        private final core.Plugin plugin;

        public PluginInfo(String name, String basePath, core.Plugin plugin) {
            this.name = name;
            this.basePath = basePath;
            this.plugin = plugin;
        }

        public String getName() {
            return name;
        }

        public String getBasePath() {
            return basePath;
        }

        public core.Plugin getPlugin() {
            return plugin;
        }

        public String getDescription() {
            // Extrair descrição do nome completo
            if (name.contains("Usuários")) return "Gerenciamento de usuários";
            if (name.contains("Produtos")) return "Gerenciamento de produtos";
            if (name.contains("Pedidos")) return "Gerenciamento de pedidos";
            return "Plugin customizado";
        }

        public String getEndpointName() {
            // Extrair nome do endpoint do basePath
            return basePath.replace("/api/", "");
        }
    }
} 