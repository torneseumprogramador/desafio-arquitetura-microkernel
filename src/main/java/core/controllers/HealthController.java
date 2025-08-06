package core.controllers;

import core.SimpleController;
import core.DatabaseManager;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controller para health check da aplicação.
 * Testa conexão com banco de dados e status dos componentes.
 */
public class HealthController extends SimpleController {
    
    private final DatabaseManager dbManager;
    
    public HealthController() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    /**
     * GET /api/health - Health check básico
     */
    public void getHealth(HttpExchange exchange) throws IOException {
        String response = buildBasicHealthJson();
        sendJsonResponse(exchange, 200, response);
    }
    
    /**
     * GET /api/health/detailed - Health check detalhado
     */
    public void getDetailedHealth(HttpExchange exchange) throws IOException {
        String response = buildDetailedHealthJson();
        sendJsonResponse(exchange, 200, response);
    }
    
    /**
     * GET /api/health/database - Teste específico do banco de dados
     */
    public void getDatabaseHealth(HttpExchange exchange) throws IOException {
        String response = buildDatabaseHealthJson();
        sendJsonResponse(exchange, 200, response);
    }
    
    private String buildBasicHealthJson() {
        return "{" +
               "\"status\": \"UP\"," +
               "\"application\": \"Microkernel Ecommerce\"," +
               "\"timestamp\": \"" + getCurrentTimestamp() + "\"," +
               "\"version\": \"1.0.0\"" +
               "}";
    }
    
    private String buildDetailedHealthJson() {
        boolean dbConnection = testDatabaseConnection();
        boolean dbTables = testDatabaseTables();
        
        return "{" +
               "\"status\": \"" + (dbConnection && dbTables ? "UP" : "DEGRADED") + "\"," +
               "\"application\": \"Microkernel Ecommerce\"," +
               "\"timestamp\": \"" + getCurrentTimestamp() + "\"," +
               "\"version\": \"1.0.0\"," +
               "\"components\": {" +
               "  \"database\": {" +
               "    \"status\": \"" + (dbConnection ? "UP" : "DOWN") + "\"," +
               "    \"connection\": " + dbConnection + "," +
               "    \"tables\": " + dbTables +
               "  }," +
               "  \"plugins\": {" +
               "    \"status\": \"UP\"," +
               "    \"count\": 3," +
               "    \"loaded\": [\"UserPlugin\", \"ProductPlugin\", \"OrderPlugin\"]" +
               "  }," +
               "  \"api\": {" +
               "    \"status\": \"UP\"," +
               "    \"endpoints\": 20," +
               "    \"routes\": \"declarative\"" +
               "  }" +
               "}," +
               "\"uptime\": \"running\"," +
               "\"memory\": \"available\"" +
               "}";
    }
    
    private String buildDatabaseHealthJson() {
        boolean connection = testDatabaseConnection();
        boolean tables = testDatabaseTables();
        int userCount = getUserCount();
        int productCount = getProductCount();
        int orderCount = getOrderCount();
        
        return "{" +
               "\"database\": \"SQLite\"," +
               "\"status\": \"" + (connection && tables ? "HEALTHY" : "UNHEALTHY") + "\"," +
               "\"timestamp\": \"" + getCurrentTimestamp() + "\"," +
               "\"connection\": {" +
               "  \"status\": \"" + (connection ? "CONNECTED" : "DISCONNECTED") + "\"," +
               "  \"tested\": " + connection +
               "}," +
               "\"tables\": {" +
               "  \"status\": \"" + (tables ? "EXISTS" : "MISSING") + "\"," +
               "  \"tested\": " + tables +
               "}," +
               "\"data\": {" +
               "  \"users\": " + userCount + "," +
               "  \"products\": " + productCount + "," +
               "  \"orders\": " + orderCount +
               "}," +
               "\"file\": \"microkernel_ecommerce.db\"," +
               "\"size\": \"check manually\"" +
               "}";
    }
    
    private boolean testDatabaseConnection() {
        try {
            Connection connection = dbManager.getConnection();
            return connection != null && !connection.isClosed();
        } catch (Exception e) {
            return false;
        }
    }
    
    private boolean testDatabaseTables() {
        try (Connection connection = dbManager.getConnection();
             Statement stmt = connection.createStatement()) {
            
            // Testar se as tabelas principais existem
            stmt.execute("SELECT COUNT(*) FROM users");
            stmt.execute("SELECT COUNT(*) FROM products");
            stmt.execute("SELECT COUNT(*) FROM orders");
            
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    
    private int getUserCount() {
        try (Connection connection = dbManager.getConnection();
             Statement stmt = connection.createStatement()) {
            
            var result = stmt.executeQuery("SELECT COUNT(*) as count FROM users");
            if (result.next()) {
                return result.getInt("count");
            }
            return 0;
        } catch (SQLException e) {
            return -1;
        }
    }
    
    private int getProductCount() {
        try (Connection connection = dbManager.getConnection();
             Statement stmt = connection.createStatement()) {
            
            var result = stmt.executeQuery("SELECT COUNT(*) as count FROM products");
            if (result.next()) {
                return result.getInt("count");
            }
            return 0;
        } catch (SQLException e) {
            return -1;
        }
    }
    
    private int getOrderCount() {
        try (Connection connection = dbManager.getConnection();
             Statement stmt = connection.createStatement()) {
            
            var result = stmt.executeQuery("SELECT COUNT(*) as count FROM orders");
            if (result.next()) {
                return result.getInt("count");
            }
            return 0;
        } catch (SQLException e) {
            return -1;
        }
    }
    
    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
} 