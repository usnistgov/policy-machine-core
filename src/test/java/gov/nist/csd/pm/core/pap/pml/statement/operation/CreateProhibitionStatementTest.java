package gov.nist.csd.pm.core.pap.pml.statement.operation;


import static gov.nist.csd.pm.core.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static gov.nist.csd.pm.core.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.prohibition.NodeProhibition;
import gov.nist.csd.pm.core.common.prohibition.ProcessProhibition;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CreateProhibitionStatementTest {

    @Test
    void testNodeProhibitionSuccess() throws PMException {
        CreateProhibitionStatement stmt = CreateProhibitionStatement.nodeProhibition(
            new StringLiteralExpression("pro1"),
            new StringLiteralExpression("ua2"),
            buildArrayLiteral("read"),
            buildArrayLiteral("oa1"),
            buildArrayLiteral("oa2"),
            true
        );

        PAP pap = new TestPAP();
        pap.modify().operations().setResourceAccessRights(new AccessRightSet("read"));
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua2", ids("pc1"));
        pap.modify().graph().createUser("u1", ids("ua2"));
        pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
        pap.modify().graph().createObjectAttribute("oa2", ids("pc1"));

        ExecutionContext execCtx = new ExecutionContext(new UserContext(id("u1")), pap);

        stmt.execute(execCtx, pap);

        assertDoesNotThrow(() -> pap.query().prohibitions().getProhibition("pro1"));

        Prohibition prohibition = pap.query().prohibitions().getProhibition("pro1");
        assertInstanceOf(NodeProhibition.class, prohibition);

        NodeProhibition nodeProhibition = (NodeProhibition) prohibition;
        assertEquals("pro1", nodeProhibition.getName());
        assertEquals(id("ua2"), nodeProhibition.getNodeId());
        assertEquals(new AccessRightSet("read"), nodeProhibition.getAccessRightSet());
        assertTrue(nodeProhibition.isConjunctive());
        assertEquals(Set.of(id("oa1")), nodeProhibition.getInclusionSet());
        assertEquals(Set.of(id("oa2")), nodeProhibition.getExclusionSet());
    }

    @Test
    void testProcessProhibitionSuccess() throws PMException {
        CreateProhibitionStatement stmt = CreateProhibitionStatement.processProhibition(
            new StringLiteralExpression("pro2"),
            new StringLiteralExpression("u1"),
            new StringLiteralExpression("proc1"),
            buildArrayLiteral("read", "write"),
            buildArrayLiteral("oa1"),
            buildArrayLiteral("oa2"),
            false
        );

        PAP pap = new TestPAP();
        pap.modify().operations().setResourceAccessRights(new AccessRightSet("read", "write"));
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
        pap.modify().graph().createUser("u1", ids("ua1"));
        pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
        pap.modify().graph().createObjectAttribute("oa2", ids("pc1"));

        ExecutionContext execCtx = new ExecutionContext(new UserContext(id("u1")), pap);

        stmt.execute(execCtx, pap);

        assertDoesNotThrow(() -> pap.query().prohibitions().getProhibition("pro2"));

        Prohibition prohibition = pap.query().prohibitions().getProhibition("pro2");
        assertInstanceOf(ProcessProhibition.class, prohibition);

        ProcessProhibition processProhibition = (ProcessProhibition) prohibition;
        assertEquals("pro2", processProhibition.getName());
        assertEquals(id("u1"), processProhibition.getUserId());
        assertEquals("proc1", processProhibition.getProcess());
        assertEquals(new AccessRightSet("read", "write"), processProhibition.getAccessRightSet());
        assertEquals(false, processProhibition.isConjunctive());
        assertEquals(Set.of(id("oa1")), processProhibition.getInclusionSet());
        assertEquals(Set.of(id("oa2")), processProhibition.getExclusionSet());
    }

    @Test
    void testToFormattedStringNodeProhibition() {
        CreateProhibitionStatement stmt = CreateProhibitionStatement.nodeProhibition(
            new StringLiteralExpression("pro1"),
            new StringLiteralExpression("ua2"),
            buildArrayLiteral("read"),
            buildArrayLiteral("oa1"),
            buildArrayLiteral("oa2"),
            true
        );

        String str = stmt.toFormattedString(0);
        assertEquals(
            "create conj node prohibition \"pro1\"\n" +
            "deny \"ua2\"\n" +
            "arset [\"read\"]\n" +
            "include [\"oa1\"]\n" +
            "exclude [\"oa2\"]",
            str
        );

        str = stmt.toFormattedString(1);
        assertEquals(
            "    create conj node prohibition \"pro1\"\n" +
            "    deny \"ua2\"\n" +
            "    arset [\"read\"]\n" +
            "    include [\"oa1\"]\n" +
            "    exclude [\"oa2\"]",
            str
        );
    }

    @Test
    void testToFormattedStringNodeProhibitionDisjunctive() {
        CreateProhibitionStatement stmt = CreateProhibitionStatement.nodeProhibition(
            new StringLiteralExpression("pro2"),
            new StringLiteralExpression("ua1"),
            buildArrayLiteral("write"),
            buildArrayLiteral("oa1", "oa2"),
            buildArrayLiteral("oa3"),
            false
        );

        String str = stmt.toFormattedString(0);
        assertEquals(
            "create disj node prohibition \"pro2\"\n" +
            "deny \"ua1\"\n" +
            "arset [\"write\"]\n" +
            "include [\"oa1\", \"oa2\"]\n" +
            "exclude [\"oa3\"]",
            str
        );
    }

    @Test
    void testToFormattedStringProcessProhibition() {
        CreateProhibitionStatement stmt = CreateProhibitionStatement.processProhibition(
            new StringLiteralExpression("pro3"),
            new StringLiteralExpression("u1"),
            new StringLiteralExpression("proc1"),
            buildArrayLiteral("read", "write"),
            buildArrayLiteral("oa1"),
            buildArrayLiteral("oa2"),
            true
        );

        String str = stmt.toFormattedString(0);
        assertEquals(
            "create conj process prohibition \"pro3\"\n" +
            "deny \"u1\" process \"proc1\"\n" +
            "arset [\"read\", \"write\"]\n" +
            "include [\"oa1\"]\n" +
            "exclude [\"oa2\"]",
            str
        );

        str = stmt.toFormattedString(1);
        assertEquals(
            "    create conj process prohibition \"pro3\"\n" +
            "    deny \"u1\" process \"proc1\"\n" +
            "    arset [\"read\", \"write\"]\n" +
            "    include [\"oa1\"]\n" +
            "    exclude [\"oa2\"]",
            str
        );
    }
}
