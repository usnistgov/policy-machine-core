package gov.nist.csd.pm.core.pap.pml.statement.operation;


import static gov.nist.csd.pm.core.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static gov.nist.csd.pm.core.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import org.junit.jupiter.api.Test;

class DeassignStatementTest {

    @Test
    void testSuccess() throws PMException {
        DeassignStatement stmt = new DeassignStatement(
                new StringLiteralExpression("ua3"),
                buildArrayLiteral("ua1", "ua2")
        );

        PAP pap = new TestPAP();
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
        pap.modify().graph().createUserAttribute("ua2", ids("pc1"));
        pap.modify().graph().createUserAttribute("ua3", ids("ua1", "ua2", "pc1"));
        pap.modify().graph().createUser("u1", ids("ua1"));

        ExecutionContext execCtx = new ExecutionContext(new TestUserContext("u1"), pap);
        stmt.execute(execCtx, pap);

        assertTrue(pap.query().graph().getAdjacentDescendants(id("ua3")).contains(id("pc1")));
    }

    @Test
    void testToFormattedString() {
        DeassignStatement stmt = new DeassignStatement(
                new StringLiteralExpression("ua3"),
                buildArrayLiteral("ua1", "ua2")
        );

        assertEquals(
                """
                        deassign "ua3" from ["ua1", "ua2"]""",
                stmt.toFormattedString(0)
        );
        assertEquals(
                """
                            deassign "ua3" from ["ua1", "ua2"]
                        """,
                stmt.toFormattedString(1) + "\n"
        );
    }

}