package model.journal;

import model.user.UserInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public abstract class AbstractJournal implements JournalInterface {
    protected final Map<String, List<Integer>> journal = new HashMap<>();

    @Override
    public Map<String, List<Integer>> getGrades(UserInterface student) {
        String studentName = student.getName();
        List<Integer> grades = journal.get(studentName);
        if (grades != null) {
            return Map.of(studentName, List.copyOf(grades));
        } else {
            return Map.of();
        }
    }

    @Override
    public boolean canUserView(UserInterface user, UserInterface targetStudent) {
        return false;
    }

    @Override
    public boolean canUserEdit(UserInterface user) {
        return false;
    }

    @Override
    public void addGrade(UserInterface teacher, UserInterface student, int grade) {
        if (!canUserEdit(teacher)) {
            throw new SecurityException(
                    String.format("User %s cannot edit journal.", teacher.getName())
            );
        }
        String studentName = student.getName();
        journal.putIfAbsent(studentName, new ArrayList<>());
        journal.get(studentName).add(grade);
    }

    @Override
    public String toString() {
        return journal.toString();
    }
}