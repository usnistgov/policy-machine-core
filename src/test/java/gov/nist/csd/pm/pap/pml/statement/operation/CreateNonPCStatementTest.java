package gov.nist.csd.pm.pap.pml.statement.operation;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.util.TestPAP;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;


import static gov.nist.csd.pm.util.TestIdGenerator.id;
import static gov.nist.csd.pm.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.*;

class CreateNonPCStatementTest {

    @Test
    void testSuccess() throws PMException {
        CreateNonPCStatement stmt1 = new CreateNonPCStatement(new StringLiteral("ua1"), NodeType.UA, buildArrayLiteral("pc1"));
        CreateNonPCStatement stmt2 = new CreateNonPCStatement(new StringLiteral("oa1"), NodeType.OA, buildArrayLiteral("pc1"));
        CreateNonPCStatement stmt3 = new CreateNonPCStatement(new StringLiteral("u1"), NodeType.U, buildArrayLiteral("ua1"));
        CreateNonPCStatement stmt4 = new CreateNonPCStatement(new StringLiteral("o1"), NodeType.O, buildArrayLiteral("oa1"));

        PAP pap = new TestPAP();
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua2", ids("pc1"));
        pap.modify().graph().createUser("u2", ids("ua2"));
        ExecutionContext execCtx = new ExecutionContext(new UserContext(id("u2")), pap);

        stmt1.execute(execCtx, pap);
        stmt2.execute(execCtx, pap);
        stmt3.execute(execCtx, pap);
        stmt4.execute(execCtx, pap);

        assertTrue(pap.query().graph().nodeExists("ua1"));
        assertTrue(pap.query().graph().nodeExists("oa1"));
        assertTrue(pap.query().graph().nodeExists("u1"));
        assertTrue(pap.query().graph().nodeExists("o1"));
        
        assertTrue(pap.query().graph().getAdjacentDescendants(id("ua1")).contains(id("pc1")));
        assertTrue(pap.query().graph().getAdjacentDescendants(id("oa1")).contains(id("pc1")));
        assertTrue(pap.query().graph().getAdjacentDescendants(id("u1")).contains(id("ua1")));
        assertTrue(pap.query().graph().getAdjacentDescendants(id("o1")).contains(id("oa1")));
    }

    @Test
    void testToFormattedString() {
        CreateNonPCStatement stmt = new CreateNonPCStatement(
                new StringLiteral("ua1"),
                NodeType.UA,
                buildArrayLiteral("ua2")
        );
        assertEquals(
                "create UA \"ua1\" in [\"ua2\"]",
                stmt.toFormattedString(0)
        );
        assertEquals(
                "    create UA \"ua1\" in [\"ua2\"]",
                stmt.toFormattedString(1)
        );
    }

}