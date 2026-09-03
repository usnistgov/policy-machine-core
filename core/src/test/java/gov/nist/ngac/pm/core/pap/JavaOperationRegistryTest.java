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

package gov.nist.ngac.pm.core.pap;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.OperationExistsException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.AdminOperation;
import gov.nist.ngac.pm.core.pap.operation.AdminOperations;
import gov.nist.ngac.pm.core.pap.operation.JavaOperationRegistry;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class JavaOperationRegistryTest {

    private static AdminOperation<Void> testOp(String name) {
        return new AdminOperation<>(name, VOID_TYPE, List.of(), List.of()) {
            @Override
            public Void execute(PAP pap, UserContext userCtx, Args args) throws PMException {
                return null;
            }
        };
    }

    @Test
    void testRegisterAndGetJavaOperation() throws PMException {
        JavaOperationRegistry registry = new JavaOperationRegistry();
        Operation<?> op = testOp("op1");

        registry.register(op);

        assertEquals(op, registry.get("op1"));
    }

    @Test
    void testRegisterThrowsOnDuplicateName() throws PMException {
        JavaOperationRegistry registry = new JavaOperationRegistry();
        registry.register(testOp("op1"));

        assertThrows(OperationExistsException.class, () -> registry.register(testOp("op1")));
    }

    @Test
    void testRegisterThrowsWhenNameConflictsWithProtectedBuiltin() {
        JavaOperationRegistry registry = new JavaOperationRegistry();

        assertThrows(OperationExistsException.class, () -> registry.register(testOp("assign")));
    }

    @Test
    void testGetThrowsForUnregisteredOperation() {
        JavaOperationRegistry registry = new JavaOperationRegistry();

        assertThrows(OperationDoesNotExistException.class, () -> registry.get("nonexistent"));
    }

    @Test
    void testFreshRegistryIsProtectedForEveryAdminBuiltin() {
        JavaOperationRegistry registry = new JavaOperationRegistry();

        for (Operation<?> op : AdminOperations.ADMIN_OPERATIONS) {
            assertTrue(registry.isProtected(op.getName()), "Expected isProtected to be true for: " + op.getName());
        }
    }

    @Test
    void testIsProtectedFalseForFreshlyRegisteredOperation() throws PMException {
        JavaOperationRegistry registry = new JavaOperationRegistry();
        registry.register(testOp("op1"));

        assertFalse(registry.isProtected("op1"));
    }

    @Test
    void testGetProtectedNamesReturnsEveryAdminOperationName() {
        JavaOperationRegistry registry = new JavaOperationRegistry();

        Set<String> expected = AdminOperations.ADMIN_OPERATIONS.stream().map(Operation::getName).collect(Collectors.toSet());

        assertEquals(expected, registry.getProtectedNames());
    }

    @Test
    void testGetProtectedNamesExcludesFreshlyRegisteredOperation() throws PMException {
        JavaOperationRegistry registry = new JavaOperationRegistry();
        registry.register(testOp("op1"));

        assertFalse(registry.getProtectedNames().contains("op1"));
    }

    @Test
    void testGetProtectedOperationsReturnsEveryAdminOperation() {
        JavaOperationRegistry registry = new JavaOperationRegistry();

        Collection<Operation<?>> protectedOperations = registry.getProtectedOperations();

        assertEquals(AdminOperations.ADMIN_OPERATIONS.size(), protectedOperations.size());
        assertTrue(protectedOperations.containsAll(AdminOperations.ADMIN_OPERATIONS));
    }

    @Test
    void testNoPublicPathToMarkRegisteredOperationAsProtected() {
        long publicRegistrationMethods = java.util.Arrays.stream(JavaOperationRegistry.class.getMethods())
            .filter(m -> m.getName().equals("register"))
            .count();

        assertEquals(1, publicRegistrationMethods);
    }
}
