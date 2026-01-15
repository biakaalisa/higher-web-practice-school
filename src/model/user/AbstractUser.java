package model.user;

import model.location.Location;
import model.role.Role;
import model.role.Roles;

import java.util.List;
import java.util.Objects;

public abstract class AbstractUser implements UserInterface {
    protected final String login;
    protected final String name;
    protected final String icon;
    protected final String password;
    protected Role role;
    protected Location currentLocation;
    protected final List<String> childrenNames;

    protected AbstractUser(String login, String name, String icon, String password,
                           Role role, Location currentLocation, List<String> childrenNames) {

        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("login must not be empty");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be empty");
        }
        if (password == null) {
            throw new IllegalArgumentException("password must not be null");
        }
        if (role == null) {
            throw new IllegalArgumentException("role must not be null");
        }

        List<String> safeChildren = childrenNames != null ? childrenNames : List.of();
        if (!safeChildren.isEmpty() && role.getRole() != Roles.PARENT) {
            throw new IllegalArgumentException("Only users with PARENT role can have children");
        }

        for (String child : safeChildren) {
            if (child == null) continue;

            if (child.equalsIgnoreCase(login) || child.equalsIgnoreCase(name)) {
                throw new IllegalArgumentException("User cannot be parent of himself");
            }
        }

        this.login = login;
        this.name = name;
        this.icon = icon;
        this.password = password;
        this.role = role;
        this.currentLocation = currentLocation;
        this.childrenNames = safeChildren;
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public Location getCurrentLocation() {
        return currentLocation;
    }

    @Override
    public List<String> getChildrenNames() {
        return childrenNames != null ? childrenNames : List.of();
    }

    @Override
    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    @Override
    public boolean checkPassword(String enteredPassword) {
        return password.equals(enteredPassword);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserInterface)) return false;
        UserInterface that = (UserInterface) o;
        return Objects.equals(getLogin(), that.getLogin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLogin());
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", role=" + role.getRole() +
                '}';
    }
}
