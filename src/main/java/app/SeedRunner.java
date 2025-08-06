package app;

import core.SeedManager;

/**
 * Classe para executar o seed do banco de dados via linha de comando.
 * Permite popular o banco com dados iniciais de forma controlada.
 */
public class SeedRunner {
    
    public static void main(String[] args) {
        System.out.println("🌱 Sistema de Seed - Microkernel Ecommerce");
        System.out.println("==========================================\n");
        
        try {
            SeedManager seedManager = new SeedManager();
            
            String mode = args.length > 0 ? args[0] : "normal";
            
            switch (mode) {
                case "normal":
                    System.out.println("📝 Executando seed normal...");
                    seedManager.executeSeed(false);
                    break;
                    
                case "force":
                    System.out.println("🔄 Executando seed forçado...");
                    seedManager.executeSeed(true);
                    break;
                    
                case "check":
                    System.out.println("🔍 Verificando dados existentes...");
                    if (seedManager.hasData()) {
                        System.out.println("✅ Banco de dados já possui dados");
                        System.out.println("💡 Use 'force' para limpar e recriar dados");
                    } else {
                        System.out.println("📭 Banco de dados está vazio");
                        System.out.println("💡 Execute o seed para inserir dados iniciais");
                    }
                    break;
                    
                default:
                    System.out.println("❌ Modo não reconhecido: " + mode);
                    System.out.println("📋 Modos disponíveis:");
                    System.out.println("   normal - Executa seed normal");
                    System.out.println("   force  - Força seed (limpa dados)");
                    System.out.println("   check  - Verifica dados existentes");
                    System.exit(1);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao executar seed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
} 