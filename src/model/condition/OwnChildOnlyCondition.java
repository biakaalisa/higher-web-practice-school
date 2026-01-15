package model.condition;

import model.user.UserInterface;

public class OwnChildOnlyCondition extends AbstractCondition {
    public OwnChildOnlyCondition() {
        super("OWN_CHILD_ONLY",
                "Allows access only to own child's data.");
    }

    @Override
    protected boolean doCheck(UserInterface user, Object... context) {
        if (context.length > 0 && context[0] instanceof UserInterface) {
            UserInterface targetStudent = (UserInterface) context[0];
            return user.getChildrenNames().contains(targetStudent.getName());
        }
        return false;
    }
}