package core;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gerenciador de seed para inserir dados iniciais no banco de dados.
 * Responsável por popular o banco com dados de exemplo quando necessário.
 */
public class SeedManager {
    
    private final DatabaseManager dbManager;
    
    public SeedManager() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    /**
     * Executa o seed do banco de dados com dados iniciais.
     * @param forceSeed true para forçar inserção mesmo se já existirem dados
     */
    public void executeSeed(boolean forceSeed) {
        System.out.println("🌱 Executando seed do banco de dados...");
        
        try (Connection connection = dbManager.getConnection();
             Statement stmt = connection.createStatement()) {
            
            if (forceSeed) {
                // Limpar dados existentes
                stmt.execute("DELETE FROM order_products");
                stmt.execute("DELETE FROM orders");
                stmt.execute("DELETE FROM products");
                stmt.execute("DELETE FROM users");
                System.out.println("🧹 Dados existentes removidos");
            }
            
            // Inserir usuários de exemplo
            stmt.execute("INSERT OR IGNORE INTO users (name, email) VALUES " +
                "('João Silva', 'joao@email.com')," +
                "('Maria Santos', 'maria@email.com')," +
                "('Pedro Costa', 'pedro@email.com')," +
                "('Ana Oliveira', 'ana@email.com')," +
                "('Carlos Ferreira', 'carlos@email.com')");
            
            // Inserir produtos de exemplo
            stmt.execute("INSERT OR IGNORE INTO products (name, description, price, stock) VALUES " +
                "('Notebook Dell Inspiron', 'Notebook Dell Inspiron 15 polegadas, Intel i5, 8GB RAM', 2999.99, 10)," +
                "('Mouse Wireless Logitech', 'Mouse sem fio Logitech M185, 1000 DPI', 89.90, 50)," +
                "('Teclado Mecânico RGB', 'Teclado mecânico RGB com switches Blue', 299.99, 15)," +
                "('Monitor LED 24\"', 'Monitor LED 24 polegadas Full HD', 599.99, 8)," +
                "('Headset Gamer', 'Headset gamer com microfone e RGB', 199.99, 20)," +
                "('Webcam HD', 'Webcam HD 1080p com microfone integrado', 159.99, 12)," +
                "('SSD 500GB', 'SSD SATA 500GB para notebook/desktop', 399.99, 25)," +
                "('Memória RAM 8GB', 'Memória RAM DDR4 8GB 2666MHz', 249.99, 30)");
            
            System.out.println("✅ Seed executado com sucesso!");
            System.out.println("📊 Dados inseridos:");
            System.out.println("   👥 5 usuários de exemplo");
            System.out.println("   📦 8 produtos de exemplo");
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao executar seed: " + e.getMessage());
        }
    }
    
    /**
     * Executa o seed padrão (sem forçar inserção).
     */
    public void executeSeed() {
        executeSeed(false);
    }
    
    /**
     * Verifica se existem dados no banco.
     * @return true se existem dados, false caso contrário
     */
    public boolean hasData() {
        try (Connection connection = dbManager.getConnection();
             Statement stmt = connection.createStatement()) {
            
            var result = stmt.executeQuery("SELECT COUNT(*) as count FROM users");
            if (result.next()) {
                return result.getInt("count") > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao verificar dados: " + e.getMessage());
        }
        
        return false;
    }
} 