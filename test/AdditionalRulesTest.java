import model.journal.SchoolJournal;
import model.location.Classroom;
import model.location.DirectorCabin;
import model.location.TeachersRoom;
import model.permission.EnterCabin;
import model.permission.EnterClass;
import model.permission.EnterRoom;
import model.role.Chief;
import model.role.Parent;
import model.role.Student;
import model.role.Teacher;
import model.user.User;
import org.junit.jupiter.api.Test;
import service.auth.AuthorizationService;
import service.state.SchoolState;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AdditionalRulesTest {

    @Test
    void parentCannotEnterAnyRoomsOrClasses() {
        AuthorizationService auth = new AuthorizationService();
        SchoolState state = new SchoolState();

        User parent = User.createUser(
                "kanga", "Kanga", "🐨", "pass123",
                new Parent(), null, List.of("Roo")
        );

        Classroom classroom = new Classroom(1);
        TeachersRoom teachersRoom = new TeachersRoom();
        DirectorCabin directorCabin = new DirectorCabin();

        assertFalse(auth.authorize(parent, new EnterClass(List.of()), classroom, state),
                "Parent must NOT enter classrooms by TЗ");
        assertFalse(auth.authorize(parent, new EnterRoom(List.of()), teachersRoom, state),
                "Parent must NOT enter teachers room by TЗ");
        assertFalse(auth.authorize(parent, new EnterCabin(List.of()), directorCabin, state),
                "Parent must NOT enter director cabin by TЗ");
    }

    @Test
    void teacherCanEditJournalAndGradeIsSaved() {
        SchoolJournal journal = new SchoolJournal();

        User teacher = User.createUser(
                "piglet", "Piglet", "🐷", "pass123",
                new Teacher(), null
        );
        User student = User.createUser(
                "roo", "Roo", "🦘", "pass123",
                new Student(), null
        );

        assertDoesNotThrow(() -> journal.addGrade(teacher, student, 5),
                "Teacher must be able to add grades (edit journal)");

        assertEquals(List.of(5), journal.getGrades(student).get("Roo"),
                "Grade must be saved in the journal");
    }

    @Test
    void chiefCanViewAnyStudentJournal() {
        SchoolJournal journal = new SchoolJournal();

        User chief = User.createUser(
                "owl", "Owl", "🦉", "pass123",
                new Chief(), null
        );
        User student = User.createUser(
                "roo", "Roo", "🦘", "pass123",
                new Student(), null
        );

        assertTrue(journal.canUserView(chief, student),
                "Chief must be able to view any student's journal by TЗ");
    }
}
