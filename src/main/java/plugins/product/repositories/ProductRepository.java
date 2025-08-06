package plugins.product.repositories;

import plugins.product.entities.Product;
import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository para acesso aos dados de produtos.
 * Pertence ao plugin de gerenciamento de produtos.
 */
public class ProductRepository {
    private final Connection connection;

    public ProductRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Salva um novo produto.
     */
    public Product save(Product product) throws SQLException {
        String sql = "INSERT INTO products (name, description, price, stock) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setBigDecimal(3, product.getPrice());
            pstmt.setInt(4, product.getStock());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    product.setId(rs.getInt(1));
                }
            }
        }
        return product;
    }

    /**
     * Busca produto por ID.
     */
    public Optional<Product> findById(Integer id) throws SQLException {
        String sql = "SELECT id, name, description, price, stock, created_at FROM products WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getBigDecimal("price"));
                    product.setStock(rs.getInt("stock"));
                    product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return Optional.of(product);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Lista todos os produtos com estoque.
     */
    public List<Product> findAllWithStock() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, name, description, price, stock, created_at FROM products WHERE stock > 0 ORDER BY name";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getBigDecimal("price"));
                    product.setStock(rs.getInt("stock"));
                    product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    products.add(product);
                }
            }
        }
        return products;
    }

    /**
     * Lista todos os produtos.
     */
    public List<Product> findAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, name, description, price, stock, created_at FROM products ORDER BY name";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getBigDecimal("price"));
                    product.setStock(rs.getInt("stock"));
                    product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    products.add(product);
                }
            }
        }
        return products;
    }

    /**
     * Atualiza o estoque de um produto.
     */
    public void updateStock(Integer productId, Integer newStock) throws SQLException {
        String sql = "UPDATE products SET stock = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, newStock);
            pstmt.setInt(2, productId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Verifica se um produto tem estoque suficiente.
     */
    public boolean hasStock(Integer productId, Integer quantity) throws SQLException {
        String sql = "SELECT stock FROM products WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int stock = rs.getInt("stock");
                    return stock >= quantity;
                }
            }
        }
        return false;
    }
} 