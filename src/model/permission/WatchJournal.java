package model.permission;

import model.condition.Condition;

import java.util.List;

public class WatchJournal extends AbstractPermission {
    public WatchJournal(List<Condition> conditions) {
        super(Actions.WATCH, Targets.JOURNAL, conditions);
    }
}