package gov.nist.csd.pm.pap.pml.statement.operation;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.util.TestPAP;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.util.TestIdGenerator.id;
import static gov.nist.csd.pm.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AssociateStatementTest {

    @Test
    void testSuccess() throws PMException {
        AssociateStatement stmt = new AssociateStatement(
                new StringLiteralExpression("ua1"),
                new StringLiteralExpression("oa1"),
                buildArrayLiteral("read")
        );

        PAP pap = new TestPAP();
        pap.modify().operations().setResourceOperations(new AccessRightSet("read"));
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
        pap.modify().graph().createUserAttribute("u1", ids("pc1"));
        pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
        ExecutionContext execCtx = new ExecutionContext(new TestUserContext("u1"), pap);
        stmt.execute(execCtx, pap);

	    assertEquals(
                pap.query().graph().getAssociationsWithSource(id("ua1")).iterator().next(),
                new Association(id("ua1"), id("oa1"), new AccessRightSet("read"))
        );
	    assertEquals(
                pap.query().graph().getAssociationsWithTarget(id("oa1")).iterator().next(),
                new Association(id("ua1"), id("oa1"), new AccessRightSet("read"))
        );
    }

    @Test
    void testToFormattedString() {
        AssociateStatement stmt = new AssociateStatement(
                new StringLiteralExpression("ua1"),
                new StringLiteralExpression("oa1"),
                buildArrayLiteral("read")
        );
        assertEquals(
                "associate \"ua1\" and \"oa1\" with [\"read\"]",
                stmt.toFormattedString(0)
        );
        assertEquals(
                "    associate \"ua1\" and \"oa1\" with [\"read\"]",
                stmt.toFormattedString(1)
        );
    }

}