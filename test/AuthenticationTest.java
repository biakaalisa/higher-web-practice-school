import model.role.Chief;
import model.user.User;
import org.junit.jupiter.api.Test;
import service.auth.AuthenticationService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationTest {

    private Map<String, User> createUsers() {
        Map<String, User> users = new HashMap<>();
        User owl = User.createUser("owl", "Сова", "🦉", "pass123", new Chief(), null);
        users.put(owl.getLogin(), owl);

        return users;
    }

    @Test
    void existingUserWithWrongPasswordCannotLogin() {
        AuthenticationService auth = new AuthenticationService(createUsers());
        User user = auth.authenticate("owl", "WRONG_PASSWORD");
        assertNull(user, "Existing user must NOT login with wrong password");
    }

    @Test
    void nonExistingUserCannotLoginEvenWithCorrectPassword() {
        AuthenticationService auth = new AuthenticationService(createUsers());
        User user = auth.authenticate("unknown_user", "pass123");
        assertNull(user, "Non-existing user must NOT be able to login");
    }

    @Test
    void existingUserWithCorrectPasswordCanLogin() {
        AuthenticationService auth = new AuthenticationService(createUsers());
        User user = auth.authenticate("owl", "pass123");
        assertNotNull(user, "Existing user must login with correct password");
        assertEquals("owl", user.getLogin());
    }
}
