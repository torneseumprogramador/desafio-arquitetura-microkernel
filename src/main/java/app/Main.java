package app;

import core.Kernel;
import core.EcommerceMenu;

/**
 * Classe principal da aplicação Microkernel Ecommerce.
 * Ponto de entrada que inicializa o sistema e apresenta menu interativo.
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("🛒 Sistema Microkernel Ecommerce");
        System.out.println("================================\n");
        
        try {
            // Cria e inicializa o Kernel do sistema
            Kernel kernel = new Kernel();
            
            // Inicializa o sistema (banco de dados)
            kernel.initialize();
            
            // Apresenta menu de e-commerce
            EcommerceMenu ecommerce = new EcommerceMenu();
            ecommerce.iniciar();
            ecommerce.close();
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao inicializar o sistema: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
} 