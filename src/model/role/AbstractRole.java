package model.role;

import model.permission.Permission;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractRole implements Role{
    protected Roles role;
    protected Set<Permission> permissions = new HashSet<>();

    public AbstractRole(Roles role, Set<Permission> permissions) {
        this.role = role;
        this.permissions = permissions;
    }

    @Override
    public Roles getRole() {
        return role;
    }

    @Override
    public Set<Permission> getPermissions() {
        return permissions;
    }

    @Override
    public boolean hasPermission(Permission permission){
        return permissions.contains(permission);
    }

}
