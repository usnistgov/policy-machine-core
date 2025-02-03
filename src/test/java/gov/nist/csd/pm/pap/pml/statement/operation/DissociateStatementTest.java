package gov.nist.csd.pm.pap.pml.statement.operation;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.util.TestPAP;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.util.TestIdGenerator.id;
import static gov.nist.csd.pm.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DissociateStatementTest {

    @Test
    void testSuccess() throws PMException {
        DissociateStatement stmt = new DissociateStatement(new StringLiteral("ua1"), new StringLiteral("oa1"));

        PAP pap = new TestPAP();
        pap.modify().operations().setResourceOperations(new AccessRightSet("read"));
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
        pap.modify().graph().createUser("u1", ids("ua1"));
        pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
        pap.modify().graph().associate(id("ua1"), id("oa1"), new AccessRightSet("read"));
        UserContext userContext = new TestUserContext("u1");

        stmt.execute(new ExecutionContext(userContext, pap), pap);

        assertTrue(pap.query().graph().getAssociationsWithSource(id("ua1")).isEmpty());
        assertTrue(pap.query().graph().getAssociationsWithTarget(id("oa1")).isEmpty());
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