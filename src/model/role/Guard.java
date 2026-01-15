package model.role;

import model.permission.EnterSchool;
import model.permission.Permission;

import java.util.List;
import java.util.Set;

public class Guard extends AbstractRole {

    private static final Set<Permission> GUARD_PERMISSIONS = Set.of(
            new EnterSchool(List.of())
        );

    public Guard() {
        super(Roles.GUARD, GUARD_PERMISSIONS);
    }
}