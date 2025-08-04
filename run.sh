#!/bin/bash

# 🛒 Sistema Microkernel Ecommerce - Script de Execução
# =====================================================

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Função para exibir mensagens coloridas
print_message() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_header() {
    echo -e "${CYAN}================================${NC}"
    echo -e "${CYAN}$1${NC}"
    echo -e "${CYAN}================================${NC}"
}

# Verificar se o Maven está instalado
check_maven() {
    if ! command -v mvn &> /dev/null; then
        print_error "Maven não encontrado! Por favor, instale o Maven primeiro."
        exit 1
    fi
}

# Verificar se o Java está instalado
check_java() {
    if ! command -v java &> /dev/null; then
        print_error "Java não encontrado! Por favor, instale o Java 11 ou superior."
        exit 1
    fi
}

# Função para executar via Maven
run_with_maven() {
    print_message "Executando via Maven..."
    mvn clean compile exec:java -Dexec.mainClass="app.Main"
}

# Função para executar em modo automático (sem menu)
run_auto_mode() {
    print_message "Executando em modo automático..."
    mvn clean compile exec:java -Dexec.mainClass="app.AutoMode"
}

# Função para executar via JAR
run_with_jar() {
    if [ ! -f "target/microkernel-ecommerce-1.0.0.jar" ]; then
        print_warning "JAR não encontrado. Gerando JAR primeiro..."
        mvn clean package
    fi
    
    print_message "Executando via JAR..."
    java -jar target/microkernel-ecommerce-1.0.0.jar
}

# Função para gerar JAR
build_jar() {
    print_message "Gerando JAR executável..."
    mvn clean package
    if [ $? -eq 0 ]; then
        print_message "JAR gerado com sucesso em target/microkernel-ecommerce-1.0.0.jar"
    else
        print_error "Erro ao gerar JAR"
        exit 1
    fi
}

# Função para mostrar ajuda
show_help() {
    echo -e "${BLUE}Uso:${NC} $0 [OPÇÃO]"
    echo ""
    echo -e "${BLUE}Opções:${NC}"
    echo -e "  ${GREEN}maven${NC}     - Executa via Maven com menu interativo (padrão)"
    echo -e "  ${GREEN}auto${NC}      - Executa via Maven em modo automático"
    echo -e "  ${GREEN}jar${NC}       - Executa via JAR"
    echo -e "  ${GREEN}build${NC}     - Gera JAR executável"
    echo -e "  ${GREEN}clean${NC}     - Limpa arquivos compilados"
    echo -e "  ${GREEN}help${NC}      - Mostra esta ajuda"
    echo ""
    echo -e "${BLUE}Exemplos:${NC}"
    echo -e "  $0          # Executa via Maven com menu"
    echo -e "  $0 maven    # Executa via Maven com menu"
    echo -e "  $0 auto     # Executa em modo automático"
    echo -e "  $0 jar      # Executa via JAR"
    echo -e "  $0 build    # Gera JAR"
    echo ""
}

# Função para limpar
clean_project() {
    print_message "Limpando projeto..."
    mvn clean
    print_message "Limpeza concluída!"
}

# Função principal
main() {
    print_header "🛒 Sistema Microkernel Ecommerce"
    print_message "Iniciando script de execução..."
    
    # Verificar dependências
    check_java
    check_maven
    
    # Processar argumentos
    case "${1:-maven}" in
        "maven")
            run_with_maven
            ;;
        "auto")
            run_auto_mode
            ;;
        "jar")
            run_with_jar
            ;;
        "build")
            build_jar
            ;;
        "clean")
            clean_project
            ;;
        "help"|"-h"|"--help")
            show_help
            ;;
        *)
            print_warning "Opção '$1' não reconhecida. Usando modo padrão (maven)."
            run_with_maven
            ;;
    esac
}

# Executar função principal
main "$@" 