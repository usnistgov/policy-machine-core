package gov.nist.csd.pm.core.pdp;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.VOID_TYPE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.DisconnectedNodeException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.AdminOperation;
import gov.nist.csd.pm.core.pap.function.Routine;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.bootstrap.PMLBootstrapper;
import gov.nist.csd.pm.core.util.TestPAP;
import java.util.List;
import org.junit.jupiter.api.Test;

class PMLBootstrapperTest {

    @Test
    void test() throws PMException {
        PAP pap = new TestPAP();
        PDP pdp = new PDP(pap);

        String input = """
                set resource access rights ["read", "write"]
                
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                
                associate "ua1" and "oa1" with ["read"]
                
                assign "u1" to ["ua1"]
                
                op1()
                
                routine1()
                """;

        AdminOperation<?> op1 = new AdminOperation<>("op1", VOID_TYPE, List.of()) {
            @Override
            public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {

            }

            @Override
            public Void execute(PAP pap, Args args) throws PMException {
                pap.modify().graph().createPolicyClass("op1");

                return null;
            }

        };

        Routine<?> routine1 = new Routine<>("routine1", VOID_TYPE, List.of()) {
            @Override
            public Void execute(PAP pap, Args actualArgs) throws PMException {
                pap.modify().graph().createPolicyClass("routine1");
                return null;
            }

        };

        pap.plugins().registerAdminOperation(pap.query().operations(), op1);
        pap.plugins().registerRoutine(pap.query().operations(), routine1);

        pdp.bootstrap(new PMLBootstrapper("u1", input));

        assertTrue(pap.query().graph().nodeExists("pc1"));
        assertTrue(pap.query().graph().nodeExists("op1"));
        assertTrue(pap.query().graph().nodeExists("routine1"));
        assertTrue(pap.query().graph().nodeExists("ua1"));
        assertTrue(pap.query().graph().nodeExists("oa1"));
        assertTrue(pap.query().graph().nodeExists("u1"));
    }

    @Test
    void testBootstrapThrowsExceptionWhenUserNotAssigned() throws PMException {
        PAP pap = new TestPAP();
        PDP pdp = new PDP(pap);

        assertThrows(DisconnectedNodeException.class, () -> pdp.bootstrap(new PMLBootstrapper(
            "u1", "create pc \"pc1\""
        )));
    }

}