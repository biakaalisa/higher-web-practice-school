package model.permission;

import model.condition.Condition;

import java.util.List;

public class EnterSchool extends AbstractPermission {
    public EnterSchool(List<Condition> conditions) {
        super(Actions.ENTER, Targets.SCHOOL, conditions);
    }
}