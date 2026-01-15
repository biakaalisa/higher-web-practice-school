import model.condition.DirectorAbsentCondition;
import model.permission.EnterCabin;
import model.permission.EnterSchool;
import model.permission.Permission;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PermissionEqualityTest {

    @Test
    void permissionsAreEqualByActionAndTargetEvenIfConditionsDiffer() {
        Permission enterCabinNoConditions = new EnterCabin(List.of());
        Permission enterCabinWithCondition = new EnterCabin(List.of(new DirectorAbsentCondition()));

        assertEquals(enterCabinNoConditions, enterCabinWithCondition,
                "EnterCabin permissions must be equal by action+target");
        assertEquals(enterCabinNoConditions.hashCode(), enterCabinWithCondition.hashCode(),
                "Equal permissions must have same hashCode");
    }

    @Test
    void permissionSetContainsNewEquivalentObject() {
        Set<Permission> set = Set.of(
                new EnterSchool(List.of()),
                new EnterCabin(List.of(new DirectorAbsentCondition()))
        );

        assertTrue(set.contains(new EnterSchool(List.of())),
                "Set must contain logically same EnterSchool permission");
        assertTrue(set.contains(new EnterCabin(List.of())),
                "Set must contain logically same EnterCabin permission");
    }
}
