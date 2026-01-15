import model.condition.DirectorAbsentCondition;
import model.condition.OwnChildOnlyCondition;
import model.condition.TeacherPresentCondition;
import model.location.Classroom;
import model.location.DirectorCabin;
import model.location.Location;
import model.permission.EnterCabin;
import model.permission.EnterClass;
import model.permission.Permission;
import model.permission.WatchJournal;
import model.role.Chief;
import model.role.Parent;
import model.role.Student;
import model.role.Teacher;
import model.user.User;
import model.user.UserInterface;
import org.junit.jupiter.api.Test;
import service.auth.AuthorizationService;
import service.state.SchoolState;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorizationServiceTest {

    private final AuthorizationService service = new AuthorizationService();

    private User makeUser(String login, String name, model.role.Role role) {
        return User.createUser(login, name, "", "pass123", role, null);
    }

    @Test
    void authorizeReturnsFalseWhenRoleDoesNotHavePermission() {
        UserInterface guard = makeUser("pooh", "Винни Пух", new model.role.Guard());
        Permission watchJournal = new WatchJournal(List.of());
        boolean ok = service.authorize(guard, watchJournal, new Object(), new SchoolState());
        assertFalse(ok, "Guard must not be allowed to view journal");
    }

    @Test
    void authorizeChecksDirectorAbsentConditionForTeacherEnteringCabin() {
        Location cabin = new DirectorCabin();
        SchoolState context = new SchoolState();

        UserInterface teacher = makeUser("piglet", "Пятачок", new Teacher());
        Permission enterCabin = new EnterCabin(List.of(new DirectorAbsentCondition()));
        assertTrue(service.authorize(teacher, enterCabin, cabin, context));

        UserInterface director = makeUser("owl", "Сова", new Chief());
        cabin.userEnters(director);
        assertFalse(service.authorize(teacher, enterCabin, cabin, context));
    }

    @Test
    void authorizeChecksTeacherPresentConditionForStudentEnteringClass() {
        Location classA = new Classroom(1);
        SchoolState context = new SchoolState();

        UserInterface student = makeUser("roo", "Крошка Ру", new Student());
        Permission enterClass = new EnterClass(List.of(new TeacherPresentCondition()));
        assertFalse(service.authorize(student, enterClass, classA, context));

        UserInterface teacher = makeUser("piglet", "Пятачок", new Teacher());
        classA.userEnters(teacher);
        assertTrue(service.authorize(student, enterClass, classA, context));
    }

    @Test
    void authorizeParentCanViewOnlyOwnChild() {
        SchoolState context = new SchoolState();
        UserInterface parent = User.createUser(
                "kanga", "Кенга", "", "pass123", new Parent(), null, List.of("Крошка Ру")
        );
        UserInterface ownChild = makeUser("roo", "Крошка Ру", new Student());
        UserInterface unknown = makeUser("other", "Другой", new Student());
        Permission watchOwnChild = new WatchJournal(List.of(new OwnChildOnlyCondition()));

        assertTrue(service.authorize(parent, watchOwnChild, ownChild, context),
                "Parent must be allowed to view own child");
        assertFalse(service.authorize(parent, watchOwnChild, unknown, context),
                "Parent must NOT be allowed to view чужого ребёнка");
    }
}
