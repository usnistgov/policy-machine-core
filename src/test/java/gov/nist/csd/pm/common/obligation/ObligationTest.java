package gov.nist.csd.pm.common.obligation;

import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.common.op.graph.AssignOp;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.value.VoidValue;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.util.TestMemoryPAP;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.common.op.graph.GraphOp.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObligationTest {

    @Test
    void testResponseWithExistingFunction() throws PMException {
        String pml = """
                create pc "pc1"
                create oa "oa1" in ["pc1"]
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                
                associate "ua1" and PM_ADMIN_OBJECT with ["create_policy_class"]
                
                create obligation "obl1" {
                    create rule "rule1"
                    when user "u1"
                    performs "assign"
                    on {
                        ascendant: any,
                        descendant: "oa1"
                    }
                    do(ctx) {
                        createX()
                    }
                }
                """;

        TestMemoryPAP pap = getTestMemoryPAP();

        pap.executePML(new UserContext(4), pml);

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        epp.processEvent(
                new EventContext(
                        "u1",
                        null,
                        new AssignOp(),
                        Map.of(ASCENDANT_OPERAND, "o1", DESCENDANTS_OPERAND, List.of("oa1"))
                )
        );
        assertTrue(pap.query().graph().nodeExists("hello world"));
    }

    private static TestMemoryPAP getTestMemoryPAP() throws PMException {
        TestMemoryPAP pap = new TestMemoryPAP();

        pap.setPMLConstants(Map.of("x", new StringValue("hello world")));
        pap.setPMLOperations(new PMLOperation("createX", Type.voidType()) {
            @Override
            public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) {

            }

            @Override
            public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
                ExecutionContext ctx = getCtx();
                pap.executePML(ctx.author(), "create pc x");

                return new VoidValue();
            }
        });
        return pap;
    }
}