package gov.nist.csd.pm.pap.query;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PAPTestInitializer;
import gov.nist.csd.pm.pap.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.util.SamplePolicy;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public abstract class OperationsQuerierTest extends PAPTestInitializer {

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

        pap.modify().operations().createAdminOperation(new Operation<Object>("op1", List.of()) {
            @Override
            public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
                
            }

            @Override
            public Object execute(PAP pap, Map<String, Object> operands) throws PMException {
                return null;
            }
        });

        pap.modify().operations().createAdminOperation(new Operation<Object>("op2", List.of()) {
            @Override
            public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
                
            }

            @Override
            public Object execute(PAP pap, Map<String, Object> operands) throws PMException {
                return null;
            }
        });

        Collection<String> adminOperationNames = pap.query().operations().getAdminOperationNames();
        assertTrue(adminOperationNames.containsAll(Set.of("op1", "op2")));
    }

    @Nested
    class GetAdminOperation {

        @Test
        void testSuccess() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            Operation<Object> operation = new Operation<Object>("op1", List.of()) {
                @Override
                public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
                    
                }

                @Override
                public Object execute(PAP pap, Map<String, Object> operands) throws PMException {
                    return null;
                }
            };

            pap.modify().operations().createAdminOperation(operation);

            Operation<?> actual = pap.query().operations().getAdminOperation(operation.getName());
            assertEquals(operation, actual);
        }

        @Test
        void testOperationDoesNotExist() throws PMException, IOException {
            SamplePolicy.loadSamplePolicyFromPML(pap);

            assertThrows(OperationDoesNotExistException.class, () -> pap.query().operations().getAdminOperation("op1"));
        }

    }
}