import org.junit.jupiter.api.Test;
import ru.yandex.practicum.impl.SecureStateImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SecureStateImplIntegrationTest {

    @Test
    void multiUserStateIsPreservedBetweenLoginsLogoutDoesNotRemoveFromLocation() {
        SecureStateImpl state = new SecureStateImpl();

        String owlName = state.doAction("login", "owl", "pass123");
        assertNotNull(owlName);
        assertEquals("OK", state.doAction("E", "1"));
        assertEquals("OK", state.doAction("E", "2"));
        assertEquals("OK", state.doAction("Q"));

        String eeyoreName = state.doAction("login", "eeyore", "pass123");
        assertNotNull(eeyoreName);
        assertEquals("OK", state.doAction("E", "1"));
        assertEquals("Access denied", state.doAction("E", "2"));
    }

    @Test
    void historyContainsNonObviousEventsLikeHistoryRequestAndLogout() {
        SecureStateImpl state = new SecureStateImpl();

        assertNotNull(state.doAction("login", "owl", "pass123"));
        assertEquals("OK", state.doAction("E", "1"));
        assertEquals("OK", state.doAction("H"));
        assertEquals("OK", state.doAction("Q"));

        List<Object> history = state.getActionHistory();

        assertTrue(history.stream().anyMatch(x -> x.toString().contains("Login successful")));
        assertTrue(history.stream().anyMatch(x -> x.toString().contains("History requested")));
        assertTrue(history.stream().anyMatch(x -> x.toString().contains("Logout")));
    }
}
