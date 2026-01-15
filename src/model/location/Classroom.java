package model.location;

import model.role.Roles;
import model.user.UserInterface;

public class Classroom extends AbstractLocation {
    private int classNumber;

    public Classroom(int classNumber) {
        super(Locations.CLASS, null);
        this.classNumber = classNumber;
    }

    public int getClassNumber() {
        return classNumber;
    }

    @Override
    public boolean canUserEnter(UserInterface user) {
        Roles role = user.getRole().getRole();
        return role == Roles.TEACHER || role == Roles.STUDENT || role == Roles.ADMIN || role == Roles.CHIEF;
    }
}