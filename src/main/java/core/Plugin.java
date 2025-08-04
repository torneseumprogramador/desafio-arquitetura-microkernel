package core;

/**
 * Interface base para todos os plugins do sistema Microkernel.
 * Define o contrato comum que todos os plugins devem implementar.
 */
public interface Plugin {
    
    /**
     * Retorna o nome do plugin.
     * @return Nome do plugin
     */
    String getName();
    
    /**
     * Executa a funcionalidade específica do plugin.
     * Cada plugin implementa sua própria lógica de negócio.
     */
    void execute();
} 