package model.role;

import model.condition.DirectorAbsentCondition;
import model.permission.*;

import java.util.List;
import java.util.Set;

public class Teacher extends AbstractRole {
    private static final Set<Permission> TEACHER_PERMISSIONS = Set.of(
            new EnterSchool(List.of()),
            new EnterClass(List.of()),
            new EnterCabin(List.of(new DirectorAbsentCondition())),
            new WatchJournal(List.of()),
            new EditJournal(List.of())
    );

    public Teacher() {
        super(Roles.TEACHER, TEACHER_PERMISSIONS);
    }
}