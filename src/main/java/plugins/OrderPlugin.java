package plugins;

import core.Plugin;
import core.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Plugin responsável pelo domínio de Pedidos.
 * Simula funcionalidades de geração e processamento de pedidos.
 */
public class OrderPlugin implements Plugin {
    
    @Override
    public String getName() {
        return "Order Plugin - Gerenciamento de Pedidos";
    }
    
    @Override
    public void execute() {
        System.out.println("Gerando pedido...");
        
        try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            Connection conn = dbManager.getConnection();
            
            // Criar novo pedido
            String insertOrderSQL = "INSERT INTO orders (user_id, total_amount, status) VALUES (?, ?, ?)";
            int orderId = 0;
            
            try (PreparedStatement pstmt = conn.prepareStatement(insertOrderSQL)) {
                pstmt.setInt(1, 1); // Usuário João Silva
                pstmt.setDouble(2, 3089.89); // Total do pedido
                pstmt.setString(3, "PENDING");
                pstmt.executeUpdate();
                
                // Obter ID do pedido criado (SQLite não suporta RETURN_GENERATED_KEYS)
                String getLastIdSQL = "SELECT last_insert_rowid()";
                try (PreparedStatement lastIdStmt = conn.prepareStatement(getLastIdSQL);
                     ResultSet rs = lastIdStmt.executeQuery()) {
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                        System.out.println("  → Pedido #" + orderId + " criado com sucesso!");
                    }
                }
            }
            
            // Adicionar produtos ao pedido
            if (orderId > 0) {
                String insertOrderProductSQL = "INSERT INTO order_products (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertOrderProductSQL)) {
                    // Notebook Dell
                    pstmt.setInt(1, orderId);
                    pstmt.setInt(2, 1);
                    pstmt.setInt(3, 1);
                    pstmt.setDouble(4, 2999.99);
                    pstmt.executeUpdate();
                    
                    // Mouse Wireless
                    pstmt.setInt(1, orderId);
                    pstmt.setInt(2, 2);
                    pstmt.setInt(3, 1);
                    pstmt.setDouble(4, 89.90);
                    pstmt.executeUpdate();
                    
                    System.out.println("  → Produtos adicionados ao pedido #" + orderId);
                }
            }
            
            // Listar pedidos recentes
            System.out.println("  → Pedidos recentes:");
            String selectSQL = "SELECT o.id, u.name as user_name, o.total_amount, o.status, o.created_at " +
                             "FROM orders o " +
                             "JOIN users u ON o.user_id = u.id " +
                             "ORDER BY o.id DESC LIMIT 3";
            
            try (PreparedStatement pstmt = conn.prepareStatement(selectSQL);
                 ResultSet rs = pstmt.executeQuery()) {
                
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String userName = rs.getString("user_name");
                    double totalAmount = rs.getDouble("total_amount");
                    String status = rs.getString("status");
                    
                    System.out.println("    - Pedido #" + id + ", Cliente: " + userName + 
                                     ", Total: R$ " + String.format("%.2f", totalAmount) + 
                                     ", Status: " + status);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("  ❌ Erro ao executar operações de pedido: " + e.getMessage());
        }
    }
} 