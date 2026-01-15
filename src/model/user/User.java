package model.user;

import model.location.Location;
import model.role.Role;

import java.util.List;

public class User extends AbstractUser {

    public User(String login, String name, String icon, String password, Role role, Location currentLocation,
                List<String> childrenNames) {
        super(login, name, icon, password, role, currentLocation, childrenNames);
    }

    public static User createUser(String login, String name, String icon, String password, Role role,
                                  Location currentLocation, List<String> childrenNames) {
        return new User(login, name, icon, password, role, currentLocation, childrenNames);
    }

    public static User createUser(String login, String name, String icon, String password, Role role,
                                  Location currentLocation) {
        return new User(login, name, icon, password, role, currentLocation, null);
    }
}