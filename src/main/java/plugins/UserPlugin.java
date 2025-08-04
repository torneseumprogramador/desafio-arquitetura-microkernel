package plugins;

import core.Plugin;
import core.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Plugin responsável pelo domínio de Usuários.
 * Simula funcionalidades de cadastro e gerenciamento de usuários.
 */
public class UserPlugin implements Plugin {
    
    @Override
    public String getName() {
        return "User Plugin - Gerenciamento de Usuários";
    }
    
    @Override
    public void execute() {
        System.out.println("Cadastrando usuário...");
        
        try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            Connection conn = dbManager.getConnection();
            
            // Cadastrar novo usuário
            String insertSQL = "INSERT OR IGNORE INTO users (name, email) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setString(1, "Carlos Ferreira");
                pstmt.setString(2, "carlos@email.com");
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("  → Usuário 'Carlos Ferreira' cadastrado com sucesso!");
                } else {
                    System.out.println("  → Usuário já existe no sistema");
                }
            }
            
            // Listar usuários cadastrados
            System.out.println("  → Listando usuários cadastrados:");
            String selectSQL = "SELECT id, name, email FROM users ORDER BY id DESC LIMIT 3";
            try (PreparedStatement pstmt = conn.prepareStatement(selectSQL);
                 ResultSet rs = pstmt.executeQuery()) {
                
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    System.out.println("    - ID: " + id + ", Nome: " + name + ", Email: " + email);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("  ❌ Erro ao executar operações de usuário: " + e.getMessage());
        }
    }
} 