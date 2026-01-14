package gov.nist.csd.pm.core.pap.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.PAPTestInitializer;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.AdminOperation;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.SamplePolicy;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public abstract class OperationsQuerierTest extends PAPTestInitializer {

    static AdminOperation<Object> op1 = new AdminOperation<>("op1", List.of()) {

        @Override
        public Object execute(PAP pap, Args args) throws PMException {
            return null;
        }

        @Override
        public void canExecute(PAP pap, UserContext userCtx, Args args) throws
                                                                                                     PMException {
        }
    };

    static AdminOperation<Object> op2 = new AdminOperation<>("op2", List.of()) {
        @Override
        public Object execute(PAP pap, Args args) throws PMException {
            return null;
        }

        @Override
        public void canExecute(PAP pap, UserContext userCtx, Args args) throws
                                                                                                     PMException {

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

        pap.modify().operations().createAdminOperation(op1);
        pap.modify().operations().createAdminOperation(op2);

        Collection<String> adminOperationNames = pap.query().operations().getAdminOperationNames();
        assertTrue(adminOperationNames.containsAll(Set.of("op1", "op2")));

        pap.plugins().registerOperation(new AdminOperation<>("op3", List.of()) {
            @Override
            public Object execute(PAP pap, Args args) throws PMException {
                return null;
            }

            @Override
            public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {

            }
        });

        adminOperationNames = pap.query().operations().getAdminOperationNames();
        assertTrue(adminOperationNames.containsAll(Set.of("op1", "op2", "op3")));
    }

    @Nested
    class GetAdminOperation {

        @Test
        void testSuccess() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.modify().operations().createAdminOperation(op1);

            Operation<?> actual = pap.query().operations().getAdminOperation(op1.getName());
            assertEquals(op1, actual);

            pap.plugins().registerOperation(op2);
            actual = pap.query().operations().getAdminOperation(op2.getName());
            assertEquals(op2, actual);
        }

        @Test
        void testOperationDoesNotExist() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            assertThrows(OperationDoesNotExistException.class, () -> pap.query().operations().getAdminOperation("op1"));
        }

    }
}