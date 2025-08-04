package app;

import core.Kernel;
import core.DatabaseManager;

/**
 * Classe para execução automática de todos os plugins.
 * Executa todos os plugins sem menu interativo.
 */
public class AutoMode {
    
    public static void main(String[] args) {
        System.out.println("🛒 Sistema Microkernel Ecommerce - Modo Automático");
        System.out.println("==================================================\n");
        
        try {
            // Cria e inicializa o Kernel do sistema
            Kernel kernel = new Kernel();
            
            // Inicializa o sistema (banco de dados)
            kernel.initialize();
            
            // Executa todos os plugins automaticamente
            System.out.println("🚀 Executando todos os plugins automaticamente...\n");
            kernel.getPluginLoader().loadAndExecutePlugins();
            
            // Fechar conexão com banco de dados
            DatabaseManager.getInstance().closeConnection();
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao executar em modo automático: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
} 