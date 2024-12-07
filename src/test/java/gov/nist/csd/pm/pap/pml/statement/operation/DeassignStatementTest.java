package gov.nist.csd.pm.pap.pml.statement.operation;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class DeassignStatementTest {

    @Test
    void testSuccess() throws PMException {
        DeassignStatement stmt = new DeassignStatement(
                new StringLiteral("ua3"),
                buildArrayLiteral("ua1", "ua2")
        );

        PAP pap = new MemoryPAP();
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
        pap.modify().graph().createUserAttribute("ua2", List.of("pc1"));
        pap.modify().graph().createUserAttribute("ua3", List.of("ua1", "ua2", "pc1"));
        pap.modify().graph().createUser("u1", List.of("ua1"));

        ExecutionContext execCtx = new ExecutionContext(new UserContext("u1"), pap);
        stmt.execute(execCtx, pap);

        assertTrue(pap.query().graph().getAdjacentDescendants("ua3").contains("pc1"));
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