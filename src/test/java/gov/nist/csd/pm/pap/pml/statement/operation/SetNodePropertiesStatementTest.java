package gov.nist.csd.pm.pap.pml.statement.operation;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.util.TestPAP;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildMapLiteral;
import static gov.nist.csd.pm.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SetNodePropertiesStatementTest {

    @Test
    void testSuccess() throws PMException {
        SetNodePropertiesStatement stmt = new SetNodePropertiesStatement(
                new StringLiteral("ua1"),
                buildMapLiteral("a", "b", "c", "d")
        );

        PAP pap = new TestPAP();
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
        pap.modify().graph().createUser("u1", ids("ua1"));
        UserContext userContext = new TestUserContext("u1");

        stmt.execute(new ExecutionContext(userContext, pap), pap);

        assertEquals(
                Map.of("a", "b", "c", "d"),
                pap.query().graph().getNodeByName("ua1").getProperties()
        );
    }

    @Test
    void testToFormattedString() {
        SetNodePropertiesStatement stmt = new SetNodePropertiesStatement(
                new StringLiteral("ua1"),
                buildMapLiteral("a", "b", "c", "d")
        );

        assertEquals(
                "set properties of \"ua1\" to {\"a\": \"b\", \"c\": \"d\"}",
                stmt.toFormattedString(0)
        );
        assertEquals(
                "    set properties of \"ua1\" to {\"a\": \"b\", \"c\": \"d\"}",
                stmt.toFormattedString(1)
        );
    }

}