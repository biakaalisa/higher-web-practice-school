package service.auth;

import model.condition.Condition;
import model.permission.Permission;
import model.user.User;
import model.user.UserInterface;
import service.state.SchoolState;

public class AuthorizationService {
    public boolean authorize(UserInterface user, Permission permission, Object resource, SchoolState context) {
        if (!user.getRole().hasPermission(permission)) {
            return false;
        }

        if (permission.isConditional()) {
            for (Condition condition : permission.getConditions()) {
                if (!condition.check(user, resource, context)) {
                    return false;
                }
            }
        }

        return true;
    }
}