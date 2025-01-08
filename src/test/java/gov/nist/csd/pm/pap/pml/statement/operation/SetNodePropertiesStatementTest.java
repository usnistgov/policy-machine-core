package gov.nist.csd.pm.pap.pml.statement.operation;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildMapLiteral;
import static org.junit.jupiter.api.Assertions.*;

class SetNodePropertiesStatementTest {

    @Test
    void testSuccess() throws PMException {
        SetNodePropertiesStatement stmt = new SetNodePropertiesStatement(
                new StringLiteral("ua1"),
                buildMapLiteral("a", "b", "c", "d")
        );

        PAP pap = new MemoryPAP();
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
        pap.modify().graph().createUser("u1", List.of("ua1"));
        UserContext userContext = new UserContext("u1");

        stmt.execute(new ExecutionContext(userContext, pap), pap);

        assertEquals(
                Map.of("a", "b", "c", "d"),
                pap.query().graph().getNode("ua1").getProperties()
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