package model.journal;

import model.user.UserInterface;

import java.util.List;
import java.util.Map;

public interface JournalInterface {
    Map<String, List<Integer>> getGrades(UserInterface student);

    boolean canUserView(UserInterface user, UserInterface targetStudent);
    boolean canUserEdit(UserInterface user);
    void addGrade(UserInterface teacher, UserInterface student, int grade);
}
