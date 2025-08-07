package core.controllers;

import core.SimpleController;
import core.SwaggerGenerator;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;

/**
 * Controller para servir a documentação Swagger/OpenAPI.
 * Gera documentação dinâmica baseada nos plugins carregados.
 */
public class SwaggerController extends SimpleController {
    
    /**
     * GET /api/swagger - Documentação Swagger/OpenAPI
     */
    public void getSwaggerJson(HttpExchange exchange) throws IOException {
        // Obter plugins carregados do HomeController
        List<HomeController.PluginInfo> loadedPlugins = HomeController.getLoadedPlugins();
        
        // Gerar documentação OpenAPI
        String openApiJson = SwaggerGenerator.generateOpenApiJson(loadedPlugins);
        
        sendJsonResponse(exchange, 200, openApiJson);
    }
    
    /**
     * GET /api/swagger-ui - Interface Swagger UI
     */
    public void getSwaggerUi(HttpExchange exchange) throws IOException {
        String html = generateSwaggerUiHtml();
        
        exchange.getResponseHeaders().add("Content-Type", "text/html");
        sendResponse(exchange, 200, html, "text/html");
    }
    
    private String generateSwaggerUiHtml() {
        return "<!DOCTYPE html>\n" +
               "<html lang=\"pt-BR\">\n" +
               "<head>\n" +
               "    <meta charset=\"UTF-8\">\n" +
               "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
               "    <title>Microkernel Ecommerce API - Swagger UI</title>\n" +
               "    <link rel=\"stylesheet\" type=\"text/css\" href=\"https://unpkg.com/swagger-ui-dist@5.9.0/swagger-ui.css\" />\n" +
               "    <style>\n" +
               "        html { box-sizing: border-box; overflow: -moz-scrollbars-vertical; overflow-y: scroll; }\n" +
               "        *, *:before, *:after { box-sizing: inherit; }\n" +
               "        body { margin:0; background: #fafafa; }\n" +
               "        .swagger-ui .topbar { display: none; }\n" +
               "        .swagger-ui .info .title { color: #3b4151; }\n" +
               "        .swagger-ui .info .title small { color: #606f7b; }\n" +
               "    </style>\n" +
               "</head>\n" +
               "<body>\n" +
               "    <div id=\"swagger-ui\"></div>\n" +
               "    <script src=\"https://unpkg.com/swagger-ui-dist@5.9.0/swagger-ui-bundle.js\"></script>\n" +
               "    <script src=\"https://unpkg.com/swagger-ui-dist@5.9.0/swagger-ui-standalone-preset.js\"></script>\n" +
               "    <script>\n" +
               "        window.onload = function() {\n" +
               "            const ui = SwaggerUIBundle({\n" +
               "                url: '/api/swagger',\n" +
               "                dom_id: '#swagger-ui',\n" +
               "                deepLinking: true,\n" +
               "                presets: [\n" +
               "                    SwaggerUIBundle.presets.apis,\n" +
               "                    SwaggerUIStandalonePreset\n" +
               "                ],\n" +
               "                plugins: [\n" +
               "                    SwaggerUIBundle.plugins.DownloadUrl\n" +
               "                ],\n" +
               "                layout: \"StandaloneLayout\",\n" +
               "                validatorUrl: null,\n" +
               "                docExpansion: 'list',\n" +
               "                filter: true,\n" +
               "                showExtensions: true,\n" +
               "                showCommonExtensions: true,\n" +
               "                supportedSubmitMethods: ['get', 'post', 'put', 'delete', 'patch']\n" +
               "            });\n" +
               "        };\n" +
               "    </script>\n" +
               "</body>\n" +
               "</html>";
    }
}
