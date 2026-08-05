package gov.nist.ngac.pm.core.pap.operation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class AdminOperationsTest {

    @Test
    void testRegistryContains19Operations() {
        assertEquals(19, AdminOperations.ADMIN_OPERATIONS.size());
    }

    @Test
    void testRegistryContainsExpectedNames() {
        Set<String> expectedNames = Set.of(
                "assign", "associate", "create_object_attribute", "create_object",
                "create_policy_class", "create_user_attribute", "create_user",
                "deassign", "delete_node", "dissociate", "set_node_properties",
                "create_obligation", "delete_obligation",
                "set_resource_access_rights", "create_operation", "delete_operation",
                "create_node_prohibition", "create_process_prohibition", "delete_prohibition"
        );

        Set<String> actualNames = AdminOperations.ADMIN_OPERATIONS.stream()
                .map(Operation::getName)
                .collect(Collectors.toSet());

        assertTrue(actualNames.containsAll(expectedNames));
        assertEquals(expectedNames.size(), actualNames.size());
    }
}
