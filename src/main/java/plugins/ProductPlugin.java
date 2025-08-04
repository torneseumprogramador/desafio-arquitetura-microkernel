package plugins;

import core.Plugin;
import core.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Plugin responsável pelo domínio de Produtos.
 * Simula funcionalidades de listagem e gerenciamento de produtos.
 */
public class ProductPlugin implements Plugin {
    
    @Override
    public String getName() {
        return "Product Plugin - Gerenciamento de Produtos";
    }
    
    @Override
    public void execute() {
        System.out.println("Listando produtos...");
        
        try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            Connection conn = dbManager.getConnection();
            
            // Listar produtos disponíveis
            String selectSQL = "SELECT id, name, description, price, stock FROM products ORDER BY name";
            try (PreparedStatement pstmt = conn.prepareStatement(selectSQL);
                 ResultSet rs = pstmt.executeQuery()) {
                
                System.out.println("  → Produtos disponíveis no catálogo:");
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    double price = rs.getDouble("price");
                    int stock = rs.getInt("stock");
                    
                    System.out.println("    - ID: " + id + ", Nome: " + name);
                    System.out.println("      Descrição: " + description);
                    System.out.println("      Preço: R$ " + String.format("%.2f", price));
                    System.out.println("      Estoque: " + stock + " unidades");
                    System.out.println();
                }
            }
            
            // Atualizar estoque de um produto
            String updateSQL = "UPDATE products SET stock = stock - 1 WHERE id = 1 AND stock > 0";
            try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("  → Estoque do Notebook Dell atualizado (1 unidade vendida)");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("  ❌ Erro ao executar operações de produto: " + e.getMessage());
        }
    }
} 