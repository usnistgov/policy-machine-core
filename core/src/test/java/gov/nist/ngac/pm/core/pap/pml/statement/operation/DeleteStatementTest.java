/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.pml.statement.operation;


import static gov.nist.ngac.pm.core.util.TestIdGenerator.id;
import static gov.nist.ngac.pm.core.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.exception.ProhibitionDoesNotExistException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.core.pap.obligation.event.EventPattern;
import gov.nist.ngac.pm.core.pap.obligation.event.operation.AnyOperationPattern;
import gov.nist.ngac.pm.core.pap.obligation.event.subject.SubjectPattern;
import gov.nist.ngac.pm.core.pap.obligation.response.ObligationResponse;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.core.util.TestPAP;
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
        UserContext userContext = NodeUserContext.of("u1");
        pap.modify().obligations().createObligation(new Obligation(NodeUserContext.of(id("u1")), "o1",
                new EventPattern(new SubjectPattern(), new AnyOperationPattern()),
                new ObligationResponse("e", List.of())
        ));
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
        UserContext testUserContext = NodeUserContext.of("u1");
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