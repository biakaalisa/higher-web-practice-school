package model.role;

import model.permission.*;

import java.util.List;
import java.util.Set;

public class Admin extends AbstractRole {
    private static final Set<Permission> ADMIN_PERMISSIONS = Set.of(
            new EnterSchool(List.of()),
            new EnterClass(List.of()),
            new EnterCabin(List.of()),
            new WatchJournal(List.of()),
            new EditJournal(List.of())
    );

    public Admin() {
        super(Roles.ADMIN, ADMIN_PERMISSIONS);
    }
}