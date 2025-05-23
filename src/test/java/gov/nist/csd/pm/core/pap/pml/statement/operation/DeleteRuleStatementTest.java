package gov.nist.csd.pm.core.pap.pml.statement.operation;


import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.obligation.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.Response;
import gov.nist.csd.pm.core.pap.obligation.Rule;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.core.pap.pml.pattern.subject.SubjectPattern;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.core.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeleteRuleStatementTest {

    @Test
    void testSuccess() throws PMException {
        DeleteRuleStatement stmt = new DeleteRuleStatement(
                new StringLiteralExpression("rule1"), new StringLiteralExpression("obl1"));

        PAP pap = new TestPAP();
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
        pap.modify().graph().createUser("u1", ids("ua1"));
        UserContext userContext = new TestUserContext("u1");
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
                new StringLiteralExpression("rule1"), new StringLiteralExpression("obl1"));

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