package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.function.routine.Routine;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.bootstrap.PMLBootstrapper;
import gov.nist.csd.pm.util.TestPAP;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PMLBootstrapperTest {

    @Test
    void test() throws PMException {
        PAP pap = new TestPAP();
        PDP pdp = new PDP(pap);

        String input = """
                set resource operations ["read", "write"]
                
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                
                associate "ua1" and "oa1" with ["read"]
                
                assign "u1" to ["ua1"]
                
                op1()
                
                routine1()
                """;

        Operation<?, ?> op1 = new Operation<>("op1", List.of()) {
            @Override
            public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {

            }

            @Override
            public Object execute(PAP pap, Args args) throws PMException {
                pap.modify().graph().createPolicyClass("op1");

                return null;
            }

            @Override
            protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
                return null;
            }
        };

        Routine<?, ?> routine1 = new Routine<>("routine1", List.of()) {
            @Override
            public Object execute(PAP pap, Args actualArgs) throws PMException {
                pap.modify().graph().createPolicyClass("routine1");
                return null;
            }

            @Override
            protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
                return null;
            }
        };

        pdp.bootstrap("u1", new PMLBootstrapper(List.of(op1), List.of(routine1), input));

        assertTrue(pap.query().graph().nodeExists("pc1"));
        assertTrue(pap.query().graph().nodeExists("op1"));
        assertTrue(pap.query().graph().nodeExists("routine1"));
        assertTrue(pap.query().graph().nodeExists("ua1"));
        assertTrue(pap.query().graph().nodeExists("oa1"));
        assertTrue(pap.query().graph().nodeExists("u1"));
    }

}