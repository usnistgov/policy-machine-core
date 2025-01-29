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
import static gov.nist.csd.pm.util.TestMemoryPAP.id;
import static gov.nist.csd.pm.util.TestMemoryPAP.ids;
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
        pap.modify().graph().createUserAttribute("ua1", ids(pap, "pc1"));
        pap.modify().graph().createUserAttribute("u1", ids(pap, "pc1"));
        pap.modify().graph().createObjectAttribute("oa1", ids(pap, "pc1"));
        ExecutionContext execCtx = new ExecutionContext(new UserContext(id(pap, "u1")), pap);
        stmt.execute(execCtx, pap);

	    assertEquals(
                pap.query().graph().getAssociationsWithSource(id(pap, "ua1")).iterator().next(),
                new Association(id(pap, "ua1"), id(pap, "oa1"), new AccessRightSet("read"))
        );
	    assertEquals(
                pap.query().graph().getAssociationsWithTarget(id(pap, "oa1")).iterator().next(),
                new Association(id(pap, "ua1"), id(pap, "oa1"), new AccessRightSet("read"))
        );
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