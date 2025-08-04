package plugins;

import core.Plugin;
import core.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Plugin responsável pelo domínio de Pagamentos.
 * Simula funcionalidades de processamento de pagamentos.
 */
public class PaymentPlugin implements Plugin {
    
    @Override
    public String getName() {
        return "Payment Plugin - Processamento de Pagamentos";
    }
    
    @Override
    public void execute() {
        System.out.println("Processando pagamento...");
        
        try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            Connection conn = dbManager.getConnection();
            
            // Processar pagamento para o pedido mais recente
            String selectOrderSQL = "SELECT id, total_amount FROM orders WHERE status = 'PENDING' ORDER BY id DESC LIMIT 1";
            int orderId = 0;
            double amount = 0;
            
            try (PreparedStatement pstmt = conn.prepareStatement(selectOrderSQL);
                 ResultSet rs = pstmt.executeQuery()) {
                
                if (rs.next()) {
                    orderId = rs.getInt("id");
                    amount = rs.getDouble("total_amount");
                    
                    // Criar registro de pagamento
                    String insertPaymentSQL = "INSERT INTO payments (order_id, amount, payment_method, status) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement paymentStmt = conn.prepareStatement(insertPaymentSQL)) {
                        paymentStmt.setInt(1, orderId);
                        paymentStmt.setDouble(2, amount);
                        paymentStmt.setString(3, "CREDIT_CARD");
                        paymentStmt.setString(4, "APPROVED");
                        paymentStmt.executeUpdate();
                        
                        System.out.println("  → Pagamento processado para pedido #" + orderId);
                        System.out.println("  → Valor: R$ " + String.format("%.2f", amount));
                        System.out.println("  → Método: Cartão de Crédito");
                        System.out.println("  → Status: Aprovado");
                    }
                    
                    // Atualizar status do pedido
                    String updateOrderSQL = "UPDATE orders SET status = 'PAID' WHERE id = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateOrderSQL)) {
                        updateStmt.setInt(1, orderId);
                        updateStmt.executeUpdate();
                        System.out.println("  → Status do pedido #" + orderId + " atualizado para PAID");
                    }
                }
            }
            
            // Listar pagamentos recentes
            System.out.println("  → Pagamentos recentes:");
            String selectPaymentsSQL = "SELECT p.id, p.order_id, p.amount, p.payment_method, p.status, p.created_at, " +
                "u.name as customer_name " +
                "FROM payments p " +
                "JOIN orders o ON p.order_id = o.id " +
                "JOIN users u ON o.user_id = u.id " +
                "ORDER BY p.id DESC LIMIT 3";
            
            try (PreparedStatement pstmt = conn.prepareStatement(selectPaymentsSQL);
                 ResultSet rs = pstmt.executeQuery()) {
                
                while (rs.next()) {
                    int paymentId = rs.getInt("id");
                    int orderIdPayment = rs.getInt("order_id");
                    double paymentAmount = rs.getDouble("amount");
                    String paymentMethod = rs.getString("payment_method");
                    String paymentStatus = rs.getString("status");
                    String customerName = rs.getString("customer_name");
                    
                    System.out.println("    - Pagamento #" + paymentId + ", Pedido #" + orderIdPayment);
                    System.out.println("      Cliente: " + customerName);
                    System.out.println("      Valor: R$ " + String.format("%.2f", paymentAmount));
                    System.out.println("      Método: " + paymentMethod);
                    System.out.println("      Status: " + paymentStatus);
                    System.out.println();
                }
            }
            
        } catch (SQLException e) {
            System.err.println("  ❌ Erro ao executar operações de pagamento: " + e.getMessage());
        }
    }
} 