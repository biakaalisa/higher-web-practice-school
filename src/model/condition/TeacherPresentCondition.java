package model.condition;

import model.location.Location;
import model.user.UserInterface;

public class TeacherPresentCondition extends AbstractCondition {
    public TeacherPresentCondition() {
        super("TEACHER_PRESENT", "Requires a teacher to be present in the classroom.");
    }

    @Override
    protected boolean doCheck(UserInterface user, Object... context) {
        if (context.length > 0 && context[0] instanceof Location) {
            Location location = (Location) context[0];
            return location.isTeacherPresent();
        }
        return false;
    }
}