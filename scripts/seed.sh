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

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

# Função para verificar dependências
check_java() {
    if ! command -v java &> /dev/null; then
        print_error "Java não encontrado!"
        print_info "Instale o Java JDK 11 ou superior"
        exit 1
    fi
    print_success "Java encontrado: $(java -version 2>&1 | head -n 1)"
}

check_maven() {
    if ! command -v mvn &> /dev/null; then
        print_error "Maven não encontrado!"
        print_info "Instale o Apache Maven"
        exit 1
    fi
    print_success "Maven encontrado: $(mvn -version 2>&1 | head -n 1)"
}

# Função para executar seed
run_seed() {
    print_info "Executando seed do banco de dados..."
    mvn clean compile exec:java -Dexec.mainClass="app.SeedRunner" -Dexec.args="$1"
}

# Função para mostrar ajuda
show_help() {
    echo "Uso: $0 [OPÇÃO]"
    echo ""
    echo "Opções:"
    echo "  normal    - Executa seed normal (padrão)"
    echo "  force     - Força seed (limpa dados existentes)"
    echo "  check     - Verifica se existem dados no banco"
    echo "  help      - Mostra esta ajuda"
    echo ""
    echo "Exemplos:"
    echo "  $0          # Executa seed normal"
    echo "  $0 normal   # Executa seed normal"
    echo "  $0 force    # Força seed (limpa dados)"
    echo "  $0 check    # Verifica dados existentes"
    echo ""
}

# Função para verificar dados
check_data() {
    print_info "Verificando dados no banco..."
    mvn clean compile exec:java -Dexec.mainClass="app.SeedRunner" -Dexec.args="check"
}

# Função principal
main() {
    print_header "🌱 Seed do Banco de Dados"
    print_info "Iniciando script de seed..."
    
    # Verificar dependências
    check_java
    check_maven
    
    # Processar argumentos
    case "${1:-normal}" in
        "normal")
            run_seed "normal"
            ;;
        "force")
            print_warning "Forçando seed - dados existentes serão removidos!"
            read -p "Tem certeza? (y/N): " -n 1 -r
            echo
            if [[ $REPLY =~ ^[Yy]$ ]]; then
                run_seed "force"
            else
                print_info "Operação cancelada"
            fi
            ;;
        "check")
            check_data
            ;;
        "help"|"-h"|"--help")
            show_help
            ;;
        *)
            print_warning "Opção '$1' não reconhecida. Usando modo padrão (normal)."
            run_seed "normal"
            ;;
    esac
}

# Executar função principal
main "$@" 