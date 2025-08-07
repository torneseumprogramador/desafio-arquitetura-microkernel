package core;

import java.util.List;
import java.util.Map;

/**
 * Interface base para todos os plugins do sistema.
 */
public interface Plugin {
    
    /**
     * Retorna o nome do plugin.
     * @return Nome do plugin
     */
    String getName();
    
    /**
     * Executa a funcionalidade do plugin.
     */
    void execute();
    
    /**
     * Retorna as rotas disponíveis do plugin.
     * @return Lista de rotas do plugin
     */
    default List<String> getAvailableRoutes() {
        return List.of();
    }
    
    /**
     * Retorna o emoji do plugin.
     * @return Emoji do plugin
     */
    default String getEmoji() {
        return "🔧";
    }
    
    /**
     * Retorna informações de documentação Swagger do plugin.
     * @return Mapa com informações de documentação
     */
    default Map<String, Object> getSwaggerInfo() {
        return Map.of();
    }
    
    /**
     * Retorna os schemas OpenAPI do plugin.
     * @return Mapa com schemas OpenAPI
     */
    default Map<String, Object> getOpenApiSchemas() {
        return Map.of();
    }
    
    /**
     * Retorna os paths OpenAPI do plugin.
     * @return Mapa com paths OpenAPI
     */
    default Map<String, Object> getOpenApiPaths() {
        return Map.of();
    }
    
    /**
     * Retorna as tags para agrupamento dos endpoints.
     * @return Lista de tags do plugin
     */
    default List<String> getOpenApiTags() {
        return List.of();
    }
} 