import model.condition.DirectorAbsentCondition;
import model.condition.OwnChildOnlyCondition;
import model.condition.TeacherPresentCondition;
import model.location.Classroom;
import model.location.DirectorCabin;
import model.location.Location;
import model.role.Chief;
import model.role.Parent;
import model.role.Student;
import model.role.Teacher;
import model.user.User;
import model.user.UserInterface;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConditionsTest {

    private User makeUser(String login, String name, model.role.Role role) {
        return User.createUser(login, name, "", "pass123", role, null);
    }

    @Test
    void directorAbsentConditionTrueWhenDirectorIsNotInCabin() {
        Location cabin = new DirectorCabin();

        DirectorAbsentCondition condition = new DirectorAbsentCondition();
        UserInterface teacher = makeUser("t", "Teacher", new Teacher());
        assertTrue(condition.check(teacher, cabin), "DirectorAbsent must be true for empty cabin");

        UserInterface director = makeUser("owl", "Сова", new Chief());
        cabin.userEnters(director);
        assertFalse(condition.check(teacher, cabin), "DirectorAbsent must be false when director is present");
    }

    @Test
    void teacherPresentConditionTrueOnlyWhenTeacherIsInClass() {
        Location classA = new Classroom(1);

        TeacherPresentCondition condition = new TeacherPresentCondition();
        UserInterface student = makeUser("roo", "Крошка Ру", new Student());
        assertFalse(condition.check(student, classA), "TeacherPresent must be false if no teacher in class");

        UserInterface teacher = makeUser("piglet", "Пятачок", new Teacher());
        classA.userEnters(teacher);
        assertTrue(condition.check(student, classA), "TeacherPresent must be true if teacher is in class");
    }

    @Test
    void ownChildOnlyConditionAllowsOnlyOwnChildGrades() {
        OwnChildOnlyCondition condition = new OwnChildOnlyCondition();

        UserInterface child1 = makeUser("roo", "Крошка Ру", new Student());
        UserInterface child2 = makeUser("other", "Другой", new Student());
        UserInterface parent = User.createUser(
                "kanga", "Кенга", "", "pass123", new Parent(), null, List.of("Крошка Ру")
        );

        assertTrue(condition.check(parent, child1), "Parent must be allowed to access own child");
        assertFalse(condition.check(parent, child2), "Parent must NOT be allowed to access чужого ребёнка");
    }
}
