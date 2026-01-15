package model.location;

import model.role.Roles;
import model.user.UserInterface;

public class DirectorCabin extends AbstractLocation{
    public DirectorCabin() {
        super(Locations.CABIN, null);
    }

    @Override
    public boolean canUserEnter(UserInterface user) {
        Roles role = user.getRole().getRole();
        return Roles.CHIEF.equals(role) || Roles.ADMIN.equals(role) || Roles.TEACHER.equals(role);
    }
}
