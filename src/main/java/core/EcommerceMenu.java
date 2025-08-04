package core;

import java.sql.*;
import java.util.*;
import java.util.Scanner;

public class EcommerceMenu {
    private final Scanner scanner;
    private final DatabaseManager dbManager;
    private Map<Integer, Integer> carrinho; // productId -> quantidade
    private Integer usuarioAtual;

    public EcommerceMenu() {
        this.scanner = new Scanner(System.in);
        this.dbManager = DatabaseManager.getInstance();
        this.carrinho = new HashMap<>();
        this.usuarioAtual = null;
    }

    public void iniciar() {
        while (true) {
            if (usuarioAtual == null) {
                mostrarMenuLogin();
            } else {
                mostrarMenuPrincipal();
            }
        }
    }

    private void mostrarMenuLogin() {
        System.out.println("\n🛒 ========================================");
        System.out.println("    SISTEMA E-COMMERCE MICROKERNEL");
        System.out.println("========================================");
        System.out.println("1. 🔐 Fazer Login");
        System.out.println("2. 📝 Cadastrar Novo Usuário");
        System.out.println("3. 🚪 Sair");
        System.out.println("========================================");
        System.out.print("Escolha uma opção: ");

        int opcao = scanner.nextInt();
        scanner.nextLine(); // Consumir quebra de linha

        switch (opcao) {
            case 1:
                fazerLogin();
                break;
            case 2:
                cadastrarUsuario();
                break;
            case 3:
                System.out.println("👋 Obrigado por usar nosso e-commerce!");
                System.exit(0);
            default:
                System.out.println("❌ Opção inválida!");
        }
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n🛒 ========================================");
        System.out.println("    BEM-VINDO AO E-COMMERCE!");
        System.out.println("========================================");
        System.out.println("1. 🛍️ Ver Produtos");
        System.out.println("2. 🛒 Ver Carrinho");
        System.out.println("3. 📋 Meus Pedidos");
        System.out.println("4. 👤 Meu Perfil");
        System.out.println("5. 🔐 Sair");
        System.out.println("========================================");
        System.out.print("Escolha uma opção: ");

        int opcao = scanner.nextInt();
        scanner.nextLine(); // Consumir quebra de linha

        switch (opcao) {
            case 1:
                mostrarProdutos();
                break;
            case 2:
                mostrarCarrinho();
                break;
            case 3:
                mostrarMeusPedidos();
                break;
            case 4:
                mostrarMeuPerfil();
                break;
            case 5:
                fazerLogout();
                break;
            default:
                System.out.println("❌ Opção inválida!");
        }
    }

    private void fazerLogin() {
        System.out.println("\n🔐 ========================================");
        System.out.println("              FAZER LOGIN");
        System.out.println("========================================");
        
        System.out.print("📧 Email: ");
        String email = scanner.nextLine();
        
        System.out.print("🔑 Senha: ");
        String senha = scanner.nextLine();

        try (Connection conn = dbManager.getConnection()) {
            String sql = "SELECT id, name FROM users WHERE email = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, email);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        usuarioAtual = rs.getInt("id");
                        String nome = rs.getString("name");
                        System.out.println("✅ Login realizado com sucesso!");
                        System.out.println("👋 Bem-vindo, " + nome + "!");
                    } else {
                        System.out.println("❌ Usuário não encontrado!");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao fazer login: " + e.getMessage());
        }
    }

    private void cadastrarUsuario() {
        System.out.println("\n📝 ========================================");
        System.out.println("           CADASTRAR USUÁRIO");
        System.out.println("========================================");
        
        System.out.print("👤 Nome completo: ");
        String nome = scanner.nextLine();
        
        System.out.print("📧 Email: ");
        String email = scanner.nextLine();
        
        System.out.print("🔑 Senha: ");
        String senha = scanner.nextLine();

        try (Connection conn = dbManager.getConnection()) {
            String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nome);
                pstmt.setString(2, email);
                int rowsAffected = pstmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    System.out.println("✅ Usuário cadastrado com sucesso!");
                    System.out.println("🔐 Agora você pode fazer login!");
                } else {
                    System.out.println("❌ Erro ao cadastrar usuário!");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao cadastrar: " + e.getMessage());
        }
    }

    private void mostrarProdutos() {
        System.out.println("\n🛍️ ========================================");
        System.out.println("              PRODUTOS");
        System.out.println("========================================");

        try (Connection conn = dbManager.getConnection()) {
            String sql = "SELECT id, name, description, price, stock FROM products WHERE stock > 0 ORDER BY name";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String nome = rs.getString("name");
                        String descricao = rs.getString("description");
                        double preco = rs.getDouble("price");
                        int estoque = rs.getInt("stock");

                        System.out.println("📦 ID: " + id);
                        System.out.println("   Nome: " + nome);
                        System.out.println("   Descrição: " + descricao);
                        System.out.println("   Preço: R$ " + String.format("%.2f", preco));
                        System.out.println("   Estoque: " + estoque + " unidades");
                        System.out.println("   ----------------------------------------");
                    }
                }
            }

            System.out.println("\n🛒 ========================================");
            System.out.println("1. ➕ Adicionar ao Carrinho");
            System.out.println("2. 🔙 Voltar");
            System.out.println("========================================");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            if (opcao == 1) {
                adicionarAoCarrinho();
            }

        } catch (SQLException e) {
            System.err.println("❌ Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private void adicionarAoCarrinho() {
        System.out.print("📦 Digite o ID do produto: ");
        int productId = scanner.nextInt();
        
        System.out.print("📊 Quantidade: ");
        int quantidade = scanner.nextInt();

        try (Connection conn = dbManager.getConnection()) {
            // Verificar se produto existe e tem estoque
            String checkSql = "SELECT name, price, stock FROM products WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
                pstmt.setInt(1, productId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String nome = rs.getString("name");
                        double preco = rs.getDouble("price");
                        int estoque = rs.getInt("stock");

                        if (quantidade <= estoque) {
                            carrinho.put(productId, carrinho.getOrDefault(productId, 0) + quantidade);
                            System.out.println("✅ " + quantidade + "x " + nome + " adicionado ao carrinho!");
                        } else {
                            System.out.println("❌ Quantidade indisponível! Estoque: " + estoque);
                        }
                    } else {
                        System.out.println("❌ Produto não encontrado!");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao adicionar ao carrinho: " + e.getMessage());
        }
    }

    private void mostrarCarrinho() {
        if (carrinho.isEmpty()) {
            System.out.println("\n🛒 ========================================");
            System.out.println("              CARRINHO VAZIO");
            System.out.println("========================================");
            System.out.println("📦 Adicione produtos ao seu carrinho!");
            return;
        }

        System.out.println("\n🛒 ========================================");
        System.out.println("              SEU CARRINHO");
        System.out.println("========================================");

        double total = 0.0;
        try (Connection conn = dbManager.getConnection()) {
            for (Map.Entry<Integer, Integer> item : carrinho.entrySet()) {
                int productId = item.getKey();
                int quantidade = item.getValue();

                String sql = "SELECT name, price FROM products WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, productId);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            String nome = rs.getString("name");
                            double preco = rs.getDouble("price");
                            double subtotal = preco * quantidade;
                            total += subtotal;

                            System.out.println("📦 " + nome);
                            System.out.println("   Quantidade: " + quantidade);
                            System.out.println("   Preço unitário: R$ " + String.format("%.2f", preco));
                            System.out.println("   Subtotal: R$ " + String.format("%.2f", subtotal));
                            System.out.println("   ----------------------------------------");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao carregar carrinho: " + e.getMessage());
            return;
        }

        System.out.println("💰 TOTAL: R$ " + String.format("%.2f", total));
        System.out.println("========================================");
        System.out.println("1. 🛒 Finalizar Compra");
        System.out.println("2. 🗑️ Limpar Carrinho");
        System.out.println("3. 🔙 Voltar");
        System.out.println("========================================");
        System.out.print("Escolha uma opção: ");

        int opcao = scanner.nextInt();
        switch (opcao) {
            case 1:
                finalizarCompra();
                break;
            case 2:
                carrinho.clear();
                System.out.println("🗑️ Carrinho limpo!");
                break;
        }
    }

    private void finalizarCompra() {
        if (carrinho.isEmpty()) {
            System.out.println("❌ Carrinho vazio!");
            return;
        }

        System.out.println("\n💳 ========================================");
        System.out.println("           FINALIZAR COMPRA");
        System.out.println("========================================");

        // Calcular total
        double total = 0.0;
        try (Connection conn = dbManager.getConnection()) {
            for (Map.Entry<Integer, Integer> item : carrinho.entrySet()) {
                int productId = item.getKey();
                int quantidade = item.getValue();

                String sql = "SELECT price FROM products WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, productId);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            double preco = rs.getDouble("price");
                            total += preco * quantidade;
                        }
                    }
                }
            }

            // Criar pedido
            String orderSql = "INSERT INTO orders (user_id, total_amount, status) VALUES (?, ?, 'PENDING')";
            int orderId = 0;
            try (PreparedStatement pstmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, usuarioAtual);
                pstmt.setDouble(2, total);
                pstmt.executeUpdate();

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                    }
                }
            }

            // Adicionar produtos ao pedido
            String orderProductSql = "INSERT INTO order_products (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
            for (Map.Entry<Integer, Integer> item : carrinho.entrySet()) {
                int productId = item.getKey();
                int quantidade = item.getValue();

                // Obter preço do produto
                String priceSql = "SELECT price FROM products WHERE id = ?";
                try (PreparedStatement priceStmt = conn.prepareStatement(priceSql)) {
                    priceStmt.setInt(1, productId);
                    try (ResultSet rs = priceStmt.executeQuery()) {
                        if (rs.next()) {
                            double preco = rs.getDouble("price");

                            // Inserir produto no pedido
                            try (PreparedStatement opStmt = conn.prepareStatement(orderProductSql)) {
                                opStmt.setInt(1, orderId);
                                opStmt.setInt(2, productId);
                                opStmt.setInt(3, quantidade);
                                opStmt.setDouble(4, preco);
                                opStmt.executeUpdate();
                            }

                            // Atualizar estoque
                            String updateStockSql = "UPDATE products SET stock = stock - ? WHERE id = ?";
                            try (PreparedStatement stockStmt = conn.prepareStatement(updateStockSql)) {
                                stockStmt.setInt(1, quantidade);
                                stockStmt.setInt(2, productId);
                                stockStmt.executeUpdate();
                            }
                        }
                    }
                }
            }

            // Criar pagamento
            String paymentSql = "INSERT INTO payments (order_id, amount, payment_method, status) VALUES (?, ?, 'CREDIT_CARD', 'APPROVED')";
            try (PreparedStatement pstmt = conn.prepareStatement(paymentSql)) {
                pstmt.setInt(1, orderId);
                pstmt.setDouble(2, total);
                pstmt.executeUpdate();
            }

            // Atualizar status do pedido
            String updateOrderSql = "UPDATE orders SET status = 'PAID' WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateOrderSql)) {
                pstmt.setInt(1, orderId);
                pstmt.executeUpdate();
            }

            System.out.println("✅ Pedido #" + orderId + " criado com sucesso!");
            System.out.println("💰 Total: R$ " + String.format("%.2f", total));
            System.out.println("💳 Pagamento aprovado!");
            
            carrinho.clear();

        } catch (SQLException e) {
            System.err.println("❌ Erro ao finalizar compra: " + e.getMessage());
        }
    }

    private void mostrarMeusPedidos() {
        System.out.println("\n📋 ========================================");
        System.out.println("              MEUS PEDIDOS");
        System.out.println("========================================");

        try (Connection conn = dbManager.getConnection()) {
            String sql = "SELECT o.id, o.total_amount, o.status, o.created_at FROM orders o WHERE o.user_id = ? ORDER BY o.created_at DESC";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, usuarioAtual);
                try (ResultSet rs = pstmt.executeQuery()) {
                    boolean temPedidos = false;
                    while (rs.next()) {
                        temPedidos = true;
                        int orderId = rs.getInt("id");
                        double total = rs.getDouble("total_amount");
                        String status = rs.getString("status");
                        String data = rs.getString("created_at");

                        System.out.println("📦 Pedido #" + orderId);
                        System.out.println("   Total: R$ " + String.format("%.2f", total));
                        System.out.println("   Status: " + status);
                        System.out.println("   Data: " + data);
                        System.out.println("   ----------------------------------------");
                    }

                    if (!temPedidos) {
                        System.out.println("📦 Você ainda não fez nenhum pedido!");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao carregar pedidos: " + e.getMessage());
        }
    }

    private void mostrarMeuPerfil() {
        System.out.println("\n👤 ========================================");
        System.out.println("              MEU PERFIL");
        System.out.println("========================================");

        try (Connection conn = dbManager.getConnection()) {
            String sql = "SELECT name, email, created_at FROM users WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, usuarioAtual);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String nome = rs.getString("name");
                        String email = rs.getString("email");
                        String data = rs.getString("created_at");

                        System.out.println("👤 Nome: " + nome);
                        System.out.println("📧 Email: " + email);
                        System.out.println("📅 Cadastro: " + data);
                        System.out.println("========================================");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao carregar perfil: " + e.getMessage());
        }
    }

    private void fazerLogout() {
        usuarioAtual = null;
        carrinho.clear();
        System.out.println("👋 Logout realizado com sucesso!");
    }

    public void close() {
        scanner.close();
    }
} 