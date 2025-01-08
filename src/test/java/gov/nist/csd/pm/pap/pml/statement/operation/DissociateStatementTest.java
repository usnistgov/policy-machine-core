package gov.nist.csd.pm.pap.pml.statement.operation;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class DissociateStatementTest {

    @Test
    void testSuccess() throws PMException {
        DissociateStatement stmt = new DissociateStatement(new StringLiteral("ua1"), new StringLiteral("oa1"));

        PAP pap = new MemoryPAP();
        pap.modify().operations().setResourceOperations(new AccessRightSet("read"));
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
        pap.modify().graph().createUser("u1", List.of("ua1"));
        pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
        pap.modify().graph().associate("ua1", "oa1", new AccessRightSet("read"));
        UserContext userContext = new UserContext("u1");

        stmt.execute(new ExecutionContext(userContext, pap), pap);

        assertTrue(pap.query().graph().getAssociationsWithSource("ua1").isEmpty());
        assertTrue(pap.query().graph().getAssociationsWithTarget("oa1").isEmpty());
    }

    @Test
    void testToFormattedString() {
        DissociateStatement stmt = new DissociateStatement(new StringLiteral("ua1"), buildArrayLiteral("oa1"));

        assertEquals("dissociate \"ua1\" and [\"oa1\"]", stmt.toFormattedString(0));
        assertEquals(
                "    dissociate \"ua1\" and [\"oa1\"]",
                stmt.toFormattedString(1)
        );
    }

}