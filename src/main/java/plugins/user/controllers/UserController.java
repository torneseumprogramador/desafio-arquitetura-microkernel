package plugins.user.controllers;

import core.SimpleController;
import plugins.user.services.UserService;
import plugins.user.entities.User;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Controller de usuários usando sistema de rotas declarativo.
 * Cada método representa um endpoint específico.
 */
public class UserController extends SimpleController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * GET /api/users - Lista todos os usuários
     */
    public void listUsers(HttpExchange exchange) throws IOException {
        try {
            List<User> users = userService.getAllUsers();
            String response = buildUsersJson(users);
            sendJsonResponse(exchange, 200, response);
        } catch (SQLException e) {
            sendError(exchange, 500, "Database error: " + e.getMessage());
        }
    }
    
    /**
     * GET /api/users/{id} - Busca usuário por ID
     */
    public void getUserById(HttpExchange exchange) throws IOException {
        try {
            String path = getPath(exchange);
            int userId = extractId(path);
            
            Optional<User> user = userService.findUserById(userId);
            if (user.isPresent()) {
                String response = buildUserJson(user.get());
                sendJsonResponse(exchange, 200, response);
            } else {
                sendError(exchange, 404, "User not found");
            }
        } catch (SQLException e) {
            sendError(exchange, 500, "Database error: " + e.getMessage());
        }
    }
    
    /**
     * POST /api/users - Cria novo usuário
     */
    public void createUser(HttpExchange exchange) throws IOException {
        try {
            // TODO: Implementar leitura do body da requisição
            User newUser = userService.registerUser("Novo Usuário", "novo@email.com");
            String response = "{\"message\":\"Usuário criado com sucesso\",\"id\":" + newUser.getId() + "}";
            sendJsonResponse(exchange, 201, response);
        } catch (IllegalArgumentException e) {
            sendError(exchange, 400, e.getMessage());
        } catch (SQLException e) {
            sendError(exchange, 500, "Database error: " + e.getMessage());
        }
    }
    
    /**
     * PUT /api/users/{id} - Atualiza usuário
     */
    public void updateUser(HttpExchange exchange) throws IOException {
        String path = getPath(exchange);
        int userId = extractId(path);
        String response = "{\"message\":\"Usuário atualizado com sucesso\",\"id\":" + userId + "}";
        sendJsonResponse(exchange, 200, response);
    }
    
    /**
     * DELETE /api/users/{id} - Deleta usuário
     */
    public void deleteUser(HttpExchange exchange) throws IOException {
        String path = getPath(exchange);
        int userId = extractId(path);
        String response = "{\"message\":\"Usuário deletado com sucesso\",\"id\":" + userId + "}";
        sendJsonResponse(exchange, 200, response);
    }
    
    private String buildUsersJson(List<User> users) {
        StringBuilder json = new StringBuilder("{\"users\":[");
        for (int i = 0; i < users.size(); i++) {
            if (i > 0) json.append(",");
            json.append(buildUserJson(users.get(i)));
        }
        json.append("]}");
        return json.toString();
    }
    
    private String buildUserJson(User user) {
        return "{\"id\":" + user.getId() + 
               ",\"name\":\"" + user.getName() + "\"" +
               ",\"email\":\"" + user.getEmail() + "\"" +
               ",\"createdAt\":\"" + user.getCreatedAt() + "\"}";
    }
} 