# 🛒 Ecommerce Microkernel Architecture - Desafio de Arquiteturas de Software

## 📚 Sobre o Projeto

Este projeto foi desenvolvido como parte do **Desafio de Arquiteturas de Software** do curso [Arquiteturas de Software Modernas](https://www.torneseumprogramador.com.br/cursos/arquiteturas_software) ministrado pelo **Prof. Danilo Aparecido** na plataforma [Torne-se um Programador](https://www.torneseumprogramador.com.br/).

### 🎯 Objetivo

Implementar um sistema de e-commerce utilizando **Microkernel Architecture (Plugin-Based)** com Java, Maven, SQLite, JDBC, sistema de rotas declarativo e **documentação Swagger/OpenAPI plugável**.

## 🏗️ Arquitetura

O projeto segue os princípios da **Microkernel Architecture** com separação clara entre core e plugins:

```
┌─────────────────────────────────────┐
│              Core                   │ ← Kernel, DatabaseManager, RouteRegistry
├─────────────────────────────────────┤
│           Plugins                   │ ← UserPlugin, ProductPlugin, OrderPlugin
│  ┌─────────┬─────────┬─────────┐   │
│  │  User   │ Product │  Order  │   │ ← Cada plugin autocontido
│  │ Plugin  │ Plugin  │ Plugin  │   │
│  └─────────┴─────────┴─────────┘   │
└─────────────────────────────────────┘
```

### 📁 Estrutura do Projeto

```
microkernel-ecommerce/
├── src/main/java/
│   ├── app/                          # Camada de Aplicação
│   │   ├── Main.java                 # Ponto de entrada da API
│   │   ├── SeedRunner.java           # Executor de seed do banco
│   │   └── AutoMode.java             # Modo automático (sem menu)
│   ├── core/                         # Core do Microkernel
│   │   ├── Kernel.java               # Orquestrador principal
│   │   ├── Plugin.java               # Interface dos plugins
│   │   ├── PluginLoader.java         # Carregador via ServiceLoader
│   │   ├── DatabaseManager.java      # Gerenciador do SQLite
│   │   ├── SeedManager.java          # Gerenciador de dados iniciais
│   │   ├── Route.java                # Definição de rota
│   │   ├── RouteRegistry.java        # Registro de rotas
│   │   ├── SimpleController.java     # Controller base
│   │   ├── CoreRoutes.java           # Rotas do core
│   │   ├── HttpHandler.java          # Interface HTTP handler
│   │   ├── SwaggerGenerator.java     # Gerador de documentação OpenAPI
│   │   └── controllers/              # Controllers do core
│   │       ├── HomeController.java    # Controller home
│   │       ├── HealthController.java  # Controller health
│   │       └── SwaggerController.java # Controller Swagger
│   └── plugins/                      # Plugins autocontidos
│       ├── user/                     # Plugin de Usuários
│       │   ├── UserPlugin.java       # Plugin principal
│       │   ├── entities/             # Entidades
│       │   ├── repositories/         # Repositórios
│       │   ├── services/             # Serviços
│       │   ├── controllers/          # Controllers
│       │   └── routes/               # Definição de rotas
│       ├── product/                  # Plugin de Produtos
│       │   ├── ProductPlugin.java    # Plugin principal
│       │   ├── entities/             # Entidades
│       │   ├── repositories/         # Repositórios
│       │   ├── services/             # Serviços
│       │   ├── controllers/          # Controllers
│       │   └── routes/               # Definição de rotas
│       └── order/                    # Plugin de Pedidos
│           ├── OrderPlugin.java      # Plugin principal
│           ├── entities/             # Entidades
│           ├── repositories/         # Repositórios
│           ├── services/             # Serviços
│           ├── controllers/          # Controllers
│           └── routes/               # Definição de rotas
├── src/main/resources/
│   ├── META-INF/services/           # ServiceLoader
│   │   └── core.Plugin              # Registro dos plugins
│   └── simplelogger.properties      # Configuração do SLF4J
├── scripts/                          # Scripts de automação
│   ├── api.sh                       # Gerenciamento da API
│   ├── seed.sh                      # Seed do banco
│   └── test_api.sh                  # Testes da API
├── run.sh                           # Script de execução
├── push.sh                          # Script de Git
├── pom.xml                          # Configuração Maven
└── README.md                        # Esta documentação
```

## 🚀 Tecnologias Utilizadas

- **Java 11+** - Linguagem de programação
- **Maven** - Gerenciamento de dependências
- **SQLite** - Banco de dados local
- **JDBC** - Acesso a dados
- **SLF4J** - Logging
- **ServiceLoader** - Descoberta de plugins
- **HttpServer** - Servidor HTTP embutido
- **Swagger/OpenAPI** - Documentação interativa
- **Jackson** - Processamento JSON
- **Microkernel Architecture** - Organização do projeto

## 📋 Pré-requisitos

- [Java 11+](https://adoptium.net/)
- [Maven](https://maven.apache.org/)
- [Git](https://git-scm.com/)

## ⚡ Como Executar

```bash
# Clone o repositório
$ git clone <url-do-repositorio>
$ cd desafio-arquitetura-microkernel

# Compile o projeto
$ mvn clean compile

# Execute a aplicação
$ ./run.sh

# Ou execute diretamente
$ mvn clean compile exec:java -Dexec.mainClass="app.Main"

# Para modo automático (sem API)
$ mvn clean compile exec:java -Dexec.mainClass="app.AutoMode"
```

## 🌐 Acessando a API

Após executar o projeto, a API estará disponível em:

- **API Base**: http://localhost:8080
- **Documentação**: http://localhost:8080/api/docs
- **Health Check**: http://localhost:8080/api/health
- **📖 Swagger UI**: http://localhost:8080/api/swagger-ui
- **📄 OpenAPI JSON**: http://localhost:8080/api/swagger

## 📖 Documentação Swagger/OpenAPI

### 🎯 Documentação Plugável

O sistema possui **documentação Swagger/OpenAPI totalmente plugável** que se adapta dinamicamente aos plugins carregados:

#### **✨ Características:**
- ✅ **Documentação dinâmica** baseada nos plugins ativos
- ✅ **Agrupamento por tags** (System, Usuários, Produtos, Pedidos)
- ✅ **Schemas automáticos** gerados pelos plugins
- ✅ **Endpoints organizados** por funcionalidade
- ✅ **Interface interativa** para testar a API

#### **🔧 Como Funciona:**
1. **Plugins fornecem** suas próprias informações de documentação
2. **SwaggerGenerator** coleta dinamicamente os dados
3. **Interface se adapta** automaticamente aos plugins carregados
4. **Zero configuração** manual necessária

#### **📊 Exemplo de Agrupamento:**
```
Tags disponíveis:
  - System: Endpoints relacionados ao sistema principal
  - Usuários: Endpoints relacionados a usuários
  - Produtos: Endpoints relacionados a produtos
  - Pedidos: Endpoints relacionados a pedidos

Endpoints por tag:
  System: 8 endpoints
  Usuários: 5 endpoints
  Produtos: 7 endpoints
  Pedidos: 8 endpoints
```

## 📖 Endpoints da API

### 🏠 Home
| Método | Endpoint | Descrição                |
|--------|----------|--------------------------|
| GET    | `/`      | Página inicial da API    |
| GET    | `/api`   | Informações da API       |
| GET    | `/api/docs` | Documentação da API   |

### ❤️ Health
| Método | Endpoint           | Descrição                        |
|--------|-------------------|----------------------------------|
| GET    | `/api/health`     | Status básico da API             |
| GET    | `/api/health/detailed` | Status detalhado da API      |
| GET    | `/api/health/database` | Status do banco de dados   |

### 📚 Swagger/OpenAPI
| Método | Endpoint           | Descrição                        |
|--------|-------------------|----------------------------------|
| GET    | `/api/swagger`    | Documentação OpenAPI (JSON)      |
| GET    | `/api/swagger-ui` | Interface Swagger UI             |

### 👤 Usuários (User)
| Método | Endpoint         | Descrição           |
|--------|------------------|---------------------|
| GET    | `/api/users`     | Listar usuários     |
| GET    | `/api/users/{id}`| Buscar usuário por ID |
| POST   | `/api/users`     | Criar usuário       |
| PUT    | `/api/users/{id}`| Atualizar usuário   |
| DELETE | `/api/users/{id}`| Deletar usuário     |

### 📦 Produtos (Product)
| Método | Endpoint           | Descrição           |
|--------|--------------------|---------------------|
| GET    | `/api/products`    | Listar produtos     |
| GET    | `/api/products/available` | Produtos disponíveis |
| GET    | `/api/products/{id}`| Buscar produto por ID |
| POST   | `/api/products`    | Criar produto       |
| PUT    | `/api/products/{id}`| Atualizar produto   |
| PUT    | `/api/products/{id}/stock` | Atualizar estoque |
| DELETE | `/api/products/{id}`| Deletar produto     |

### 🛒 Pedidos (Order)
| Método | Endpoint           | Descrição           |
|--------|--------------------|---------------------|
| GET    | `/api/orders`      | Listar pedidos      |
| GET    | `/api/orders/{id}` | Buscar pedido por ID|
| GET    | `/api/orders/user/{userId}` | Pedidos do usuário |
| POST   | `/api/orders`      | Criar pedido        |
| POST   | `/api/orders/{id}/products` | Adicionar produto |
| PUT    | `/api/orders/{id}` | Atualizar pedido    |
| PUT    | `/api/orders/{id}/finalize` | Finalizar pedido |
| DELETE | `/api/orders/{id}` | Deletar pedido      |

## 🧪 Exemplos de Uso

### Criar Usuário
```bash
curl -X POST "http://localhost:8080/api/users" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@email.com",
    "password": "123456"
  }'
```

### Criar Produto
```bash
curl -X POST "http://localhost:8080/api/products" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Notebook Dell",
    "description": "Notebook i7 16GB",
    "price": 4999.99,
    "stock": 10
  }'
```

### Criar Pedido
```bash
curl -X POST "http://localhost:8080/api/orders" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "products": [
      {
        "productId": 1,
        "quantity": 2
      }
    ]
  }'
```

## 🛠️ Scripts Disponíveis

### Execução da Aplicação
```bash
# Executar API
./run.sh

# Executar modo automático
./run.sh auto

# Gerar JAR executável
./run.sh jar

# Build do projeto
./run.sh build

# Limpar projeto
./run.sh clean

# Ajuda
./run.sh help
```

### Gerenciamento da API
```bash
# Iniciar API
./scripts/api.sh start

# Parar API
./scripts/api.sh stop

# Status da API
./scripts/api.sh status

# Logs da API
./scripts/api.sh logs

# Testar API
./scripts/api.sh test

# Seed do banco
./scripts/api.sh seed
```

### Seed do Banco de Dados
```bash
# Executar seed normal
./scripts/seed.sh

# Executar seed forçado (limpa dados existentes)
./scripts/seed.sh force

# Verificar se há dados
./scripts/seed.sh check
```

### Git Automation
```bash
# Push automático
./push.sh "mensagem do commit"

# Ajuda
./push.sh help
```

## 🛡️ Tratamento de Erros

A API retorna mensagens de erro padronizadas para validação e exceções de negócio.

### Exemplo de erro de validação
```json
{
  "error": "Email já cadastrado",
  "status": 400
}
```

## 🔧 Configuração do Banco de Dados

O banco SQLite é criado automaticamente no arquivo `microkernel_ecommerce.db` na raiz do projeto.

### Estrutura do Banco
```sql
-- Tabela de usuários
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de produtos
CREATE TABLE products (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de pedidos
CREATE TABLE orders (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status TEXT NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Tabela de produtos do pedido
CREATE TABLE order_products (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```

## 🏗️ Arquitetura Microkernel

### Princípios Aplicados

1. **Core Mínimo**: Apenas infraestrutura essencial
2. **Plugins Autocontidos**: Cada plugin gerencia sua própria entidade
3. **Descoberta Dinâmica**: ServiceLoader carrega plugins automaticamente
4. **Baixo Acoplamento**: Plugins não dependem uns dos outros
5. **Extensibilidade**: Novos plugins podem ser adicionados facilmente
6. **Documentação Plugável**: Swagger se adapta aos plugins carregados

### Vantagens da Arquitetura

- ✅ **Modularidade**: Cada plugin é independente
- ✅ **Extensibilidade**: Fácil adicionar novos recursos
- ✅ **Manutenibilidade**: Mudanças isoladas por plugin
- ✅ **Testabilidade**: Plugins podem ser testados isoladamente
- ✅ **Flexibilidade**: Plugins podem ser ativados/desativados
- ✅ **Documentação Dinâmica**: Swagger se adapta automaticamente

### 🔧 Interface Plugin Expandida

A interface `Plugin` foi expandida para suportar documentação plugável:

```java
public interface Plugin {
    String getName();
    void execute();
    List<String> getAvailableRoutes();
    String getEmoji();
    
    // Novos métodos para documentação Swagger
    Map<String, Object> getSwaggerInfo();
    Map<String, Object> getOpenApiSchemas();
    Map<String, Object> getOpenApiPaths();
    List<String> getOpenApiTags();
}
```

### 📊 SwaggerGenerator Plugável

O `SwaggerGenerator` coleta dinamicamente informações dos plugins:

- ✅ **Schemas automáticos** dos plugins
- ✅ **Paths dinâmicos** baseados nos endpoints
- ✅ **Tags organizadas** por funcionalidade
- ✅ **Zero configuração** manual

## 🧪 Testes da API

```bash
# Testar todos os endpoints
./scripts/test_api.sh

# Testar endpoints específicos
./scripts/test_api.sh users
./scripts/test_api.sh products
./scripts/test_api.sh orders
./scripts/test_api.sh health
```

## 📝 Logs e Monitoramento

A aplicação utiliza SLF4J para logging configurado em `src/main/resources/simplelogger.properties`.

### Níveis de Log
- **INFO**: Operações normais
- **WARN**: Avisos não críticos
- **ERROR**: Erros que precisam atenção
- **DEBUG**: Informações detalhadas para debug

## 👨‍🏫 Sobre o Professor

**Prof. Danilo Aparecido** é instrutor na plataforma [Torne-se um Programador](https://www.torneseumprogramador.com.br/), especializado em arquiteturas de software e desenvolvimento de sistemas escaláveis.

## 📚 Curso Completo

Para aprender mais sobre arquiteturas de software e aprofundar seus conhecimentos, acesse o curso completo:

**[Arquiteturas de Software Modernas](https://www.torneseumprogramador.com.br/cursos/arquiteturas_software)**

## 🤝 Contribuição

Este projeto foi desenvolvido como parte de um desafio educacional. Contribuições são bem-vindas através de issues e pull requests.

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

**Desenvolvido com ❤️ para o curso de Arquiteturas de Software do [Torne-se um Programador](https://www.torneseumprogramador.com.br/)** 