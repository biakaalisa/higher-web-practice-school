import model.location.School;
import model.role.Parent;
import model.role.Student;
import model.user.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserParentValidationTest {

    @Test
    void userCannotBeParentOfHimselfByName() {
        assertThrows(IllegalArgumentException.class, () -> User.createUser(
                "kanga",
                "Kanga",
                "🐨",
                "pass123",
                new Parent(),
                new School(),
                List.of("Kanga")
        ));
    }

    @Test
    void userCannotBeParentOfHimselfByLogin() {
        assertThrows(IllegalArgumentException.class, () -> User.createUser(
                "kanga",
                "Kanga",
                "🐨",
                "pass123",
                new Parent(),
                new School(),
                List.of("kanga")
        ));
    }

    @Test
    void childrenCanExistOnlyForParentRole() {
        assertThrows(IllegalArgumentException.class, () -> User.createUser(
                "roo",
                "Roo",
                "🦘",
                "pass123",
                new Student(),
                new School(),
                List.of("SomeChild")
        ));
    }

    @Test
    void parentCanHaveChildrenOk() {
        assertDoesNotThrow(() -> User.createUser(
                "kanga",
                "Kanga",
                "🐨",
                "pass123",
                new Parent(),
                new School(),
                List.of("Roo")
        ));
    }
}
