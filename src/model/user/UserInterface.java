package model.user;

import model.location.Location;
import model.role.Role;

import java.util.List;

public interface UserInterface {
    String getLogin();
    String getName();
    String getIcon();
    Role getRole();
    Location getCurrentLocation();
    List<String> getChildrenNames();

    void setCurrentLocation(Location location);
    boolean checkPassword(String password);
}