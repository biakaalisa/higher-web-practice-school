package model.permission;

import model.condition.Condition;

import java.util.List;

public interface Permission {
    Targets getTarget();
    Actions getAction();
    List<Condition> getConditions();

    boolean isConditional();
}
