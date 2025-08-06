#!/bin/bash

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Funções de output
print_header() {
    echo -e "${BLUE}================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}================================${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

print_info() {
    echo -e "${YELLOW}ℹ️  $1${NC}"
}

# Configurações
API_BASE_URL="http://localhost:8080/api"
WAIT_TIME=3

# Função para fazer requisições HTTP
make_request() {
    local method=$1
    local endpoint=$2
    local data=$3
    
    if [ -n "$data" ]; then
        curl -s -X "$method" \
             -H "Content-Type: application/json" \
             -d "$data" \
             "$API_BASE_URL$endpoint"
    else
        curl -s -X "$method" "$API_BASE_URL$endpoint"
    fi
}

# Função para testar endpoint
test_endpoint() {
    local method=$1
    local endpoint=$2
    local description=$3
    local data=$4
    
    print_info "Testando: $method $endpoint - $description"
    
    local response=$(make_request "$method" "$endpoint" "$data")
    local status_code=$(echo "$response" | tail -n1)
    
    if [ "$status_code" = "200" ] || [ "$status_code" = "201" ]; then
        print_success "Endpoint funcionando!"
        echo "$response" | head -n -1 | jq '.' 2>/dev/null || echo "$response"
    else
        print_error "Falha no endpoint (Status: $status_code)"
        echo "$response"
    fi
    
    echo ""
}

# Função principal
main() {
    print_header "🛒 Testando API Microkernel Ecommerce"
    
    # Verificar se o servidor está rodando
    print_info "Verificando se o servidor está rodando..."
    if ! curl -s "$API_BASE_URL/health" > /dev/null 2>&1; then
        print_error "Servidor não está rodando!"
        print_info "Execute: ./run.sh"
        exit 1
    fi
    
    print_success "Servidor está rodando!"
    echo ""
    
    # Testar health check
    test_endpoint "GET" "/health" "Health Check da API"
    
    # Testar endpoints de usuários
    print_header "👥 Testando API de Usuários"
    test_endpoint "GET" "/users" "Listar usuários"
    test_endpoint "POST" "/users" "Criar usuário" '{"name":"Teste","email":"teste@email.com"}'
    test_endpoint "GET" "/users/1" "Buscar usuário por ID"
    
    # Testar endpoints de produtos
    print_header "📦 Testando API de Produtos"
    test_endpoint "GET" "/products" "Listar produtos"
    test_endpoint "POST" "/products" "Criar produto" '{"name":"Produto Teste","price":99.99,"stock":10}'
    test_endpoint "GET" "/products/1" "Buscar produto por ID"
    
    # Testar endpoints de pedidos
    print_header "📋 Testando API de Pedidos"
    test_endpoint "GET" "/orders" "Listar pedidos"
    test_endpoint "POST" "/orders" "Criar pedido" '{"userId":1,"totalAmount":299.99}'
    test_endpoint "GET" "/orders/1" "Buscar pedido por ID"
    
    print_header "🎉 Testes concluídos!"
    print_info "Para ver mais detalhes, use: curl -v http://localhost:8080/api/users"
}

# Verificar dependências
check_dependencies() {
    if ! command -v curl &> /dev/null; then
        print_error "curl não está instalado"
        exit 1
    fi
    
    if ! command -v jq &> /dev/null; then
        print_info "jq não está instalado - JSON não será formatado"
    fi
}

# Mostrar ajuda
show_help() {
    echo "Uso: $0 [OPÇÃO]"
    echo ""
    echo "Opções:"
    echo "  test     - Executar todos os testes (padrão)"
    echo "  health   - Testar apenas health check"
    echo "  users    - Testar apenas API de usuários"
    echo "  products - Testar apenas API de produtos"
    echo "  orders   - Testar apenas API de pedidos"
    echo "  help     - Mostrar esta ajuda"
    echo ""
    echo "Exemplos:"
    echo "  $0 test     # Executar todos os testes"
    echo "  $0 health   # Testar apenas health check"
}

# Processar argumentos
case "${1:-test}" in
    "test")
        check_dependencies
        main
        ;;
    "health")
        check_dependencies
        test_endpoint "GET" "/health" "Health Check da API"
        ;;
    "users")
        check_dependencies
        print_header "👥 Testando API de Usuários"
        test_endpoint "GET" "/users" "Listar usuários"
        test_endpoint "POST" "/users" "Criar usuário" '{"name":"Teste","email":"teste@email.com"}'
        ;;
    "products")
        check_dependencies
        print_header "📦 Testando API de Produtos"
        test_endpoint "GET" "/products" "Listar produtos"
        test_endpoint "POST" "/products" "Criar produto" '{"name":"Produto Teste","price":99.99,"stock":10}'
        ;;
    "orders")
        check_dependencies
        print_header "📋 Testando API de Pedidos"
        test_endpoint "GET" "/orders" "Listar pedidos"
        test_endpoint "POST" "/orders" "Criar pedido" '{"userId":1,"totalAmount":299.99}'
        ;;
    "help"|"-h"|"--help")
        show_help
        ;;
    *)
        print_error "Opção '$1' não reconhecida"
        show_help
        exit 1
        ;;
esac 