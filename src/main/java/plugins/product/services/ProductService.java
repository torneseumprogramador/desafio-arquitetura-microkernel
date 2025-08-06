package plugins.product.services;

import plugins.product.entities.Product;
import plugins.product.repositories.ProductRepository;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service para lógica de negócio de produtos.
 * Pertence ao plugin de gerenciamento de produtos.
 */
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Registra um novo produto.
     */
    public Product registerProduct(String name, String description, BigDecimal price, Integer stock) throws SQLException {
        // Validar dados
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("Estoque deve ser maior ou igual a zero");
        }

        // Criar e salvar produto
        Product product = new Product(name.trim(), description, price, stock);
        return productRepository.save(product);
    }

    /**
     * Busca produto por ID.
     */
    public Optional<Product> findProductById(Integer id) throws SQLException {
        if (id == null) {
            return Optional.empty();
        }

        return productRepository.findById(id);
    }

    /**
     * Lista todos os produtos com estoque.
     */
    public List<Product> getAvailableProducts() throws SQLException {
        return productRepository.findAllWithStock();
    }

    /**
     * Lista todos os produtos.
     */
    public List<Product> getAllProducts() throws SQLException {
        return productRepository.findAll();
    }

    /**
     * Verifica se um produto tem estoque suficiente.
     */
    public boolean hasStock(Integer productId, Integer quantity) throws SQLException {
        if (productId == null || quantity == null || quantity <= 0) {
            return false;
        }

        return productRepository.hasStock(productId, quantity);
    }

    /**
     * Atualiza o estoque de um produto.
     */
    public void updateStock(Integer productId, Integer newStock) throws SQLException {
        if (productId == null || newStock == null || newStock < 0) {
            throw new IllegalArgumentException("Dados inválidos para atualização de estoque");
        }

        productRepository.updateStock(productId, newStock);
    }

    /**
     * Reduz o estoque de um produto.
     */
    public void reduceStock(Integer productId, Integer quantity) throws SQLException {
        if (productId == null || quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantidade inválida");
        }

        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            int newStock = product.getStock() - quantity;
            if (newStock < 0) {
                throw new IllegalArgumentException("Estoque insuficiente");
            }
            productRepository.updateStock(productId, newStock);
        } else {
            throw new IllegalArgumentException("Produto não encontrado");
        }
    }
} 