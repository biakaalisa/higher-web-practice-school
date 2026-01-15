package model.condition;

import model.location.Location;
import model.user.UserInterface;

public class DirectorAbsentCondition extends AbstractCondition {

    public DirectorAbsentCondition() {
        super("DIRECTOR_ABSENT", "Director must be absent from the cabin");
    }

    @Override
    protected boolean doCheck(UserInterface user, Object... context) {
        if (context.length > 0 && context[0] instanceof Location) {
            Location location = (Location) context[0];
            return !location.isDirectorPresent();
        }
        return false;
    }
}
