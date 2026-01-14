package gov.nist.csd.pm.core.pap.pml.statement.operation;


import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.core.pap.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SetResourceAccessRightsStatementTest {

    @Test
    void testSuccess() throws PMException {
        SetResourceAccessRightsStatement stmt = new SetResourceAccessRightsStatement(
                buildArrayLiteral("a", "b", "c", "d")
        );

        PAP pap = new TestPAP();

        stmt.execute(new ExecutionContext(new UserContext(0), pap), pap);

        assertEquals(
                new AccessRightSet("a", "b", "c", "d"),
                pap.query().operations().getResourceAccessRights()
        );
    }

    @Test
    void testToFormattedString() {
        SetResourceAccessRightsStatement stmt = new SetResourceAccessRightsStatement(
                buildArrayLiteral("a", "b", "c", "d")
        );

        assertEquals(
                "set resource operations [\"a\", \"b\", \"c\", \"d\"]",
                stmt.toFormattedString(0)
        );
        assertEquals(
                "    set resource operations [\"a\", \"b\", \"c\", \"d\"]",
                stmt.toFormattedString(1)
        );
    }

}