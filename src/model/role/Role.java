package model.role;

import model.permission.Permission;

import java.util.Set;

public interface Role {
    Roles getRole();
    Set<Permission> getPermissions();

    boolean hasPermission (Permission permission);
}
