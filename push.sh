#!/bin/bash

# 🚀 Script de Automação Git - Sistema Microkernel Ecommerce
# ===========================================================

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

# Função para verificar se o Git está instalado
check_git() {
    if ! command -v git &> /dev/null; then
        print_error "Git não encontrado! Por favor, instale o Git primeiro."
        exit 1
    fi
}

# Função para verificar se estamos em um repositório Git
check_repository() {
    if ! git rev-parse --git-dir > /dev/null 2>&1; then
        print_error "Não estamos em um repositório Git!"
        exit 1
    fi
}

# Função para verificar se há alterações para commitar
check_changes() {
    if git diff-index --quiet HEAD --; then
        print_warning "Não há alterações para commitar."
        return 1
    fi
    return 0
}

# Função para obter a mensagem do commit
get_commit_message() {
    if [ -z "$1" ]; then
        echo ""
        print_message "Por favor, digite a mensagem do commit:"
        echo -e "${BLUE}Exemplos:${NC}"
        echo -e "  - Adiciona menu interativo"
        echo -e "  - Corrige problema do logger SLF4J"
        echo -e "  - Implementa integração com SQLite"
        echo -e "  - Atualiza documentação"
        echo ""
        read -p "Mensagem: " commit_message
        
        # Verificar se a mensagem não está vazia
        if [ -z "$commit_message" ]; then
            print_error "Mensagem de commit não pode estar vazia!"
            exit 1
        fi
    else
        commit_message="$1"
    fi
}

# Função para mostrar status do Git
show_status() {
    print_message "Status atual do repositório:"
    echo ""
    git status --short
    echo ""
}

# Função para fazer o push
do_push() {
    local commit_message="$1"
    
    print_header "🚀 Iniciando processo de push..."
    
    # Verificar se há alterações
    if ! check_changes; then
        print_warning "Nenhuma alteração detectada. Deseja continuar mesmo assim? (y/N)"
        read -r response
        if [[ ! "$response" =~ ^[Yy]$ ]]; then
            print_message "Operação cancelada pelo usuário."
            exit 0
        fi
    fi
    
    # Mostrar status
    show_status
    
    # Adicionar todas as alterações
    print_message "📝 Adicionando alterações..."
    if git add .; then
        print_message "✅ Arquivos adicionados com sucesso!"
    else
        print_error "❌ Erro ao adicionar arquivos!"
        exit 1
    fi
    
    # Fazer commit
    print_message "💾 Realizando commit..."
    if git commit -m "$commit_message"; then
        print_message "✅ Commit realizado com sucesso!"
        print_message "📝 Mensagem: $commit_message"
    else
        print_error "❌ Erro ao fazer commit!"
        exit 1
    fi
    
    # Atualizar repositório local
    print_message "⬇️ Atualizando repositório local..."
    if git pull; then
        print_message "✅ Repositório local atualizado!"
    else
        print_warning "⚠️ Aviso: Erro ao fazer pull (pode ser normal se não houver upstream)"
    fi
    
    # Fazer push
    print_message "⬆️ Enviando alterações..."
    if git push; then
        print_message "✅ Push realizado com sucesso!"
    else
        print_error "❌ Erro ao fazer push!"
        print_warning "💡 Verifique se você tem permissões para fazer push neste repositório."
        exit 1
    fi
    
    print_header "🎉 Processo concluído com sucesso!"
}

# Função para mostrar ajuda
show_help() {
    echo -e "${BLUE}Uso:${NC} $0 [MENSAGEM_DO_COMMIT]"
    echo ""
    echo -e "${BLUE}Descrição:${NC}"
    echo -e "  Script de automação para fazer push no Git"
    echo ""
    echo -e "${BLUE}Opções:${NC}"
    echo -e "  ${GREEN}MENSAGEM_DO_COMMIT${NC}  - Mensagem do commit (opcional)"
    echo -e "  ${GREEN}help${NC}               - Mostra esta ajuda"
    echo ""
    echo -e "${BLUE}Exemplos:${NC}"
    echo -e "  $0                                    # Solicita mensagem interativamente"
    echo -e "  $0 \"Adiciona menu interativo\"       # Usa mensagem fornecida"
    echo -e "  $0 help                               # Mostra ajuda"
    echo ""
    echo -e "${BLUE}Funcionalidades:${NC}"
    echo -e "  ✅ Verifica se Git está instalado"
    echo -e "  ✅ Verifica se estamos em repositório Git"
    echo -e "  ✅ Mostra status das alterações"
    echo -e "  ✅ Adiciona todas as alterações"
    echo -e "  ✅ Faz commit com mensagem personalizada"
    echo -e "  ✅ Atualiza repositório local (pull)"
    echo -e "  ✅ Envia alterações (push)"
    echo ""
}

# Função principal
main() {
    print_header "🛒 Sistema Microkernel Ecommerce - Git Push"
    
    # Verificar argumentos
    if [ "$1" = "help" ] || [ "$1" = "-h" ] || [ "$1" = "--help" ]; then
        show_help
        exit 0
    fi
    
    # Verificar dependências
    check_git
    check_repository
    
    # Obter mensagem do commit
    get_commit_message "$1"
    
    # Executar push
    do_push "$commit_message"
}

# Executar função principal
main "$@" 