package model.location;

import model.role.Roles;
import model.user.UserInterface;

public class TeachersRoom extends AbstractLocation {
    public TeachersRoom() {
        super(Locations.ROOM, null);
    }

    @Override
    public boolean canUserEnter(UserInterface user) {
        Roles role = user.getRole().getRole();
        return Roles.TEACHER.equals(role) || Roles.CHIEF.equals(role) || Roles.ADMIN.equals(role);
    }
}