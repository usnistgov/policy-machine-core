package gov.nist.csd.pm.core.pap.pml.statement.operation;


import static gov.nist.csd.pm.core.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static gov.nist.csd.pm.core.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.graph.Association;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import org.junit.jupiter.api.Test;

class AssociateStatementTest {

    @Test
    void testSuccess() throws PMException {
        AssociateStatement stmt = new AssociateStatement(
                new StringLiteralExpression("ua1"),
                new StringLiteralExpression("oa1"),
                buildArrayLiteral("read")
        );

        PAP pap = new TestPAP();
        pap.modify().operations().setResourceAccessRights(new AccessRightSet("read"));
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
                "associate \"ua1\" to \"oa1\" with [\"read\"]",
                stmt.toFormattedString(0)
        );
        assertEquals(
                "    associate \"ua1\" to \"oa1\" with [\"read\"]",
                stmt.toFormattedString(1)
        );
    }

}