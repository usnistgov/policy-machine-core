package gov.nist.csd.pm.core.pap;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.OperationExistsException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.AdminOperations;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class NativeOperationRegistryTest {

    private static AdminOperation<Void> testOp(String name) {
        return new AdminOperation<>(name, VOID_TYPE, List.of(), List.of()) {
            @Override
            public Void execute(PAP pap, UserContext userCtx, Args args) throws PMException {
                return null;
            }
        };
    }

    @Test
    void testRegisterAndGetNativeOperation() throws PMException {
        NativeOperationRegistry registry = new NativeOperationRegistry();
        Operation<?> op = testOp("op1");

        registry.register(op);

        assertEquals(op, registry.get("op1"));
    }

    @Test
    void testRegisterThrowsOnDuplicateName() throws PMException {
        NativeOperationRegistry registry = new NativeOperationRegistry();
        registry.register(testOp("op1"));

        assertThrows(OperationExistsException.class, () -> registry.register(testOp("op1")));
    }

    @Test
    void testRegisterThrowsWhenNameConflictsWithProtectedBuiltin() {
        NativeOperationRegistry registry = new NativeOperationRegistry();

        assertThrows(OperationExistsException.class, () -> registry.register(testOp("assign")));
    }

    @Test
    void testGetThrowsForUnregisteredOperation() {
        NativeOperationRegistry registry = new NativeOperationRegistry();

        assertThrows(OperationDoesNotExistException.class, () -> registry.get("nonexistent"));
    }

    @Test
    void testFreshRegistryIsProtectedForEveryAdminBuiltin() {
        NativeOperationRegistry registry = new NativeOperationRegistry();

        for (Operation<?> op : AdminOperations.ADMIN_OPERATIONS) {
            assertTrue(registry.isProtected(op.getName()), "Expected isProtected to be true for: " + op.getName());
        }
    }

    @Test
    void testIsProtectedFalseForFreshlyRegisteredOperation() throws PMException {
        NativeOperationRegistry registry = new NativeOperationRegistry();
        registry.register(testOp("op1"));

        assertFalse(registry.isProtected("op1"));
    }

    @Test
    void testGetProtectedNamesReturnsEveryAdminOperationName() {
        NativeOperationRegistry registry = new NativeOperationRegistry();

        Set<String> expected = AdminOperations.ADMIN_OPERATIONS.stream().map(Operation::getName).collect(Collectors.toSet());

        assertEquals(expected, registry.getProtectedNames());
    }

    @Test
    void testGetProtectedNamesExcludesFreshlyRegisteredOperation() throws PMException {
        NativeOperationRegistry registry = new NativeOperationRegistry();
        registry.register(testOp("op1"));

        assertFalse(registry.getProtectedNames().contains("op1"));
    }

    @Test
    void testGetProtectedOperationsReturnsEveryAdminOperation() {
        NativeOperationRegistry registry = new NativeOperationRegistry();

        Collection<Operation<?>> protectedOperations = registry.getProtectedOperations();

        assertEquals(AdminOperations.ADMIN_OPERATIONS.size(), protectedOperations.size());
        assertTrue(protectedOperations.containsAll(AdminOperations.ADMIN_OPERATIONS));
    }

    @Test
    void testNoPublicPathToMarkRegisteredOperationAsProtected() {
        long publicRegistrationMethods = java.util.Arrays.stream(NativeOperationRegistry.class.getMethods())
            .filter(m -> m.getName().equals("register"))
            .count();

        assertEquals(1, publicRegistrationMethods);
    }
}
