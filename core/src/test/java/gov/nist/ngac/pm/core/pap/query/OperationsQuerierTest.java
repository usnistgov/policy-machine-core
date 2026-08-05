package gov.nist.ngac.pm.core.pap.query;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.PAPTestInitializer;
import gov.nist.ngac.pm.core.pap.operation.AdminOperation;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperation;
import gov.nist.ngac.pm.core.util.SamplePolicy;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public abstract class OperationsQuerierTest extends PAPTestInitializer {

    static AdminOperation<Void> op1 = new AdminOperation<>("op1", VOID_TYPE, List.of(), List.of()) {

        @Override
        public Void execute(PAP pap, UserContext userCtx, Args args) throws PMException {
            return null;
        }
    };

    static AdminOperation<Void> op2 = new AdminOperation<>("op2", VOID_TYPE, List.of(), List.of()) {
        @Override
        public Void execute(PAP pap, UserContext userCtx, Args args) throws PMException {
            return null;
        }
    };

    // must be static: an anonymous class defined inside a non-static test method captures the
    // enclosing test instance, which breaks Neo4j's Java-serialization write path
    static AdminOperation<Void> op3 = new AdminOperation<>("op3", VOID_TYPE, List.of(), List.of()) {
        @Override
        public Void execute(PAP pap, UserContext userCtx, Args args) throws PMException {
            return null;
        }
    };

    @Nested
    class GetResourceAccessRights {
        @Test
        void testGetResourceAccessRights() throws PMException {
            AccessRightSet arset = new AccessRightSet("read", "write");
            pap.modify().operations().setResourceAccessRights(arset);
            assertEquals(arset, pap.query().operations().getResourceAccessRights());
            arset = new AccessRightSet("read", "write", "execute");
            pap.modify().operations().setResourceAccessRights(arset);
            assertEquals(arset, pap.query().operations().getResourceAccessRights());
        }
    }

    @Test
    void testGetAdminOperationNames() throws PMException, IOException {
        SamplePolicy.loadSamplePolicyFromPML(pap);

        pap.nativeOperations().register(op1);
        pap.nativeOperations().register(op2);
        pap.modify().operations().createOperation(op1);
        pap.modify().operations().createOperation(op2);

        Collection<String> adminOperationNames = pap.query().operations().getOperationNames();
        assertTrue(adminOperationNames.containsAll(Set.of("op1", "op2")));

        pap.nativeOperations().register(op3);
        pap.modify().operations().createOperation(op3);

        adminOperationNames = pap.query().operations().getOperationNames();
        assertTrue(adminOperationNames.containsAll(Set.of("op1", "op2", "op3")));
    }

    @Nested
    class GetAdminOperation {

        @Test
        void testSuccess() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.nativeOperations().register(op1);
            pap.modify().operations().createOperation(op1);

            Operation<?> actual = pap.query().operations().getOperation(op1.getName());
            assertEquals(op1, actual);

            pap.nativeOperations().register(op2);
            pap.modify().operations().createOperation(op2);
            actual = pap.query().operations().getOperation(op2.getName());
            assertEquals(op2, actual);
        }

        @Test
        void testOperationDoesNotExist() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            assertThrows(OperationDoesNotExistException.class, () -> pap.query().operations().getOperation("op1"));
        }

    }

    @Test
    void testBulkListingMixesNativeAndPmlAndProtectedOperations() throws PMException, IOException {
        SamplePolicy.loadSamplePolicyFromPML(pap);

        pap.nativeOperations().register(op1);
        pap.modify().operations().createOperation(op1);
        pap.executePML(NodeUserContext.of("u1"), "adminop pml_op() { }");

        Collection<Operation<?>> operations = pap.query().operations().getOperations();
        Set<String> names = operations.stream().map(Operation::getName).collect(java.util.stream.Collectors.toSet());

        assertTrue(names.contains("op1"), "expected the user-registered native operation in the bulk listing");
        assertTrue(names.contains("pml_op"), "expected the PML-defined operation in the bulk listing");
        assertTrue(names.contains("assign"), "expected a protected built-in in the bulk listing, resolved without ever being createOperation'd");

        Operation<?> resolvedNative = operations.stream().filter(o -> o.getName().equals("op1")).findFirst().orElseThrow();
        Operation<?> resolvedPml = operations.stream().filter(o -> o.getName().equals("pml_op")).findFirst().orElseThrow();

        assertEquals(op1, resolvedNative);
        assertInstanceOf(PMLOperation.class, resolvedPml);
    }
}