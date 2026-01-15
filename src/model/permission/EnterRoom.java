package model.permission;

import model.condition.Condition;

import java.util.List;

public class EnterRoom extends AbstractPermission {
    public EnterRoom(List<Condition> conditions) {
        super(Actions.ENTER, Targets.ROOM, conditions);
    }
}