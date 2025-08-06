package plugins.user.services;

import plugins.user.entities.User;
import plugins.user.repositories.UserRepository;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service para lógica de negócio de usuários.
 * Pertence ao plugin de gerenciamento de usuários.
 */
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registra um novo usuário.
     */
    public User registerUser(String name, String email) throws SQLException {
        // Validar dados
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }

        // Verificar se email já existe
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        // Criar e salvar usuário
        User user = new User(name.trim(), email.trim());
        return userRepository.save(user);
    }

    /**
     * Autentica um usuário.
     */
    public Optional<User> authenticateUser(String email) throws SQLException {
        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }

        return userRepository.findByEmail(email.trim());
    }

    /**
     * Busca usuário por ID.
     */
    public Optional<User> findUserById(Integer id) throws SQLException {
        if (id == null) {
            return Optional.empty();
        }

        return userRepository.findById(id);
    }

    /**
     * Lista todos os usuários.
     */
    public List<User> getAllUsers() throws SQLException {
        return userRepository.findAll();
    }

    /**
     * Verifica se um email já existe.
     */
    public boolean emailExists(String email) throws SQLException {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        return userRepository.existsByEmail(email.trim());
    }
} 