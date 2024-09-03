package gov.nist.csd.pm.pap.pml.statement.operation;


import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.obligation.EventPattern;
import gov.nist.csd.pm.pap.obligation.Response;
import gov.nist.csd.pm.pap.obligation.Rule;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.exception.ProhibitionDoesNotExistException;
import gov.nist.csd.pm.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.pap.pml.pattern.subject.SubjectPattern;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.prohibition.ContainerCondition;
import gov.nist.csd.pm.pap.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeleteStatementTest {

    @Test
    void testSuccess() throws PMException {
        DeleteStatement stmt1 = new DeleteStatement(DeleteStatement.Type.OBJECT_ATTRIBUTE, new StringLiteral("oa1"));
        DeleteStatement stmt2 = new DeleteStatement(DeleteStatement.Type.PROHIBITION, new StringLiteral("p1"));
        DeleteStatement stmt3 = new DeleteStatement(DeleteStatement.Type.OBLIGATION, new StringLiteral("o1"));

        PAP pap = new MemoryPAP();
        pap.modify().operations().setResourceOperations(new AccessRightSet("read"));
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
        pap.modify().graph().createUser("u1", List.of("ua1"));
        pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
        pap.modify().graph().createObjectAttribute("oa2", List.of("pc1"));
        UserContext userContext = new UserContext("u1");
        pap.modify().obligations().createObligation(userContext.getUser(), "o1", List.of(new Rule(
                "rule1",
                new EventPattern(new SubjectPattern(), new OperationPattern("e1")),
                new Response("e", List.of())
        )));
        pap.modify().prohibitions().createProhibition("p1",
                                    new ProhibitionSubject("ua1", ProhibitionSubject.Type.USER_ATTRIBUTE),
                                    new AccessRightSet("read"),
                                    true,
                Collections.singleton(new ContainerCondition("oa1", true))
        );

        stmt2.execute(new ExecutionContext(userContext, pap), pap);
        stmt3.execute(new ExecutionContext(userContext, pap), pap);
        stmt1.execute(new ExecutionContext(userContext, pap), pap);

        assertFalse(pap.query().graph().nodeExists("oa1"));
        assertThrows(ProhibitionDoesNotExistException.class, () -> pap.query().prohibitions().getProhibition("p1"));
        assertFalse(pap.query().obligations().obligationExists("o1"));
    }

    @Test
    void testToFormattedString() {
        DeleteStatement stmt = new DeleteStatement(DeleteStatement.Type.OBJECT_ATTRIBUTE, new StringLiteral("test"));
        DeleteStatement stmt1 = new DeleteStatement(DeleteStatement.Type.OBLIGATION, new StringLiteral("test"));
        DeleteStatement stmt2 = new DeleteStatement(DeleteStatement.Type.PROHIBITION, new StringLiteral("test"));
        DeleteStatement stmt3 = new DeleteStatement(DeleteStatement.Type.OBJECT, new StringLiteral("test"));
        DeleteStatement stmt4 = new DeleteStatement(DeleteStatement.Type.POLICY_CLASS, new StringLiteral("test"));
        DeleteStatement stmt5 = new DeleteStatement(DeleteStatement.Type.USER, new StringLiteral("test"));
        DeleteStatement stmt6 = new DeleteStatement(DeleteStatement.Type.USER_ATTRIBUTE, new StringLiteral("test"));

        assertEquals("delete OA \"test\"", stmt.toFormattedString(0));
        assertEquals("delete obligation \"test\"", stmt1.toFormattedString(0));
        assertEquals("delete prohibition \"test\"", stmt2.toFormattedString(0));
        assertEquals("delete O \"test\"", stmt3.toFormattedString(0));
        assertEquals("delete PC \"test\"", stmt4.toFormattedString(0));
        assertEquals("delete U \"test\"", stmt5.toFormattedString(0));
        assertEquals("delete UA \"test\"", stmt6.toFormattedString(0));
        assertEquals(
                "    delete OA \"test\"\n",
                stmt.toFormattedString(1) + "\n"
        );
    }

}