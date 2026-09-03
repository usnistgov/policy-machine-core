/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

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
