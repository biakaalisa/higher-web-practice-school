package model.location;

import model.user.UserInterface;

import java.util.Set;

public interface Location {
    Locations getLocation();
    Set<UserInterface> getCurrentUsers();

    boolean canUserEnter(UserInterface user);
    void userEnters(UserInterface user);
    void userLeaves(UserInterface user);
    boolean isTeacherPresent();
    boolean isDirectorPresent();
}
