package core;

import java.lang.reflect.Method;

/**
 * Representa uma rota HTTP com método, path e handler.
 */
public class Route {
    private final String method;
    private final String path;
    private final String handlerMethod;
    private final Object controller;
    
    public Route(String method, String path, String handlerMethod, Object controller) {
        this.method = method;
        this.path = path;
        this.handlerMethod = handlerMethod;
        this.controller = controller;
    }
    
    public String getMethod() {
        return method;
    }
    
    public String getPath() {
        return path;
    }
    
    public String getHandlerMethod() {
        return handlerMethod;
    }
    
    public Object getController() {
        return controller;
    }
    
    /**
     * Verifica se esta rota pode processar a requisição.
     * @param requestMethod Método HTTP da requisição
     * @param requestPath Path da requisição
     * @return true se pode processar, false caso contrário
     */
    public boolean canHandle(String requestMethod, String requestPath) {
        if (!method.equals(requestMethod)) {
            return false;
        }
        
        // Converter path pattern para regex
        String pattern = path.replaceAll("\\{[^}]+\\}", "\\\\d+");
        return requestPath.matches(pattern);
    }
    
    /**
     * Extrai parâmetros do path baseado no pattern.
     * @param requestPath Path da requisição
     * @return Array com os parâmetros extraídos
     */
    public String[] extractParams(String requestPath) {
        String[] pathParts = path.split("/");
        String[] requestParts = requestPath.split("/");
        
        if (pathParts.length != requestParts.length) {
            return new String[0];
        }
        
        java.util.List<String> params = new java.util.ArrayList<>();
        for (int i = 0; i < pathParts.length; i++) {
            if (pathParts[i].startsWith("{") && pathParts[i].endsWith("}")) {
                params.add(requestParts[i]);
            }
        }
        
        return params.toArray(new String[0]);
    }
} 