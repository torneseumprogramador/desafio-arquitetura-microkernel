package core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import core.controllers.HomeController;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Gerador dinâmico de documentação Swagger/OpenAPI.
 * Gera documentação baseada nos plugins carregados.
 */
public class SwaggerGenerator {
    private static final ObjectMapper mapper = new ObjectMapper();
    
    /**
     * Gera a especificação OpenAPI 3.0 em JSON.
     * @param loadedPlugins Lista de plugins carregados
     * @return Especificação OpenAPI em JSON
     */
    public static String generateOpenApiJson(List<HomeController.PluginInfo> loadedPlugins) {
        try {
            ObjectNode openApi = mapper.createObjectNode();
            
            // Info
            ObjectNode info = mapper.createObjectNode();
            info.put("title", "Microkernel Ecommerce API");
            info.put("description", "API REST do Sistema Microkernel Ecommerce com arquitetura plugin-based");
            info.put("version", "1.0.0");
            info.put("contact", "Prof. Danilo Aparecido");
            openApi.put("openapi", "3.0.0");
            openApi.set("info", info);
            
            // Servers
            ArrayNode servers = mapper.createArrayNode();
            ObjectNode server = mapper.createObjectNode();
            server.put("url", "http://localhost:8080");
            server.put("description", "Servidor de desenvolvimento");
            servers.add(server);
            openApi.set("servers", servers);
            
            // Paths - Coletar dinamicamente dos plugins
            ObjectNode paths = mapper.createObjectNode();
            
            // Adicionar paths do sistema (sempre disponíveis)
            addSystemPaths(paths);
            
            // Adicionar paths dos plugins carregados
            for (HomeController.PluginInfo plugin : loadedPlugins) {
                addPluginPaths(paths, plugin);
            }
            
            openApi.set("paths", paths);
            
            // Components - Coletar dinamicamente dos plugins
            ObjectNode components = mapper.createObjectNode();
            
            // Schemas
            ObjectNode schemas = mapper.createObjectNode();
            addSystemSchemas(schemas);
            addPluginSchemas(schemas, loadedPlugins);
            components.set("schemas", schemas);
            
            // Tags
            ArrayNode tags = mapper.createArrayNode();
            addSystemTags(tags);
            addPluginTags(tags, loadedPlugins);
            openApi.set("tags", tags);
            
            openApi.set("components", components);
            
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(openApi);
            
        } catch (Exception e) {
            return "{\"error\": \"Erro ao gerar documentação OpenAPI: " + e.getMessage() + "\"}";
        }
    }
    
    private static void addSystemPaths(ObjectNode paths) {
        // GET /
        ObjectNode home = mapper.createObjectNode();
        ObjectNode homeGet = mapper.createObjectNode();
        homeGet.put("summary", "Página inicial da API");
        homeGet.put("description", "Retorna informações sobre o sistema Microkernel");
        homeGet.set("tags", mapper.createArrayNode().add("System"));
        homeGet.set("responses", createResponses("Informações do sistema"));
        home.set("get", homeGet);
        paths.set("/", home);
        
        // GET /api
        ObjectNode api = mapper.createObjectNode();
        ObjectNode apiGet = mapper.createObjectNode();
        apiGet.put("summary", "Informações da API");
        apiGet.put("description", "Retorna informações sobre os endpoints disponíveis");
        apiGet.set("tags", mapper.createArrayNode().add("System"));
        apiGet.set("responses", createResponses("Informações da API"));
        api.set("get", apiGet);
        paths.set("/api", api);
        
        // GET /api/docs
        ObjectNode docs = mapper.createObjectNode();
        ObjectNode docsGet = mapper.createObjectNode();
        docsGet.put("summary", "Documentação da API");
        docsGet.put("description", "Retorna a documentação completa da API");
        docsGet.set("tags", mapper.createArrayNode().add("System"));
        docsGet.set("responses", createResponses("Documentação da API"));
        docs.set("get", docsGet);
        paths.set("/api/docs", docs);
        
        // GET /api/health
        ObjectNode health = mapper.createObjectNode();
        ObjectNode healthGet = mapper.createObjectNode();
        healthGet.put("summary", "Status da aplicação");
        healthGet.put("description", "Verifica o status da aplicação e do banco de dados");
        healthGet.set("tags", mapper.createArrayNode().add("System"));
        healthGet.set("responses", createResponses("Status da aplicação"));
        health.set("get", healthGet);
        paths.set("/api/health", health);
        
        // GET /api/health/detailed
        ObjectNode healthDetailed = mapper.createObjectNode();
        ObjectNode healthDetailedGet = mapper.createObjectNode();
        healthDetailedGet.put("summary", "Status detalhado");
        healthDetailedGet.put("description", "Retorna informações detalhadas sobre o status da aplicação");
        healthDetailedGet.set("tags", mapper.createArrayNode().add("System"));
        healthDetailedGet.set("responses", createResponses("Status detalhado"));
        healthDetailed.set("get", healthDetailedGet);
        paths.set("/api/health/detailed", healthDetailed);
        
        // GET /api/health/database
        ObjectNode healthDb = mapper.createObjectNode();
        ObjectNode healthDbGet = mapper.createObjectNode();
        healthDbGet.put("summary", "Status do banco de dados");
        healthDbGet.put("description", "Verifica especificamente o status do banco de dados");
        healthDbGet.set("tags", mapper.createArrayNode().add("System"));
        healthDbGet.set("responses", createResponses("Status do banco de dados"));
        healthDb.set("get", healthDbGet);
        paths.set("/api/health/database", healthDb);
        
        // GET /api/swagger
        ObjectNode swagger = mapper.createObjectNode();
        ObjectNode swaggerGet = mapper.createObjectNode();
        swaggerGet.put("summary", "Documentação OpenAPI");
        swaggerGet.put("description", "Retorna a especificação OpenAPI 3.0 em JSON");
        swaggerGet.set("tags", mapper.createArrayNode().add("System"));
        swaggerGet.set("responses", createResponses("Documentação OpenAPI"));
        swagger.set("get", swaggerGet);
        paths.set("/api/swagger", swagger);
        
        // GET /api/swagger-ui
        ObjectNode swaggerUi = mapper.createObjectNode();
        ObjectNode swaggerUiGet = mapper.createObjectNode();
        swaggerUiGet.put("summary", "Interface Swagger UI");
        swaggerUiGet.put("description", "Retorna a interface web do Swagger UI");
        swaggerUiGet.set("tags", mapper.createArrayNode().add("System"));
        swaggerUiGet.set("responses", createResponses("Interface Swagger UI"));
        swaggerUi.set("get", swaggerUiGet);
        paths.set("/api/swagger-ui", swaggerUi);
    }
    
    private static void addPluginPaths(ObjectNode paths, HomeController.PluginInfo plugin) {
        // Obter paths do próprio plugin
        Map<String, Object> pluginPaths = plugin.getPlugin().getOpenApiPaths();
        
        // Converter para ObjectNode e adicionar aos paths
        for (Map.Entry<String, Object> entry : pluginPaths.entrySet()) {
            String path = entry.getKey();
            Object pathInfo = entry.getValue();
            
            // Converter Map para ObjectNode
            ObjectNode pathNode = mapper.valueToTree(pathInfo);
            paths.set(path, pathNode);
        }
    }
    
    private static ObjectNode createResponses(String description) {
        ObjectNode responses = mapper.createObjectNode();
        
        ObjectNode ok = mapper.createObjectNode();
        ok.put("description", description);
        ObjectNode content = mapper.createObjectNode();
        ObjectNode applicationJson = mapper.createObjectNode();
        applicationJson.put("schema", "object");
        content.set("application/json", applicationJson);
        ok.set("content", content);
        responses.set("200", ok);
        
        ObjectNode notFound = mapper.createObjectNode();
        notFound.put("description", "Recurso não encontrado");
        responses.set("404", notFound);
        
        ObjectNode badRequest = mapper.createObjectNode();
        badRequest.put("description", "Dados inválidos");
        responses.set("400", badRequest);
        
        return responses;
    }
    
    private static void addSystemSchemas(ObjectNode schemas) {
        // Health Response
        ObjectNode health = mapper.createObjectNode();
        health.put("type", "object");
        ObjectNode healthProps = mapper.createObjectNode();
        healthProps.set("status", mapper.createObjectNode().put("type", "string"));
        healthProps.set("message", mapper.createObjectNode().put("type", "string"));
        healthProps.set("timestamp", mapper.createObjectNode().put("type", "string"));
        health.set("properties", healthProps);
        schemas.set("HealthResponse", health);
        
        // API Info
        ObjectNode apiInfo = mapper.createObjectNode();
        apiInfo.put("type", "object");
        ObjectNode apiInfoProps = mapper.createObjectNode();
        apiInfoProps.set("api", mapper.createObjectNode().put("type", "string"));
        apiInfoProps.set("version", mapper.createObjectNode().put("type", "string"));
        apiInfoProps.set("plugins_loaded", mapper.createObjectNode().put("type", "integer"));
        apiInfo.set("properties", apiInfoProps);
        schemas.set("ApiInfo", apiInfo);
    }
    
    private static void addPluginSchemas(ObjectNode schemas, List<HomeController.PluginInfo> plugins) {
        for (HomeController.PluginInfo plugin : plugins) {
            // Obter schemas do próprio plugin
            Map<String, Object> pluginSchemas = plugin.getPlugin().getOpenApiSchemas();
            
            // Converter para ObjectNode e adicionar aos schemas
            for (Map.Entry<String, Object> entry : pluginSchemas.entrySet()) {
                String schemaName = entry.getKey();
                Object schemaInfo = entry.getValue();
                
                // Converter Map para ObjectNode
                ObjectNode schemaNode = mapper.valueToTree(schemaInfo);
                schemas.set(schemaName, schemaNode);
            }
        }
    }

    private static void addSystemTags(ArrayNode tags) {
        ObjectNode systemTag = mapper.createObjectNode();
        systemTag.put("name", "System");
        systemTag.put("description", "Endpoints relacionados ao sistema principal");
        tags.add(systemTag);
    }

    private static void addPluginTags(ArrayNode tags, List<HomeController.PluginInfo> plugins) {
        for (HomeController.PluginInfo plugin : plugins) {
            // Obter tags específicas do plugin
            List<String> pluginTags = plugin.getPlugin().getOpenApiTags();
            
            for (String tagName : pluginTags) {
                // Verificar se a tag já existe para evitar duplicação
                boolean tagExists = false;
                for (int i = 0; i < tags.size(); i++) {
                    if (tags.get(i).get("name").asText().equals(tagName)) {
                        tagExists = true;
                        break;
                    }
                }
                
                if (!tagExists) {
                    ObjectNode pluginTag = mapper.createObjectNode();
                    pluginTag.put("name", tagName);
                    pluginTag.put("description", "Endpoints relacionados a " + tagName.toLowerCase());
                    tags.add(pluginTag);
                }
            }
        }
    }
}
