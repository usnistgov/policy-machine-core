package gov.nist.csd.pm.pap.pml.statement.operation;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.obligation.EventPattern;
import gov.nist.csd.pm.common.obligation.Response;
import gov.nist.csd.pm.common.obligation.Rule;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.pap.pml.pattern.subject.SubjectPattern;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import org.junit.jupiter.api.Test;

import java.util.*;

import static gov.nist.csd.pm.util.TestMemoryPAP.id;
import static gov.nist.csd.pm.util.TestMemoryPAP.ids;
import static org.junit.jupiter.api.Assertions.*;

class DeleteRuleStatementTest {

    @Test
    void testSuccess() throws PMException {
        DeleteRuleStatement stmt = new DeleteRuleStatement(
                new StringLiteral("rule1"), new StringLiteral("obl1"));

        PAP pap = new MemoryPAP();
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", ids(pap, "pc1"));
        pap.modify().graph().createUser("u1", ids(pap, "ua1"));
        UserContext userContext = new UserContext(id(pap, "u1"));
        pap.modify().obligations().createObligation(userContext.getUser(), "obl1", List.of(new Rule(
                "rule1",
                new EventPattern(new SubjectPattern(), new OperationPattern()),
                new Response("e", List.of()))
        ));

        ExecutionContext execCtx = new ExecutionContext(userContext, pap);
        stmt.execute(execCtx, pap);

        assertTrue(pap.query().obligations().getObligation("obl1").getRules().isEmpty());
    }

    @Test
    void testToFormattedString() {
        DeleteRuleStatement stmt = new DeleteRuleStatement(
                new StringLiteral("rule1"), new StringLiteral("obl1"));

        assertEquals(
                """
                        delete rule "rule1" from obligation "obl1"
                        """,
                stmt.toFormattedString(0) + "\n"
        );
        assertEquals(
                """
                            delete rule "rule1" from obligation "obl1"
                        """,
                stmt.toFormattedString(1) + "\n"
        );
    }

}