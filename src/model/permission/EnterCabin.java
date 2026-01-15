package model.permission;

import model.condition.Condition;

import java.util.List;

public class EnterCabin extends AbstractPermission {
    public EnterCabin(List<Condition> conditions) {
        super(Actions.ENTER, Targets.CABIN, conditions);
    }
}