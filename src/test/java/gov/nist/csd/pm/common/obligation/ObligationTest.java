package gov.nist.csd.pm.common.obligation;

import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.obligation.EventContext;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.op.graph.AssignOp;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.value.VoidValue;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pap.exception.PMException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.op.graph.GraphOp.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObligationTest {

    @Test
    void testResponseWithExistingFunction() throws PMException {
        String pml = "create pc \"pc1\"\n" +
                "                create oa \"oa1\" in [\"pc1\"]\n" +
                "                create ua \"ua1\" in [\"pc1\"]\n" +
                "                create u \"u1\" in [\"ua1\"]\n" +
                "                \n" +
                "                associate \"ua1\" and PM_ADMIN_OBJECT with [\"create_policy_class\"]\n" +
                "                \n" +
                "                create obligation \"obl1\" {\n" +
                "                    create rule \"rule1\"\n" +
                "                    when user \"u1\"\n" +
                "                    performs \"assign\"\n" +
                "                    on {\n" +
                "                        ascendant: any,\n" +
                "                        descendant: \"oa1\"\n" +
                "                    }\n" +
                "                    do(ctx) {\n" +
                "                        createX()\n" +
                "                    }\n" +
                "                }";

        MemoryPAP pap = new MemoryPAP();

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

        pap.executePML(new UserContext("u1"), pml);

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        epp.getEventProcessor().processEvent(
                new EventContext(
                        "u1",
                        null,
                        new AssignOp(),
                        Map.of(ASCENDANT_OPERAND, "o1", DESCENDANTS_OPERAND, List.of("oa1"))
                )
        );
        assertTrue(pap.query().graph().nodeExists("hello world"));
    }
}