package gov.nist.csd.pm.pap.pml.statement.operation;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.exception.ProhibitionDoesNotExistException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.obligation.EventPattern;
import gov.nist.csd.pm.common.obligation.Response;
import gov.nist.csd.pm.common.obligation.Rule;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.pap.pml.pattern.subject.SubjectPattern;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.util.TestPAP;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static gov.nist.csd.pm.util.TestIdGenerator.id;
import static gov.nist.csd.pm.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.*;

class DeleteStatementTest {

    @Test
    void testSuccess() throws PMException {
        DeleteStatement stmt1 = new DeleteNodeStatement(new StringLiteral("oa1"));
        DeleteStatement stmt2 = new DeleteProhibitionStatement(new StringLiteral("p1"));
        DeleteStatement stmt3 = new DeleteObligationStatement(new StringLiteral("o1"));

        PAP pap = new TestPAP();
        pap.modify().operations().setResourceOperations(new AccessRightSet("read"));
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
        pap.modify().graph().createUser("u1", ids("ua1"));
        pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
        pap.modify().graph().createObjectAttribute("oa2", ids("pc1"));
        UserContext userContext = new TestUserContext("u1");
        pap.modify().obligations().createObligation(userContext.getUser(), "o1", List.of(new Rule(
                "rule1",
                new EventPattern(new SubjectPattern(), new OperationPattern("e1")),
                new Response("e", List.of())
        )));
        pap.modify().prohibitions().createProhibition("p1",
                                    new ProhibitionSubject(id("ua1")),
		        new AccessRightSet("read"),
		        true,
		        Collections.singleton(new ContainerCondition(id("oa1"), true)));

        stmt2.execute(new ExecutionContext(userContext, pap), pap);
        stmt3.execute(new ExecutionContext(userContext, pap), pap);
        stmt1.execute(new ExecutionContext(userContext, pap), pap);

        assertFalse(pap.query().graph().nodeExists("oa1"));
        assertThrows(ProhibitionDoesNotExistException.class, () -> pap.query().prohibitions().getProhibition("p1"));
        assertFalse(pap.query().obligations().obligationExists("o1"));
    }

    @Test
    void testToFormattedString() {
        DeleteStatement stmt = new DeleteNodeStatement(new StringLiteral("test"));
        DeleteStatement stmt1 = new DeleteProhibitionStatement(new StringLiteral("test"));
        DeleteStatement stmt2 = new DeleteObligationStatement( new StringLiteral("test"));
        DeleteStatement stmt3 = new DeleteNodeStatement(new StringLiteral("test"));
        DeleteStatement stmt4 = new DeleteNodeStatement(new StringLiteral("test"));
        DeleteStatement stmt5 = new DeleteNodeStatement(new StringLiteral("test"));
        DeleteStatement stmt6 = new DeleteNodeStatement(new StringLiteral("test"));

        assertEquals("delete node \"test\"", stmt.toFormattedString(0));
        assertEquals("delete prohibition \"test\"", stmt1.toFormattedString(0));
        assertEquals("delete obligation \"test\"", stmt2.toFormattedString(0));
        assertEquals("delete node \"test\"", stmt3.toFormattedString(0));
        assertEquals("delete node \"test\"", stmt4.toFormattedString(0));
        assertEquals("delete node \"test\"", stmt5.toFormattedString(0));
        assertEquals("delete node \"test\"", stmt6.toFormattedString(0));
        assertEquals(
                """
                            delete node "test"
                        """,
                stmt.toFormattedString(1) + "\n"
        );
    }

}