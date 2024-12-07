package gov.nist.csd.pm.pap.pml.statement.operation;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class AssociateStatementTest {

    @Test
    void testSuccess() throws PMException {
        AssociateStatement stmt = new AssociateStatement(
                new StringLiteral("ua1"),
                new StringLiteral("oa1"),
                buildArrayLiteral("read")
        );

        PAP pap = new MemoryPAP();
        pap.modify().operations().setResourceOperations(new AccessRightSet("read"));
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
        pap.modify().graph().createUserAttribute("u1", List.of("pc1"));
        pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
        ExecutionContext execCtx = new ExecutionContext(new UserContext("u1"), pap);
        stmt.execute(execCtx, pap);

        assertTrue(pap.query().graph().getAssociationsWithSource("ua1").iterator().next().equals(new Association("ua1", "oa1", new AccessRightSet("read"))));
        assertTrue(pap.query().graph().getAssociationsWithTarget("oa1").iterator().next().equals(new Association("ua1", "oa1", new AccessRightSet("read"))));
    }

    @Test
    void testToFormattedString() {
        AssociateStatement stmt = new AssociateStatement(
                new StringLiteral("ua1"),
                new StringLiteral("oa1"),
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