package core;

/**
 * Classe Kernel que atua como orquestradora do sistema Microkernel.
 * Responsável por inicializar e gerenciar o carregamento de plugins.
 */
public class Kernel {
    
    private final PluginLoader pluginLoader;
    
    /**
     * Construtor que inicializa o Kernel com um PluginLoader.
     */
    public Kernel() {
        this.pluginLoader = new PluginLoader();
    }
    
    /**
     * Inicializa o sistema Microkernel (apenas banco de dados).
     */
    public void initialize() {
        System.out.println("🚀 Inicializando Kernel do Sistema Microkernel...");
        
        // Inicializar banco de dados
        DatabaseManager dbManager = DatabaseManager.getInstance();
        dbManager.insertSampleData();
        
        System.out.println("📦 Plugins carregados dinamicamente...\n");
    }
    
    /**
     * Retorna o PluginLoader associado a este Kernel.
     * @return PluginLoader do sistema
     */
    public PluginLoader getPluginLoader() {
        return pluginLoader;
    }
    
    /**
     * Verifica se há plugins disponíveis no sistema.
     * @return true se há plugins disponíveis, false caso contrário
     */
    public boolean hasPlugins() {
        return pluginLoader.getPluginCount() > 0;
    }
} 