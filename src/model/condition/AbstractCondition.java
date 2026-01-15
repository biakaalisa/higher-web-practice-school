package model.condition;

import model.user.UserInterface;

public abstract class AbstractCondition implements Condition {
    protected final String conditionCode;
    protected final String description;

    protected AbstractCondition(String conditionCode, String description) {
        this.conditionCode = conditionCode;
        this.description = description;
    }

    @Override
    public String getConditionCode() {
        return conditionCode;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean check(UserInterface user, Object... context) {
        return doCheck(user, context);
    }

    protected abstract boolean doCheck(UserInterface user, Object... context);
}