package model.location;

import model.role.Roles;
import model.user.UserInterface;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractLocation implements Location{
    protected final Locations location;
    protected Set<UserInterface> currentUsers;

    public AbstractLocation(Locations location, Set<UserInterface> currentUsers) {
        this.location = location;
        this.currentUsers = new HashSet<>();
    }

    @Override
    public Locations getLocation() {
        return location;
    }

    @Override
    public Set<UserInterface> getCurrentUsers() {
        return currentUsers;
    }

    @Override
    public boolean canUserEnter(UserInterface user) {
        return false;
    }

    @Override
    public void userEnters(UserInterface user) {
        currentUsers.add(user);
    }

    @Override
    public void userLeaves(UserInterface user) {
        currentUsers.remove(user);
    }

    @Override
    public boolean isTeacherPresent() {
        for (UserInterface user : currentUsers) {
            if (Roles.TEACHER.equals(user.getRole().getRole())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isDirectorPresent() {
        for (UserInterface user : currentUsers) {
            if (Roles.CHIEF.equals(user.getRole().getRole())) {
                return true;
            }
        }
        return false;
    }
}
