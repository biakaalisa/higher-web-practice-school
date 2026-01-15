package model.role;

import model.condition.OwnChildOnlyCondition;
import model.permission.EnterSchool;
import model.permission.Permission;
import model.permission.WatchJournal;

import java.util.List;
import java.util.Set;

public class Parent extends AbstractRole {
    private static final Set<Permission> PARENT_PERMISSIONS = Set.of(
            new EnterSchool(List.of()),
            new WatchJournal(List.of(new OwnChildOnlyCondition()))
    );

    public Parent() {
        super(Roles.PARENT, PARENT_PERMISSIONS);
    }
}