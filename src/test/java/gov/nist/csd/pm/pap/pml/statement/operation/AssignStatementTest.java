package gov.nist.csd.pm.pap.pml.statement.operation;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.util.TestMemoryPAP.id;
import static gov.nist.csd.pm.util.TestMemoryPAP.ids;
import static org.junit.jupiter.api.Assertions.*;

class AssignStatementTest {

    @Test
    void testSuccess() throws PMException {
        AssignStatement stmt = new AssignStatement(new StringLiteral("u1"), buildArrayLiteral("ua2", "ua3"));

        PAP pap = new MemoryPAP();
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", ids(pap, ("pc1")));
        pap.modify().graph().createUserAttribute("ua2", ids(pap, ("pc1")));
        pap.modify().graph().createUserAttribute("ua3", ids(pap, ("pc1")));
        pap.modify().graph().createUser("u1", ids(pap, ("ua1")));
        ExecutionContext execCtx = new ExecutionContext(new TestUserContext("u1", pap), pap);
        stmt.execute(execCtx, pap);

        assertTrue(Arrays.stream(pap.query().graph().getAdjacentDescendants(id(pap, "u1")))
                .boxed().toList()
                .containsAll(ids(pap, "ua1", "ua2", "ua3")));
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