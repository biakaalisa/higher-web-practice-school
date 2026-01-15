package model.permission;

import model.condition.Condition;

import java.util.List;
import java.util.Objects;

public abstract class AbstractPermission implements Permission{
    protected final Actions action;
    protected final Targets target;
    protected final List<Condition> conditions;

    protected AbstractPermission(Actions action, Targets target ,List<Condition> conditions) {
        this.action = action;
        this.target = target;
        this.conditions = conditions;
    }

    @Override
    public Targets getTarget(){
        return target;
    }

    @Override
    public Actions getAction() {
        return action;
    }

    @Override
    public List<Condition> getConditions() {
        return conditions;
    }

    @Override
    public boolean isConditional() {
        return !conditions.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission)) return false;
        Permission that = (Permission) o;
        return action == that.getAction() && target == that.getTarget();
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, target);
    }

    @Override
    public String toString() {
        return action + "@" + target;
    }
}
