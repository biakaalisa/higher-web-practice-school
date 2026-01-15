package model.permission;

import model.condition.Condition;

import java.util.List;

public class EnterClass extends AbstractPermission {
    public EnterClass(List<Condition> conditions) {
        super(Actions.ENTER, Targets.CLASS, conditions);
    }
}