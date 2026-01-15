package model.role;

import model.condition.TeacherPresentCondition;
import model.permission.EnterClass;
import model.permission.EnterSchool;
import model.permission.Permission;
import model.permission.WatchJournal;

import java.util.List;
import java.util.Set;

public class Student extends AbstractRole {
    private static final Set<Permission> STUDENT_PERMISSIONS = Set.of(
            new EnterSchool(List.of()),
            new EnterClass(List.of(new TeacherPresentCondition())),
            new WatchJournal(List.of(new TeacherPresentCondition()))
    );

    public Student() {
        super(Roles.STUDENT, STUDENT_PERMISSIONS);
    }
}