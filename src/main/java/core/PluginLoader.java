package core;

import java.util.ServiceLoader;

/**
 * Classe responsável por carregar e executar plugins dinamicamente
 * utilizando o ServiceLoader do Java.
 */
public class PluginLoader {
    
    private final ServiceLoader<Plugin> serviceLoader;
    
    /**
     * Construtor que inicializa o ServiceLoader para carregar plugins.
     */
    public PluginLoader() {
        this.serviceLoader = ServiceLoader.load(Plugin.class);
    }
    
    /**
     * Carrega e executa todos os plugins disponíveis.
     * Cada plugin é executado e sua ação é exibida no console.
     */
    public void loadAndExecutePlugins() {
        System.out.println("=== Sistema Microkernel Ecommerce ===");
        System.out.println("Carregando plugins dinamicamente...\n");
        
        int pluginCount = 0;
        
        for (Plugin plugin : serviceLoader) {
            pluginCount++;
            System.out.println("Plugin #" + pluginCount + ": " + plugin.getName());
            System.out.print("Ação: ");
            plugin.execute();
            System.out.println();
        }
        
        if (pluginCount == 0) {
            System.out.println("Nenhum plugin encontrado!");
        } else {
            System.out.println("Total de plugins carregados: " + pluginCount);
        }
        
        System.out.println("=== Execução concluída ===");
    }
    
    /**
     * Retorna o número de plugins disponíveis.
     * @return Número de plugins carregados
     */
    public int getPluginCount() {
        int count = 0;
        for (Plugin plugin : serviceLoader) {
            count++;
        }
        return count;
    }
    
    /**
     * Retorna o ServiceLoader para acesso direto aos plugins.
     * @return ServiceLoader dos plugins
     */
    public ServiceLoader<Plugin> getServiceLoader() {
        return serviceLoader;
    }
} 