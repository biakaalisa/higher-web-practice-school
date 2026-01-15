package model.condition;

import model.user.UserInterface;

public interface Condition {
    String getConditionCode();

    boolean check(UserInterface user, Object... context);
    String getDescription();
}
