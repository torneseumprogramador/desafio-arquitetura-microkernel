package core;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Menu interativo para o sistema Microkernel Ecommerce.
 * Permite ao usuário escolher qual ação executar.
 */
public class InteractiveMenu {
    
    private final PluginLoader pluginLoader;
    private final Scanner scanner;
    private final List<Plugin> availablePlugins;
    
    public InteractiveMenu() {
        this.pluginLoader = new PluginLoader();
        this.scanner = new Scanner(System.in);
        this.availablePlugins = new ArrayList<>();
        loadPlugins();
    }
    
    /**
     * Carrega todos os plugins disponíveis.
     */
    private void loadPlugins() {
        for (Plugin plugin : pluginLoader.getServiceLoader()) {
            availablePlugins.add(plugin);
        }
    }
    
    /**
     * Exibe o menu principal e processa a escolha do usuário.
     */
    public void showMenu() {
        while (true) {
            displayMainMenu();
            int choice = getUserChoice();
            
            switch (choice) {
                case 1:
                    executeAllPlugins();
                    break;
                case 2:
                    showPluginMenu();
                    break;
                case 3:
                    showDatabaseInfo();
                    break;
                case 4:
                    System.out.println("👋 Obrigado por usar o Sistema Microkernel Ecommerce!");
                    return;
                default:
                    System.out.println("❌ Opção inválida. Tente novamente.");
            }
            
            System.out.println("\n" + "=".repeat(50) + "\n");
        }
    }
    
    /**
     * Exibe o menu principal.
     */
    private void displayMainMenu() {
        System.out.println("🛒 Sistema Microkernel Ecommerce");
        System.out.println("=".repeat(40));
        System.out.println("📋 Menu Principal:");
        System.out.println("1. 🚀 Executar todos os plugins");
        System.out.println("2. 🔧 Executar plugin específico");
        System.out.println("3. 📊 Informações do banco de dados");
        System.out.println("4. 🚪 Sair");
        System.out.println("=".repeat(40));
        System.out.print("Escolha uma opção: ");
    }
    
    /**
     * Obtém a escolha do usuário.
     */
    private int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("❌ Por favor, digite um número válido.");
            scanner.next();
        }
        return scanner.nextInt();
    }
    
    /**
     * Executa todos os plugins disponíveis.
     */
    private void executeAllPlugins() {
        System.out.println("\n🚀 Executando todos os plugins...\n");
        pluginLoader.loadAndExecutePlugins();
    }
    
    /**
     * Exibe menu de plugins específicos.
     */
    private void showPluginMenu() {
        while (true) {
            System.out.println("\n🔧 Plugins Disponíveis:");
            System.out.println("-".repeat(30));
            
            for (int i = 0; i < availablePlugins.size(); i++) {
                Plugin plugin = availablePlugins.get(i);
                System.out.println((i + 1) + ". " + plugin.getName());
            }
            System.out.println("0. ⬅️ Voltar ao menu principal");
            System.out.println("-".repeat(30));
            System.out.print("Escolha um plugin: ");
            
            int choice = getUserChoice();
            
            if (choice == 0) {
                break;
            } else if (choice >= 1 && choice <= availablePlugins.size()) {
                executeSpecificPlugin(choice - 1);
            } else {
                System.out.println("❌ Opção inválida. Tente novamente.");
            }
        }
    }
    
    /**
     * Executa um plugin específico.
     */
    private void executeSpecificPlugin(int pluginIndex) {
        Plugin plugin = availablePlugins.get(pluginIndex);
        System.out.println("\n🔧 Executando: " + plugin.getName());
        System.out.println("-".repeat(50));
        
        try {
            plugin.execute();
        } catch (Exception e) {
            System.err.println("❌ Erro ao executar plugin: " + e.getMessage());
        }
        
        System.out.println("-".repeat(50));
    }
    
    /**
     * Exibe informações do banco de dados.
     */
    private void showDatabaseInfo() {
        System.out.println("\n📊 Informações do Banco de Dados");
        System.out.println("=".repeat(40));
        
        try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            
            // Contar registros em cada tabela
            countRecords("users", "Usuários");
            countRecords("products", "Produtos");
            countRecords("orders", "Pedidos");
            countRecords("order_products", "Produtos em Pedidos");
            countRecords("payments", "Pagamentos");
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao acessar banco de dados: " + e.getMessage());
        }
        
        System.out.println("=".repeat(40));
    }
    
    /**
     * Conta registros em uma tabela específica.
     */
    private void countRecords(String tableName, String displayName) {
        try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            var conn = dbManager.getConnection();
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName);
            
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("📋 " + displayName + ": " + count + " registros");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao contar registros em " + tableName + ": " + e.getMessage());
        }
    }
    
    /**
     * Fecha o scanner.
     */
    public void close() {
        scanner.close();
    }
} 