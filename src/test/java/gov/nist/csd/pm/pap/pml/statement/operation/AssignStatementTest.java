package gov.nist.csd.pm.pap.pml.statement.operation;


import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class AssignStatementTest {

    @Test
    void testSuccess() throws PMException {
        AssignStatement stmt = new AssignStatement(new StringLiteral("u1"), buildArrayLiteral("ua2", "ua3"));

        PAP pap = new MemoryPAP();
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
        pap.modify().graph().createUserAttribute("ua2", List.of("pc1"));
        pap.modify().graph().createUserAttribute("ua3", List.of("pc1"));
        pap.modify().graph().createUser("u1", List.of("ua1"));
        ExecutionContext execCtx = new ExecutionContext(new UserContext("u1"), pap);
        stmt.execute(execCtx, pap);

        assertTrue(pap.query().graph().getAdjacentDescendants("u1").containsAll(List.of("ua1", "ua2", "ua3")));
    }

    @Test
    void testToFormattedString() {
        AssignStatement stmt = new AssignStatement(new StringLiteral("u1"), buildArrayLiteral("ua2", "ua3"));
        assertEquals(
                "assign \"u1\" to [\"ua2\", \"ua3\"]",
                stmt.toFormattedString(0)
        );
        assertEquals(
                "    assign \"u1\" to [\"ua2\", \"ua3\"]",
                stmt.toFormattedString(1)
        );
    }

}