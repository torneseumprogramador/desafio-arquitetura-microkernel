# 🛒 Sistema Microkernel Ecommerce

Um sistema de ecommerce baseado na **Arquitetura Microkernel (Plugin-Based)** desenvolvido em Java com Maven.

## 📋 Sobre a Arquitetura Microkernel

A **Arquitetura Microkernel** é um padrão arquitetural que separa a funcionalidade básica (core) da aplicação das funcionalidades opcionais (plugins). Os princípios fundamentais são:

### 🎯 Características Principais

- **Núcleo Mínimo**: O kernel fornece apenas funcionalidades essenciais
- **Extensibilidade**: Novas funcionalidades são adicionadas via plugins
- **Baixo Acoplamento**: Plugins são independentes entre si
- **Modularidade**: Cada plugin pode ser adicionado/removido sem afetar o sistema
- **Descoberta Automática**: Plugins são carregados dinamicamente

### 🏗️ Estrutura do Projeto

```
microkernel-ecommerce/
├── core/                          # Núcleo do sistema
│   ├── Plugin.java               # Interface base dos plugins
│   ├── PluginLoader.java         # Carregador de plugins via ServiceLoader
│   ├── Kernel.java               # Orquestrador do sistema
│   ├── DatabaseManager.java      # Gerenciador do banco SQLite
│   └── InteractiveMenu.java      # Menu interativo da aplicação
├── app/
│   ├── Main.java                 # Ponto de entrada com menu interativo
│   └── AutoMode.java             # Modo automático (sem menu)
├── plugins/                       # Plugins de domínio
│   ├── UserPlugin.java           # Gerenciamento de usuários
│   ├── ProductPlugin.java        # Gerenciamento de produtos
│   ├── OrderPlugin.java          # Gerenciamento de pedidos
│   ├── OrderProductPlugin.java   # Produtos do pedido
│   └── PaymentPlugin.java        # Processamento de pagamentos
├── resources/
│   └── META-INF/services/        # Configuração do ServiceLoader
│       └── core.Plugin           # Lista de plugins registrados
├── run.sh                        # Script de execução
├── push.sh                       # Script de automação Git
├── .gitignore                    # Arquivos ignorados pelo Git
├── pom.xml                       # Configuração Maven
└── microkernel_ecommerce.db      # Banco de dados SQLite (criado automaticamente)
```

## 🚀 Como Executar

### Pré-requisitos

- Java 11 ou superior
- Maven 3.6 ou superior

### 📜 Script de Execução

O projeto inclui um script `run.sh` que facilita a execução da aplicação:

- **Verificação automática** de dependências (Java e Maven)
- **Múltiplas opções** de execução (Maven, JAR, Auto)
- **Mensagens coloridas** para melhor experiência
- **Tratamento de erros** e validações

### 🎮 Menu Interativo

O sistema agora possui um menu interativo que permite:

- **🚀 Executar todos os plugins**: Executa todos os plugins de uma vez
- **🔧 Executar plugin específico**: Escolhe qual plugin executar
- **📊 Informações do banco**: Mostra estatísticas do banco de dados
- **🚪 Sair**: Encerra a aplicação

### 🚀 Script de Automação Git

O projeto inclui um script `push.sh` para facilitar o processo de commit e push:

```bash
# Push com mensagem interativa
./push.sh

# Push com mensagem fornecida
./push.sh "Adiciona menu interativo"

# Ver ajuda
./push.sh help
```

#### Funcionalidades do Script:
- **✅ Verificações**: Git instalado e repositório válido
- **📊 Status**: Mostra alterações antes do commit
- **💾 Commit**: Commit automático com mensagem personalizada
- **⬇️ Pull**: Atualiza repositório local
- **⬆️ Push**: Envia alterações para o repositório remoto
- **🎨 Interface**: Mensagens coloridas e feedback visual

### 📋 Arquivos Ignorados (.gitignore)

O projeto inclui um `.gitignore` completo que ignora:

#### **💾 Banco de Dados**
- `*.db`, `*.sqlite`, `*.sqlite3` - Arquivos de banco SQLite
- `microkernel_ecommerce.db` - Banco específico do projeto

#### **🖥️ Sistema Operacional**
- **macOS**: `.DS_Store`, `.AppleDouble`, `.Trashes`
- **Windows**: `Thumbs.db`, `Desktop.ini`, `*.tmp`
- **Linux**: `*~`, `.directory`, `.Trash-*`

#### **🏗️ Maven/Java**
- `target/` - Diretório de compilação
- `*.class`, `*.jar` - Arquivos compilados
- `*.log` - Logs de aplicação

#### **🔧 IDEs e Editores**
- `.idea/` (IntelliJ), `.vscode/` (VS Code)
- `.project`, `.classpath` (Eclipse)
- `*.swp`, `*.swo` (Vim)

#### **📝 Logs**
- `*.log` - Todos os arquivos de log
- `logs/`, `log/` - Diretórios de log

### Compilação e Execução

#### 🚀 Usando o Script de Execução (Recomendado)

```bash
# Executar via Maven com menu interativo (padrão)
./run.sh

# Executar em modo automático (sem menu)
./run.sh auto

# Executar via JAR
./run.sh jar

# Gerar JAR executável
./run.sh build

# Limpar projeto
./run.sh clean

# Ver ajuda
./run.sh help
```

#### 🔧 Usando Maven Diretamente

```bash
# Compilar o projeto
mvn clean compile

# Executar via Maven
mvn exec:java -Dexec.mainClass="app.Main"

# Ou gerar JAR executável
mvn clean package

# Executar o JAR gerado
java -jar target/microkernel-ecommerce-1.0.0.jar
```

### 🛒 Menu de E-commerce

O sistema agora oferece um **menu completo de e-commerce** com as seguintes funcionalidades:

#### **🔐 Sistema de Login**
- **Fazer Login**: Acesso com email e senha
- **Cadastrar Novo Usuário**: Registro de novos clientes

#### **🛍️ Área do Cliente**
- **Ver Produtos**: Catálogo completo com preços e estoque
- **Ver Carrinho**: Gerenciar produtos no carrinho de compras
- **Meus Pedidos**: Histórico de pedidos realizados
- **Meu Perfil**: Informações pessoais do cliente

#### **💳 Processo de Compra**
- **Adicionar ao Carrinho**: Selecionar produtos e quantidades
- **Finalizar Compra**: Processar pagamento e criar pedido
- **Controle de Estoque**: Atualização automática do estoque
- **Histórico de Pedidos**: Acompanhamento de compras anteriores

### Saída Esperada

```
🛒 Sistema Microkernel Ecommerce
================================

🚀 Inicializando Kernel do Sistema Microkernel...
📊 Conectado ao banco de dados SQLite
📋 Tabelas criadas/verificadas com sucesso
📝 Dados de exemplo inseridos com sucesso

🛒 ========================================
    SISTEMA E-COMMERCE MICROKERNEL
========================================
1. 🔐 Fazer Login
2. 📝 Cadastrar Novo Usuário
3. 🚪 Sair
========================================
Escolha uma opção: 2

📝 ========================================
           CADASTRAR USUÁRIO
========================================
👤 Nome completo: João Silva
📧 Email: joao@email.com
🔑 Senha: 123456
✅ Usuário cadastrado com sucesso!
🔐 Agora você pode fazer login!

🛒 ========================================
    BEM-VINDO AO E-COMMERCE!
========================================
1. 🛍️ Ver Produtos
2. 🛒 Ver Carrinho
3. 📋 Meus Pedidos
4. 👤 Meu Perfil
5. 🔐 Sair
========================================
Escolha uma opção: 1

🛍️ ========================================
              PRODUTOS
========================================
📦 ID: 1
   Nome: Notebook Dell
   Descrição: Notebook Dell Inspiron 15 polegadas
   Preço: R$ 2999.99
   Estoque: 10 unidades
   ----------------------------------------
📦 ID: 2
   Nome: Mouse Wireless
   Descrição: Mouse sem fio Logitech
   Preço: R$ 89.90
   Estoque: 50 unidades
   ----------------------------------------
      Estoque: 15 unidades

    - ID: 4, Nome: Monitor 24"
      Descrição: Monitor LED 24 polegadas
      Preço: R$ 599.99
      Estoque: 8 unidades

  → Estoque do Notebook Dell atualizado (1 unidade vendida)

Plugin #3: Order Plugin - Gerenciamento de Pedidos
Ação: Gerando pedido...
  → Pedido #1 criado com sucesso!
  → Produtos adicionados ao pedido #1
  → Pedidos recentes:
    - Pedido #1, Cliente: João Silva, Total: R$ 3089.89, Status: PENDING

Plugin #4: Order Product Plugin - Gerenciamento de Produtos do Pedido
Ação: Adicionando produto ao pedido...
  → Produtos no pedido mais recente:
    - Produto: Notebook Dell
      Quantidade: 1
      Preço unitário: R$ 2999.99
      Total: R$ 2999.99

    - Produto: Mouse Wireless
      Quantidade: 1
      Preço unitário: R$ 89.90
      Total: R$ 89.90

  → Total do pedido: R$ 3089.89
  → 2x Teclado Mecânico adicionado ao pedido #1

Plugin #5: Payment Plugin - Processamento de Pagamentos
Ação: Processando pagamento...
  → Pagamento processado para pedido #1
  → Valor: R$ 3089.89
  → Método: Cartão de Crédito
  → Status: Aprovado
  → Status do pedido #1 atualizado para PAID
  → Pagamentos recentes:
    - Pagamento #1, Pedido #1
      Cliente: João Silva
      Valor: R$ 3089.89
      Método: CREDIT_CARD
      Status: APPROVED

Total de plugins carregados: 5
=== Execução concluída ===
🔒 Conexão com banco de dados fechada
```

## 🔧 Como Adicionar Novos Plugins

### 1. Criar a Classe do Plugin

Crie uma nova classe que implemente a interface `Plugin`:

```java
package plugins;

import core.Plugin;

public class PaymentPlugin implements Plugin {
    
    @Override
    public String getName() {
        return "Payment Plugin - Processamento de Pagamentos";
    }
    
    @Override
    public void execute() {
        System.out.println("Processando pagamento...");
        System.out.println("  → Validando dados do cartão");
        System.out.println("  → Autorizando transação");
        System.out.println("  → Confirmando pagamento");
    }
}
```

### 2. Registrar o Plugin

Adicione a classe do plugin ao arquivo `src/main/resources/META-INF/services/core.Plugin`:

```
plugins.UserPlugin
plugins.ProductPlugin
plugins.OrderPlugin
plugins.OrderProductPlugin
plugins.PaymentPlugin  # Nova linha
```

### 3. Recompilar e Executar

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="app.Main"
```

## 🎯 Vantagens da Arquitetura Microkernel

### ✅ Benefícios

- **Extensibilidade**: Fácil adição de novas funcionalidades
- **Manutenibilidade**: Mudanças em um plugin não afetam outros
- **Testabilidade**: Plugins podem ser testados isoladamente
- **Flexibilidade**: Plugins podem ser habilitados/desabilitados
- **Reutilização**: Plugins podem ser reutilizados em outros sistemas

### 🔄 Fluxo de Execução

1. **Inicialização**: O Kernel é criado e inicializado
2. **Descoberta**: ServiceLoader descobre plugins automaticamente
3. **Carregamento**: Plugins são carregados dinamicamente
4. **Execução**: Cada plugin executa sua funcionalidade específica
5. **Finalização**: Sistema exibe resumo da execução

## 📚 Conceitos Técnicos

### ServiceLoader

O `java.util.ServiceLoader` é utilizado para:
- Descoberta automática de implementações
- Carregamento dinâmico de classes
- Configuração via arquivos META-INF/services

### Banco de Dados SQLite

O sistema utiliza SQLite como banco de dados:
- **Arquivo**: `microkernel_ecommerce.db` (criado automaticamente)
- **Tabelas**: users, products, orders, order_products, payments
- **Dados de Exemplo**: Inseridos automaticamente na primeira execução
- **Conexão**: Gerenciada pelo `DatabaseManager` (Singleton)

#### Estrutura do Banco

```sql
-- Usuários do sistema
users (id, name, email, created_at)

-- Produtos do catálogo
products (id, name, description, price, stock, created_at)

-- Pedidos dos clientes
orders (id, user_id, total_amount, status, created_at)

-- Produtos de cada pedido
order_products (id, order_id, product_id, quantity, unit_price)

-- Pagamentos processados
payments (id, order_id, amount, payment_method, status, created_at)
```

### Interface Plugin

Define o contrato comum que todos os plugins devem implementar:
- `getName()`: Identificação do plugin
- `execute()`: Execução da funcionalidade

### Kernel

Atua como orquestrador do sistema:
- Gerencia o ciclo de vida dos plugins
- Fornece infraestrutura comum
- Coordena a execução dos plugins

## 🤝 Contribuição

Para contribuir com novos plugins ou melhorias:

1. Crie um novo plugin seguindo o padrão estabelecido
2. Registre o plugin no arquivo de configuração
3. Teste a execução
4. Documente as funcionalidades adicionadas

---

**Desenvolvido como demonstração da Arquitetura Microkernel em Java** 🚀 