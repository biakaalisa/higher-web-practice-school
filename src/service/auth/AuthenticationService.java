package service.auth;

import model.user.User;

import java.util.Map;

public class AuthenticationService {
    private final Map<String, User> users;

    public AuthenticationService(Map<String, User> users) {
        this.users = users;
    }

    public User authenticate(String login, String password) {
        User user = users.get(login);
        if (user != null && user.checkPassword(password)) {
            return user;
        }
        return null;
    }
}