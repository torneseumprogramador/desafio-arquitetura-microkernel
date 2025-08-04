package plugins;

import core.Plugin;
import core.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Plugin responsável pelo domínio de Produtos do Pedido.
 * Simula funcionalidades de adicionar produtos aos pedidos.
 */
public class OrderProductPlugin implements Plugin {
    
    @Override
    public String getName() {
        return "Order Product Plugin - Gerenciamento de Produtos do Pedido";
    }
    
    @Override
    public void execute() {
        System.out.println("Adicionando produto ao pedido...");
        
        try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            Connection conn = dbManager.getConnection();
            
            // Verificar produtos em um pedido específico
            String selectSQL = "SELECT op.order_id, p.name as product_name, op.quantity, op.unit_price, " +
                "(op.quantity * op.unit_price) as total_price " +
                "FROM order_products op " +
                "JOIN products p ON op.product_id = p.id " +
                "WHERE op.order_id = (SELECT MAX(id) FROM orders) " +
                "ORDER BY op.id";
            
            try (PreparedStatement pstmt = conn.prepareStatement(selectSQL);
                 ResultSet rs = pstmt.executeQuery()) {
                
                System.out.println("  → Produtos no pedido mais recente:");
                double orderTotal = 0;
                
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    String productName = rs.getString("product_name");
                    int quantity = rs.getInt("quantity");
                    double unitPrice = rs.getDouble("unit_price");
                    double totalPrice = rs.getDouble("total_price");
                    
                    System.out.println("    - Produto: " + productName);
                    System.out.println("      Quantidade: " + quantity);
                    System.out.println("      Preço unitário: R$ " + String.format("%.2f", unitPrice));
                    System.out.println("      Total: R$ " + String.format("%.2f", totalPrice));
                    System.out.println();
                    
                    orderTotal += totalPrice;
                }
                
                System.out.println("  → Total do pedido: R$ " + String.format("%.2f", orderTotal));
            }
            
            // Adicionar novo produto ao pedido
            String insertSQL = "INSERT INTO order_products (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setInt(1, 1); // Pedido #1
                pstmt.setInt(2, 3); // Teclado Mecânico
                pstmt.setInt(3, 2); // Quantidade: 2
                pstmt.setDouble(4, 299.99); // Preço unitário
                pstmt.executeUpdate();
                
                System.out.println("  → 2x Teclado Mecânico adicionado ao pedido #1");
            }
            
        } catch (SQLException e) {
            System.err.println("  ❌ Erro ao executar operações de produto do pedido: " + e.getMessage());
        }
    }
} 