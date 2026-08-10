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
