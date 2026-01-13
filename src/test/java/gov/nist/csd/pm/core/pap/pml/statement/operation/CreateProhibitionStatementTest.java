package gov.nist.csd.pm.core.pap.pml.statement.operation;


import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubjectType;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;

import gov.nist.csd.pm.core.pap.pml.expression.literal.BoolLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.MapLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static gov.nist.csd.pm.core.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.*;

class CreateProhibitionStatementTest {

    @Test
    void testSuccess() throws PMException {
        CreateProhibitionStatement stmt = new CreateProhibitionStatement(
            new StringLiteralExpression("pro1"),
            new StringLiteralExpression("ua2"),
            ProhibitionSubjectType.USER_ATTRIBUTE,
            buildArrayLiteral("read"),
            true,
            MapLiteralExpression.of(Map.of(
                new StringLiteralExpression("oa1"), new BoolLiteralExpression(false),
                new StringLiteralExpression("oa2"), new BoolLiteralExpression(true)
            ), STRING_TYPE, BOOLEAN_TYPE)
        );

        PAP pap = new TestPAP();
        pap.modify().operations().setResourceOperations(new AccessRightSet("read"));
        pap.modify().graph().createPolicyClass("pc2");
        pap.modify().graph().createUserAttribute("ua2", ids("pc2"));
        pap.modify().graph().createUser("u2", ids("ua2"));
        pap.modify().graph().createObjectAttribute("oa1", ids("pc2"));
        pap.modify().graph().createObjectAttribute("oa2", ids("pc2"));

        ExecutionContext execCtx = new ExecutionContext(new UserContext(id("u2")), pap);

        stmt.execute(execCtx, pap);

        assertDoesNotThrow(() -> pap.query().prohibitions().getProhibition("pro1"));

        Prohibition prohibition = pap.query().prohibitions().getProhibition("pro1");
        assertEquals(
            new ProhibitionSubject(id("ua2")),
            prohibition.getSubject()
        );
        assertTrue(prohibition.isIntersection());
        assertEquals(
            new AccessRightSet("read"),
            prohibition.getAccessRightSet()
        );
        assertTrue(prohibition.getContainers().contains(new ContainerCondition(id("oa1"), false)));
        assertTrue(prohibition.getContainers().contains(new ContainerCondition(id("oa2"), true)));
    }

    @Test
    void testToFormattedString() {
        CreateProhibitionStatement stmt = new CreateProhibitionStatement(
            new StringLiteralExpression("pro1"),
            new StringLiteralExpression("ua2"),
            ProhibitionSubjectType.USER_ATTRIBUTE,
            buildArrayLiteral("read"),
            true,
            MapLiteralExpression.of(Map.of(
                new StringLiteralExpression("oa1"), new BoolLiteralExpression(false),
                new StringLiteralExpression("oa2"), new BoolLiteralExpression(true)
            ), STRING_TYPE, BOOLEAN_TYPE)
        );

        String str = stmt.toFormattedString(0);
        assertTrue(str.contains("create prohibition \"pro1\""));
        assertTrue(str.contains("  deny UA \"ua2\""));
        assertTrue(str.contains("  access rights [\"read\"]"));
        assertTrue(str.contains("  on intersection of "));
        assertTrue(str.contains("\"oa1\": false"));
        assertTrue(str.contains("\"oa2\": true"));

        str = stmt.toFormattedString(1);
        assertTrue(str.contains("    create prohibition \"pro1\""));
        assertTrue(str.contains("      deny UA \"ua2\""));
        assertTrue(str.contains("      access rights [\"read\"]"));
        assertTrue(str.contains("      on intersection of "));
        assertTrue(str.contains("\"oa1\": false"));
        assertTrue(str.contains("\"oa2\": true"));
    }
}