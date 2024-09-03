package gov.nist.csd.pm.pap.pml.statement.operation;


import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.prohibition.ContainerCondition;
import gov.nist.csd.pm.pap.prohibition.Prohibition;
import gov.nist.csd.pm.pap.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pap.pml.expression.NegatedExpression;
import gov.nist.csd.pm.pap.pml.expression.literal.ArrayLiteral;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class CreateProhibitionStatementTest {

    @Test
    void testSuccess() throws PMException {
        CreateProhibitionStatement stmt = new CreateProhibitionStatement(
                new StringLiteral("pro1"),
                new StringLiteral("ua2"),
                ProhibitionSubject.Type.USER_ATTRIBUTE,
                buildArrayLiteral("read"),
                true,
                new ArrayLiteral(
                        List.of(new StringLiteral("oa1"), new NegatedExpression(new StringLiteral("oa2"))), Type.string()
                )
        );

        PAP pap = new MemoryPAP();
        pap.modify().operations().setResourceOperations(new AccessRightSet("read"));
        pap.modify().graph().createPolicyClass("pc2");
        pap.modify().graph().createUserAttribute("ua2", List.of("pc2"));
        pap.modify().graph().createUser("u2", List.of("ua2"));
        pap.modify().graph().createObjectAttribute("oa1", List.of("pc2"));
        pap.modify().graph().createObjectAttribute("oa2", List.of("pc2"));

        ExecutionContext execCtx = new ExecutionContext(new UserContext("u2"), pap);

        stmt.execute(execCtx, pap);

        assertDoesNotThrow(() -> pap.query().prohibitions().getProhibition("pro1"));

        Prohibition prohibition = pap.query().prohibitions().getProhibition("pro1");
        assertEquals(
                new ProhibitionSubject("ua2", ProhibitionSubject.Type.USER_ATTRIBUTE),
                prohibition.getSubject()
        );
        assertTrue(prohibition.isIntersection());
        assertEquals(
                new AccessRightSet("read"),
                prohibition.getAccessRightSet()
        );
        assertEquals(
                List.of(new ContainerCondition("oa1", false), new ContainerCondition("oa2", true)),
                prohibition.getContainers()
        );
    }

    @Test
    void testToFormattedString() {
        CreateProhibitionStatement stmt = new CreateProhibitionStatement(
                new StringLiteral("pro1"),
                new StringLiteral("ua2"),
                ProhibitionSubject.Type.USER_ATTRIBUTE,
                buildArrayLiteral("read"),
                true,
                new ArrayLiteral(
                        List.of(new StringLiteral("oa1"), new NegatedExpression(new StringLiteral("oa2"))), Type.string()
                )
        );
        assertEquals(
                "create prohibition \"pro1\"\n" +
                        "  deny UA \"ua2\"\n" +
                        "  access rights [\"read\"]\n" +
                        "  on intersection of [\"oa1\", !\"oa2\"]",
                stmt.toFormattedString(0)
        );
        assertEquals(
                "create prohibition \"pro1\"\n" +
                        "  deny UA \"ua2\"\n" +
                        "  access rights [\"read\"]\n" +
                        "  on intersection of [\"oa1\", !\"oa2\"]",
                stmt.toFormattedString(0)
        );
    }
}