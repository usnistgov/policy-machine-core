package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.op.Operation;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperationWrapper;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLRoutineWrapper;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.common.routine.Routine;
import gov.nist.csd.pm.pdp.bootstrap.PMLBootstrapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PMLBootstrapperTest {

    @Test
    void test() throws PMException {
        PAP pap = new MemoryPAP();
        PDP pdp = new PDP(pap);

        String input = """
                set resource operations ["read", "write"]
                
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                
                associate "ua1" and "oa1" with ["read"]
                
                create user "u1" in ["ua1"]
                
                op1()
                
                routine1()
                
                create pc TEST_CONST
                """;

        Operation<?> op1 = new Operation<>("op1") {
            @Override
            public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map operands) throws PMException {

            }

            @Override
            public Object execute(PAP pap, Map operands) throws PMException {
                pap.modify().graph().createPolicyClass("op1");

                return null;
            }
        };

        Routine<Object> routine1 = new Routine<>("routine1", List.of()) {
            @Override
            public Object execute(PAP pap, Map<String, Object> operands) throws PMException {
                pap.modify().graph().createPolicyClass("routine1");
                return null;
            }
        };

        pdp.bootstrap(new PMLBootstrapper(new UserContext("u1"), input,
                List.of(new PMLOperationWrapper(op1)),
                List.of(new PMLRoutineWrapper(routine1)),
                Map.of("TEST_CONST", new StringValue("TEST_PC"))
        ));

        assertTrue(pap.query().graph().nodeExists("pc1"));
        assertTrue(pap.query().graph().nodeExists("op1"));
        assertTrue(pap.query().graph().nodeExists("routine1"));
        assertTrue(pap.query().graph().nodeExists("TEST_PC"));
        assertTrue(pap.query().graph().nodeExists("ua1"));
        assertTrue(pap.query().graph().nodeExists("oa1"));
        assertTrue(pap.query().graph().nodeExists("u1"));
    }

}