package model.location;

import model.user.UserInterface;

public class School extends AbstractLocation {
    public School() {
        super(Locations.SCHOOL, null);
    }

    @Override
    public boolean canUserEnter(UserInterface user) {
        return true;
    }
}