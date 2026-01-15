package model.permission;

import model.condition.Condition;

import java.util.List;

public class EditJournal extends AbstractPermission {
    public EditJournal(List<Condition> conditions) {
        super(Actions.EDIT, Targets.JOURNAL, conditions);
    }
}