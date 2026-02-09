package gov.nist.csd.pm.core.pap.pml.statement.operation;


import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static gov.nist.csd.pm.core.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.exception.ProhibitionDoesNotExistException;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.obligation.event.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.event.operation.AnyOperationPattern;
import gov.nist.csd.pm.core.pap.obligation.event.subject.SubjectPattern;
import gov.nist.csd.pm.core.pap.obligation.response.ObligationResponse;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DeleteStatementTest {

    @Test
    void testSuccess() throws PMException {
        DeleteStatement stmt1 = new DeleteNodeStatement(new StringLiteralExpression("oa1"), false);
        DeleteStatement stmt2 = new DeleteProhibitionStatement(new StringLiteralExpression("p1"), false);
        DeleteStatement stmt3 = new DeleteObligationStatement(new StringLiteralExpression("o1"), false);

        PAP pap = new TestPAP();
        pap.modify().operations().setResourceAccessRights(new AccessRightSet("read"));
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
        pap.modify().graph().createUser("u1", ids("ua1"));
        pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
        pap.modify().graph().createObjectAttribute("oa2", ids("pc1"));
        UserContext userContext = new TestUserContext("u1");
        pap.modify().obligations().createObligation(userContext.getUser(), "o1",
                new EventPattern(new SubjectPattern(), new AnyOperationPattern()),
                new ObligationResponse("e", List.of())
        );
        pap.modify().prohibitions().createNodeProhibition("p1",
                id("ua1"),
		        new AccessRightSet("read"),
		        Set.of(), Set.of(id("oa1")), true);

        stmt2.execute(new ExecutionContext(userContext, pap), pap);
        stmt3.execute(new ExecutionContext(userContext, pap), pap);
        stmt1.execute(new ExecutionContext(userContext, pap), pap);

        assertFalse(pap.query().graph().nodeExists("oa1"));
        assertThrows(ProhibitionDoesNotExistException.class, () -> pap.query().prohibitions().getProhibition("p1"));
        assertFalse(pap.query().obligations().obligationExists("o1"));
    }

    @Test
    void testSuccessIfExists() throws PMException {
        DeleteStatement stmt1 = new DeleteNodeStatement(new StringLiteralExpression("oa1"), true);
        DeleteStatement stmt2 = new DeleteProhibitionStatement(new StringLiteralExpression("p1"), true);
        DeleteStatement stmt3 = new DeleteObligationStatement(new StringLiteralExpression("o1"), true);

        PAP pap = new TestPAP();
        TestUserContext testUserContext = new TestUserContext("u1");
        assertDoesNotThrow(() -> stmt1.execute(new ExecutionContext(testUserContext, pap), pap));
        assertDoesNotThrow(() -> stmt2.execute(new ExecutionContext(testUserContext, pap), pap));
        assertDoesNotThrow(() -> stmt3.execute(new ExecutionContext(testUserContext, pap), pap));
    }

    @Test
    void testToFormattedString() {
        DeleteStatement stmt = new DeleteNodeStatement(new StringLiteralExpression("test"), false);
        DeleteStatement stmt1 = new DeleteProhibitionStatement(new StringLiteralExpression("test"), false);
        DeleteStatement stmt2 = new DeleteObligationStatement( new StringLiteralExpression("test"), false);
        DeleteStatement stmt3 = new DeleteNodeStatement(new StringLiteralExpression("test"), false);
        DeleteStatement stmt4 = new DeleteNodeStatement(new StringLiteralExpression("test"), false);
        DeleteStatement stmt5 = new DeleteNodeStatement(new StringLiteralExpression("test"), false);
        DeleteStatement stmt6 = new DeleteNodeStatement(new StringLiteralExpression("test"), true);

        assertEquals("delete node \"test\"", stmt.toFormattedString(0));
        assertEquals("delete prohibition \"test\"", stmt1.toFormattedString(0));
        assertEquals("delete obligation \"test\"", stmt2.toFormattedString(0));
        assertEquals("delete node \"test\"", stmt3.toFormattedString(0));
        assertEquals("delete node \"test\"", stmt4.toFormattedString(0));
        assertEquals("delete node \"test\"", stmt5.toFormattedString(0));
        assertEquals("delete if exists node \"test\"", stmt6.toFormattedString(0));
        assertEquals(
                """
                            delete node "test"
                        """,
                stmt.toFormattedString(1) + "\n"
        );
    }

}