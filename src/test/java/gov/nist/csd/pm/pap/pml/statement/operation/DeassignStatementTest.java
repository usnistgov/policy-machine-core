package gov.nist.csd.pm.pap.pml.statement.operation;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.util.TestPAP;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.util.TestIdGenerator.id;
import static gov.nist.csd.pm.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeassignStatementTest {

    @Test
    void testSuccess() throws PMException {
        DeassignStatement stmt = new DeassignStatement(
                new StringLiteral("ua3"),
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
                new StringLiteral("ua3"),
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