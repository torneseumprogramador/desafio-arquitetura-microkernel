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

# Função para mostrar ajuda
show_help() {
    echo "Uso: $0 [COMANDO] [OPÇÃO]"
    echo ""
    echo "Comandos:"
    echo "  start     - Iniciar a API"
    echo "  stop      - Parar a API"
    echo "  restart   - Reiniciar a API"
    echo "  status    - Verificar status da API"
    echo "  test      - Testar a API"
    echo "  logs      - Ver logs da API"
    echo "  build     - Compilar o projeto"
    echo "  clean     - Limpar arquivos compilados"
    echo "  seed      - Executar seed do banco"
    echo "  help      - Mostrar esta ajuda"
    echo ""
    echo "Exemplos:"
    echo "  $0 start     # Iniciar a API"
    echo "  $0 test      # Testar a API"
    echo "  $0 status    # Verificar status"
    echo "  $0 seed      # Executar seed"
    echo ""
    echo "Scripts disponíveis:"
    echo "  ./scripts/run.sh      - Executar aplicação"
    echo "  ./scripts/test_api.sh - Testar API"
    echo "  ./scripts/seed.sh     - Executar seed"
    echo "  ./scripts/push.sh     - Git push automático"
}

# Função para verificar se a API está rodando
check_api_status() {
    if curl -s http://localhost:8080/api/health > /dev/null 2>&1; then
        return 0
    else
        return 1
    fi
}

# Função para iniciar a API
start_api() {
    print_info "Iniciando API Microkernel Ecommerce..."
    
    if check_api_status; then
        print_error "API já está rodando!"
        return 1
    fi
    
    # Executar em background
    nohup ../run.sh > api.log 2>&1 &
    API_PID=$!
    echo $API_PID > api.pid
    
    # Aguardar um pouco para a API inicializar
    sleep 3
    
    if check_api_status; then
        print_success "API iniciada com sucesso! (PID: $API_PID)"
        print_info "Logs: tail -f api.log"
        print_info "URL: http://localhost:8080/api"
    else
        print_error "Falha ao iniciar a API"
        return 1
    fi
}

# Função para parar a API
stop_api() {
    print_info "Parando API..."
    
    if [ -f api.pid ]; then
        API_PID=$(cat api.pid)
        if kill $API_PID 2>/dev/null; then
            print_success "API parada com sucesso!"
            rm -f api.pid
        else
            print_error "Falha ao parar a API"
            return 1
        fi
    else
        print_error "Arquivo PID não encontrado"
        return 1
    fi
}

# Função para reiniciar a API
restart_api() {
    print_info "Reiniciando API..."
    stop_api
    sleep 2
    start_api
}

# Função para verificar status
status_api() {
    print_header "Status da API"
    
    if check_api_status; then
        print_success "API está rodando"
        if [ -f api.pid ]; then
            API_PID=$(cat api.pid)
            print_info "PID: $API_PID"
        fi
        print_info "URL: http://localhost:8080/api"
        
        # Testar health check
        echo ""
        print_info "Health Check:"
        curl -s http://localhost:8080/api/health | jq '.' 2>/dev/null || curl -s http://localhost:8080/api/health
    else
        print_error "API não está rodando"
        print_info "Execute: $0 start"
    fi
}

# Função para testar a API
test_api() {
    print_info "Testando API..."
    ./test_api.sh
}

# Função para ver logs
logs_api() {
    if [ -f api.log ]; then
        print_info "Mostrando logs da API:"
        tail -f api.log
    else
        print_error "Arquivo de log não encontrado"
        print_info "Execute: $0 start"
    fi
}

# Função para build
build_api() {
    print_info "Compilando projeto..."
    cd ..
    mvn clean compile
    if [ $? -eq 0 ]; then
        print_success "Compilação concluída!"
    else
        print_error "Erro na compilação"
        return 1
    fi
}

# Função para clean
clean_api() {
    print_info "Limpando projeto..."
    cd ..
    mvn clean
    if [ $? -eq 0 ]; then
        print_success "Limpeza concluída!"
    else
        print_error "Erro na limpeza"
        return 1
    fi
}

# Função para seed
seed_api() {
    print_info "Executando seed do banco de dados..."
    cd ..
    ./scripts/seed.sh
}

# Processar argumentos
case "${1:-help}" in
    "start")
        start_api
        ;;
    "stop")
        stop_api
        ;;
    "restart")
        restart_api
        ;;
    "status")
        status_api
        ;;
    "test")
        test_api
        ;;
    "logs")
        logs_api
        ;;
    "build")
        build_api
        ;;
            "clean")
            clean_api
            ;;
        "seed")
            seed_api
            ;;
        "help"|"-h"|"--help")
            show_help
            ;;
    *)
        print_error "Comando '$1' não reconhecido"
        show_help
        exit 1
        ;;
esac 