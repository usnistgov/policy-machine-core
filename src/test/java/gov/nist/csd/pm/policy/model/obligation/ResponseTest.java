package gov.nist.csd.pm.policy.model.obligation;

import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.epp.EventContext;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.expression.reference.ReferenceByID;
import gov.nist.csd.pm.policy.pml.statement.CreatePolicyStatement;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.value.StringValue;
import gov.nist.csd.pm.policy.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.policy.events.graph.AssignToEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {

    @Test
    void testResponseExecutionWithUserDefinedAndBuiltinPML() throws PMException {
        String pml = "create pc \"pc1\"\n" +
                "                create oa \"oa1\" assign to [\"pc1\"]\n" +
                "                create ua \"ua1\" assign to [\"pc1\"]\n" +
                "                create u \"u1\" assign to [\"ua1\"]\n" +
                "                \n" +
                "                associate \"ua1\" and POLICY_CLASS_TARGETS with [create_policy_class]\n" +
                "                const x = \"hello world\"\n" +
                "                function createX() {\n" +
                "                    create policy class x\n" +
                "                }\n" +
                "                \n" +
                "                create obligation \"obl1\" {\n" +
                "                    create rule \"rule1\"\n" +
                "                    when any user\n" +
                "                    performs [\"assign_to\"]\n" +
                "                    on [\"oa1\"]\n" +
                "                    do(ctx) {\n" +
                "                        createX()\n" +
                "                    }\n" +
                "                }";
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());
        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        epp.getEventProcessor().processEvent(new EventContext(new UserContext("u1"), "oa1", new AssignToEvent("o1", "oa1")));
        assertTrue(pap.graph().nodeExists("hello world"));
    }

    @Test
    void testResponseWithExistingFunction() throws PMException {
        String pml =
                "create pc \"pc1\"\n" +
                "create oa \"oa1\" assign to [\"pc1\"]\n" +
                "create ua \"ua1\" assign to [\"pc1\"]\n" +
                "create u \"u1\" assign to [\"ua1\"]\n" +
                "\n" +
                "associate \"ua1\" and POLICY_CLASS_TARGETS with [create_policy_class]\n" +
                "\n" +
                "create obligation \"obl1\" {\n" +
                "    create rule \"rule1\"\n" +
                "    when any user\n" +
                "    performs [\"assign_to\"]\n" +
                "    on [\"oa1\"]\n" +
                "    do(ctx) {\n" +
                "        createX()\n" +
                "    }\n" +
                "}";

        PAP pap = new PAP(new MemoryPolicyStore());

        pap.userDefinedPML().createConstant("x", new StringValue("hello world"));
        pap.userDefinedPML().createFunction(new FunctionDefinitionStatement.Builder("createX")
                                                    .body(List.of(new CreatePolicyStatement(new ReferenceByID("x"))))
                                                    .build());
        pap.executePML(new UserContext("u1"), pml);

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        epp.getEventProcessor().processEvent(new EventContext(new UserContext("u1"), "oa1", new AssignToEvent("o1", "oa1")));
        assertTrue(pap.graph().nodeExists("hello world"));
    }
}