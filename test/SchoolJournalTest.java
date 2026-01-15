import model.journal.SchoolJournal;
import model.role.Admin;
import model.role.Parent;
import model.role.Student;
import model.role.Teacher;
import model.user.User;
import model.user.UserInterface;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SchoolJournalTest {

    private User makeUser(String login, String name, model.role.Role role) {
        return User.createUser(login, name, "", "pass123", role, null);
    }

    @Test
    void teacherAndAdminCanEditJournalButParentCannot() {
        SchoolJournal journal = new SchoolJournal();

        UserInterface teacher = makeUser("piglet", "Пятачок", new Teacher());
        UserInterface admin = makeUser("robin", "Кристофер Робин", new Admin());
        UserInterface parent = User.createUser(
                "kanga", "Кенга", "", "pass123", new Parent(), null, List.of("Крошка Ру")
        );

        assertTrue(journal.canUserEdit(teacher));
        assertTrue(journal.canUserEdit(admin));
        assertFalse(journal.canUserEdit(parent));
    }

    @Test
    void addGradeThrowsSecurityExceptionWhenUserCannotEdit() {
        SchoolJournal journal = new SchoolJournal();

        UserInterface parent = User.createUser(
                "kanga", "Кенга", "", "pass123", new Parent(),
                null, List.of("Крошка Ру")
        );
        UserInterface student = makeUser("roo", "Крошка Ру", new Student());

        assertThrows(SecurityException.class, () -> journal.addGrade(parent, student, 5));
    }

    @Test
    void parentCanViewOnlyOwnChildTeacherCanViewAnyone() {
        SchoolJournal journal = new SchoolJournal();

        UserInterface parent = User.createUser(
                "kanga", "Кенга", "", "pass123", new Parent(),
                null, List.of("Крошка Ру")
        );
        UserInterface ownChild = makeUser("roo", "Крошка Ру", new Student());
        UserInterface otherChild = makeUser("other", "Другой", new Student());

        UserInterface teacher = makeUser("piglet", "Пятачок", new Teacher());

        assertTrue(journal.canUserView(parent, ownChild));
        assertFalse(journal.canUserView(parent, otherChild));

        assertTrue(journal.canUserView(teacher, ownChild));
        assertTrue(journal.canUserView(teacher, otherChild));
    }
}
