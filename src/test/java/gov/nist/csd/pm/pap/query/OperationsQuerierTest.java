package gov.nist.csd.pm.pap.query;

import gov.nist.csd.pm.common.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PAPTestInitializer;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.util.SamplePolicy;
import java.util.Map;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public abstract class OperationsQuerierTest extends PAPTestInitializer {

    static Operation<Object, Args> op1 = new Operation<>("op1", List.of()) {

        @Override
        public Object execute(PAP pap, Args args) throws PMException {
            return null;
        }

        @Override
        protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
            return null;
        }

        @Override
        public void canExecute(PAP pap, UserContext userCtx, Args args) throws
                                                                                                     PMException {
        }
    };

    static Operation<Object, Args> op2 = new Operation<>("op2", List.of()) {
        @Override
        public Object execute(PAP pap, Args args) throws PMException {
            return null;
        }

        @Override
        protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
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
            pap.modify().operations().setResourceOperations(arset);
            assertEquals(arset, pap.query().operations().getResourceOperations());
        }
    }

    @Test
    void testGetAdminOperationNames() throws PMException, IOException {
        SamplePolicy.loadSamplePolicyFromPML(pap);

        pap.modify().operations().createAdminOperation(op1);
        pap.modify().operations().createAdminOperation(op2);

        Collection<String> adminOperationNames = pap.query().operations().getAdminOperationNames();
        assertTrue(adminOperationNames.containsAll(Set.of("op1", "op2")));
    }

    @Nested
    class GetAdminOperation {

        @Test
        void testSuccess() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            pap.modify().operations().createAdminOperation(op1);

            Operation<?, ?> actual = pap.query().operations().getAdminOperation(op1.getName());
            assertEquals(op1, actual);
        }

        @Test
        void testOperationDoesNotExist() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            assertThrows(OperationDoesNotExistException.class, () -> pap.query().operations().getAdminOperation("op1"));
        }

    }
}