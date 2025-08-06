package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gerenciador de banco de dados SQLite para o sistema Microkernel.
 * Responsável por inicializar a conexão e criar as tabelas necessárias.
 */
public class DatabaseManager {
    
    private static final String DB_URL = "jdbc:sqlite:microkernel_ecommerce.db";
    private static DatabaseManager instance;
    private Connection connection;
    
    private DatabaseManager() {
        initializeDatabase();
    }
    
    /**
     * Retorna a instância singleton do DatabaseManager.
     * @return Instância do DatabaseManager
     */
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    /**
     * Inicializa o banco de dados e cria as tabelas necessárias.
     */
    private void initializeDatabase() {
        try {
            // Criar conexão
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("📊 Conectado ao banco de dados SQLite");
            
            // Criar tabelas
            createTables();
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }
    
    /**
     * Cria as tabelas do sistema de ecommerce.
     */
    private void createTables() {
        try (Statement stmt = connection.createStatement()) {
            
            // Tabela de usuários
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "email TEXT UNIQUE NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")");
            
            // Tabela de produtos
            stmt.execute("CREATE TABLE IF NOT EXISTS products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "description TEXT," +
                "price DECIMAL(10,2) NOT NULL," +
                "stock INTEGER DEFAULT 0," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")");
            
            // Tabela de pedidos
            stmt.execute("CREATE TABLE IF NOT EXISTS orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "total_amount DECIMAL(10,2) NOT NULL," +
                "status TEXT DEFAULT 'PENDING'," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (user_id) REFERENCES users (id)" +
                ")");
            
            // Tabela de produtos do pedido
            stmt.execute("CREATE TABLE IF NOT EXISTS order_products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "order_id INTEGER NOT NULL," +
                "product_id INTEGER NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "unit_price DECIMAL(10,2) NOT NULL," +
                "FOREIGN KEY (order_id) REFERENCES orders (id)," +
                "FOREIGN KEY (product_id) REFERENCES products (id)" +
                ")");
            
            // Tabela de pagamentos
            stmt.execute("CREATE TABLE IF NOT EXISTS payments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "order_id INTEGER NOT NULL," +
                "amount DECIMAL(10,2) NOT NULL," +
                "payment_method TEXT NOT NULL," +
                "status TEXT DEFAULT 'PENDING'," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (order_id) REFERENCES orders (id)" +
                ")");
            
            System.out.println("📋 Tabelas criadas/verificadas com sucesso");
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao criar tabelas: " + e.getMessage());
        }
    }
    
    /**
     * Retorna a conexão com o banco de dados.
     * @return Connection com o banco de dados
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
            }
            return connection;
        } catch (SQLException e) {
            System.err.println("❌ Erro ao obter conexão: " + e.getMessage());
            // Tentar reconectar
            try {
                connection = DriverManager.getConnection(DB_URL);
                return connection;
            } catch (SQLException e2) {
                System.err.println("❌ Erro ao reconectar: " + e2.getMessage());
                return null;
            }
        }
    }
    
    /**
     * Fecha a conexão com o banco de dados.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔒 Conexão com banco de dados fechada");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao fechar conexão: " + e.getMessage());
        }
    }
} 