package model.condition;

import model.location.Location;
import model.user.UserInterface;

public class DirectorPresentCondition extends AbstractCondition {
    public DirectorPresentCondition() {
        super("DIRECTOR_PRESENT", "Allows entry only if the director is present in the cabin.");
    }

    @Override
    protected boolean doCheck(UserInterface user, Object... context) {
        if (context.length > 0 && context[0] instanceof Location) {
            Location location = (Location) context[0];
            return location.isDirectorPresent();
        }
        return false;
    }
}