package model.journal;

import model.role.Roles;
import model.user.UserInterface;

public class SchoolJournal extends AbstractJournal {

    @Override
    public boolean canUserView(UserInterface user, UserInterface targetStudent) {
        Roles userRole = user.getRole().getRole();
        if (Roles.PARENT == userRole) {
            return user.getChildrenNames().contains(targetStudent.getName());
        }
        return Roles.TEACHER == userRole || Roles.CHIEF == userRole || Roles.ADMIN == userRole;
    }

    @Override
    public boolean canUserEdit(UserInterface user) {
        Roles userRole = user.getRole().getRole();
        return Roles.TEACHER == userRole || Roles.ADMIN == userRole;
    }
}